package br.ifal.med_gestao.activity

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.CalendarView
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import br.ifal.med_gestao.R
import br.ifal.med_gestao.adapters.ScheduleRadioButtonAdapter
import br.ifal.med_gestao.clients.RetrofitHelper
import br.ifal.med_gestao.databinding.RadioButtonItemBinding
import br.ifal.med_gestao.databinding.ScheduleAppointmentFormActivityBinding
import br.ifal.med_gestao.domain.Appointment
import br.ifal.med_gestao.domain.Doctor
import br.ifal.med_gestao.domain.DoctorSchedule
import br.ifal.med_gestao.domain.Patient
import br.ifal.med_gestao.service.AppointmentService
import br.ifal.med_gestao.service.DoctorService
import br.ifal.med_gestao.util.Notification
import com.bumptech.glide.Glide
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Calendar


class ScheduleAppointmentFormActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        supportActionBar?.setCustomView(R.layout.custom_toolbar_title);

        val binding = ScheduleAppointmentFormActivityBinding.inflate(layoutInflater)
        val bindingRadioButtonLayout = RadioButtonItemBinding.inflate(layoutInflater)

        val doctor = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("doctor", Doctor::class.java)
        } else {
            intent.getParcelableExtra("doctor")
        }

        val patient = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("patient", Patient::class.java)
        } else {
            intent.getParcelableExtra("patient")
        }

        // Set imagem
        Glide.with(this)
            .load(doctor?.image)
            .into(binding.appointmentDoctorImg)

        binding.appointmentDoctorName.text = doctor?.name
        binding.appointmentDoctorSpecialty.text = doctor?.specialty

        val calendarView = binding.appointmentCalendar
        val selectedDate = toLocalDate(calendarView.date)
        var radioGroup = binding.appointmentRadioGroup
        callApi(this, doctor, selectedDate, binding, bindingRadioButtonLayout, radioGroup)

        calendarConfiguration(this, calendarView, doctor, binding, bindingRadioButtonLayout, radioGroup)

        val button = binding.appointmentButton
        button.setOnClickListener{
//            val dao = DatabaseHelper.getInstance(this).appointmentDao()

            var dateMillis = binding.appointmentCalendar.date
            var selectedTimeId = radioGroup.checkedRadioButtonId
            var time = findViewById<RadioButton>(selectedTimeId).text.toString()
            val date = Instant.ofEpochMilli(dateMillis)
                .atZone(ZoneId.systemDefault())
                .toLocalDate()

            if (date != null && time.isNotEmpty()) {

                val appointment = Appointment(0, doctor!!.id, patient!!.id, time, date, BigDecimal(binding.appointmentPrice.text.toString().replace(Regex("[^0-9]"), "")))
//                dao.insert(appointment)
                val scope = CoroutineScope(Dispatchers.IO)
                scope.launch {
                    AppointmentService(RetrofitHelper().appointmentClient()).createAppointment(
                        appointment
                    )
                    Log.i("NewAppointment", appointment.toString())

                    finish()
                }
            } else {
                Notification.notification(this, "Selecione a data e o horário!")
            }
        }

        setContentView(binding.root)
    }

    private fun callApi(
        context: Context,
        doctor: Doctor?,
        selectedDate: LocalDate,
        binding: ScheduleAppointmentFormActivityBinding,
        bindingRadioButtonLayout: RadioButtonItemBinding,
        radioGroup: RadioGroup
    ) {
        var doctorSchedules = ArrayList<DoctorSchedule>();
        var empty = binding.emptyPeriods

        val scope = CoroutineScope(Dispatchers.IO)
        scope.launch {
            doctorSchedules.addAll(
                DoctorService(RetrofitHelper().doctorClient())
                    .getScheduleByDoctorById(doctor!!.id, selectedDate)
            )

            launch(Dispatchers.Main) {
                if (doctorSchedules.isNullOrEmpty()) {
                    radioGroup.removeAllViews()
                    empty.visibility = View.VISIBLE
                }

                if (doctorSchedules.isNotEmpty()) {
                    binding.appointmentPrice.text = "R$ ${doctorSchedules.get(0).queryValue}"
                    empty.visibility = View.INVISIBLE
                }

                doctorSchedules.forEachIndexed { index, item ->
                    radioGroup.removeAllViews()
                    ScheduleRadioButtonAdapter(context, item).getView(radioGroup)
                }
            }
        }
    }

    private fun calendarConfiguration(context: Context, calendarView: CalendarView, doctor: Doctor?, binding: ScheduleAppointmentFormActivityBinding,
                                      bindingRadioButtonLayout: RadioButtonItemBinding,
                                      radioGroup: RadioGroup) {
        calendarView.minDate = System.currentTimeMillis().plus(86400000)
        // TODO: desabilitar os domingos e sábados

        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            try {
                val calender = Calendar.getInstance()
                var month2 = month + 1
                calender.set(year, month, dayOfMonth)
                calendarView.setDate(calender.timeInMillis, true, true)

                callApi(
                    context,
                    doctor,
                    LocalDate.of(year, month2, dayOfMonth),
                    binding,
                    bindingRadioButtonLayout,
                    radioGroup
                )
            } catch (exception: Exception) {
                Log.e("Calendario", "Erro calendario" + exception.message)
            }

        }
    }

    private fun toLocalDate(milliseconds: Long): LocalDate {
        return Instant
            .ofEpochMilli(milliseconds)
            .atZone(ZoneId.systemDefault())
            .toLocalDate();
    }
}