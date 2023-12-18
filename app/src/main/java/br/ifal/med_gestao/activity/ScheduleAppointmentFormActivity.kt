package br.ifal.med_gestao.activity

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.CalendarView
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import br.ifal.med_gestao.R
import br.ifal.med_gestao.adapters.ScheduleRadioButtonAdapter
import br.ifal.med_gestao.clients.RetrofitHelper
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
import java.util.Calendar


class ScheduleAppointmentFormActivity : AppCompatActivity() {

    private var appointmentService = AppointmentService(RetrofitHelper().appointmentClient())
    private var doctorService = DoctorService(RetrofitHelper().doctorClient())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        supportActionBar?.setCustomView(R.layout.custom_toolbar_title);

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
        var shared = getSharedPreferences("SHARED_LOGIN", Context.MODE_PRIVATE)
        var patient_id = shared.getString("PATIENT_ID", null)

        val binding = ScheduleAppointmentFormActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)


        // Preencher dados do médico
        Glide.with(this)
            .load(doctor?.image)
            .into(binding.appointmentDoctorImg)
        binding.appointmentDoctorName.text = doctor?.name
        binding.appointmentDoctorSpecialty.text = doctor?.specialty

        // Buscar períodos disponíveis pela data selecionada no calendário
        val calendarView = binding.appointmentCalendar
        calendarView.minDate = System.currentTimeMillis().plus(86400000)
        val selectedDate = toLocalDate(calendarView.date)

        var radioGroup = binding.appointmentRadioGroup
        var emptyPeriodsMessage = binding.emptyPeriods

        var appointmentPriceTextView = binding.appointmentPrice

        updateRadioGroupWithAvailablePeriods(this, doctor, selectedDate, emptyPeriodsMessage, appointmentPriceTextView, radioGroup)
        addCalendarListener(this, calendarView, doctor, emptyPeriodsMessage, appointmentPriceTextView, radioGroup)

        val button = binding.appointmentButton
        button.setOnClickListener{

            var selectedPeriodId = radioGroup.checkedRadioButtonId

            if (selectedPeriodId != -1) {
                var period = findViewById<RadioButton>(selectedPeriodId)?.text.toString()

                val appointment = Appointment(0, doctor!!.id, patient_id!!.toLong(), period, selectedDate,
                    BigDecimal(binding.appointmentPrice.text.toString().replace(Regex("[^0-9]"), "")))

                val scope = CoroutineScope(Dispatchers.IO)
                scope.launch {
                    appointmentService.createAppointment(appointment)

                    launch(Dispatchers.Main) {
                        Notification.notification(this@ScheduleAppointmentFormActivity, "Consulta agendada com sucesso!")

                        val intent = Intent(this@ScheduleAppointmentFormActivity, ListDoctorsActivity::class.java)
                        startActivity(intent)
                    }
                }
            } else {
                Notification.notification(this, "Selecione a data e o horário!")
            }
        }

    }

    private fun updateRadioGroupWithAvailablePeriods(
        context: Context,
        doctor: Doctor?,
        selectedDate: LocalDate,
        emptyPeriodsMessageTextView: TextView,
        appointmentPriceTextView: TextView,
        radioGroup: RadioGroup
    ) {
        CoroutineScope(Dispatchers.IO).launch {

            var doctorSchedules = ArrayList<DoctorSchedule>();
            doctorSchedules.addAll(doctorService.getScheduleByDoctorById(doctor!!.id, selectedDate))

            launch(Dispatchers.Main) {

                if (doctorSchedules.isNotEmpty()) {
                    appointmentPriceTextView.text = "R$ ${doctorSchedules.get(0).queryValue}"
                    emptyPeriodsMessageTextView.visibility = View.INVISIBLE

                    doctorSchedules.forEach { item ->
                        radioGroup.removeAllViews()
                        ScheduleRadioButtonAdapter(context, item).getView(radioGroup)
                    }
                } else {
                    radioGroup.removeAllViews()
                    emptyPeriodsMessageTextView.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun addCalendarListener(context: Context,
                                    calendarView: CalendarView,
                                    doctor: Doctor?,
                                    emptyPeriodsMessageTextView: TextView,
                                    appointmentPriceTextView: TextView,
                                    radioGroup: RadioGroup) {

        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            try {
                val calender = Calendar.getInstance()
                var month2 = month + 1
                calender.set(year, month, dayOfMonth)
                calendarView.setDate(calender.timeInMillis, true, true)

                updateRadioGroupWithAvailablePeriods(
                    context,
                    doctor,
                    LocalDate.of(year, month2, dayOfMonth),
                    emptyPeriodsMessageTextView,
                    appointmentPriceTextView,
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