package br.ifal.med_gestao.activity

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.CalendarView
import android.widget.RadioButton
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import br.ifal.med_gestao.R
import br.ifal.med_gestao.database.DatabaseHelper
import br.ifal.med_gestao.databinding.ScheduleAppointmentFormActivityBinding
import br.ifal.med_gestao.domain.Appointment
import br.ifal.med_gestao.domain.Doctor
import br.ifal.med_gestao.domain.Patient
import br.ifal.med_gestao.util.Notification
import com.bumptech.glide.Glide
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Calendar


class ScheduleAppointmentFormActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        supportActionBar?.setCustomView(R.layout.custom_toolbar_title);

        val binding = ScheduleAppointmentFormActivityBinding.inflate(layoutInflater)

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

        binding.appointmentDoctorName.text = doctor?.name
        binding.appointmentDoctorSpecialty.text = doctor?.specialty
        binding.appointmentPrice.text = "R$ ${doctor?.price.toString()}"

        // Set imagem
        Glide.with(this)
            .load(doctor?.image)
            .into(binding.appointmentDoctorImg)

        val calendarView = binding.appointmentCalendar
        calendarConfiguration(calendarView)

        val button = binding.appointmentButton
        button.setOnClickListener{
            val dao = DatabaseHelper.getInstance(this).appointmentDao()

            var dateMillis = binding.appointmentCalendar.date
            var selectedTimeId = binding.appointmentRadioGroup.checkedRadioButtonId
            var time = findViewById<RadioButton>(selectedTimeId).text.toString()

            if (dateMillis != null && time != null) {
                val date = Instant.ofEpochMilli(dateMillis)
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate()

                val appointment = Appointment(0, doctor!!.id, patient!!.id, time, date, doctor.price)
                dao.insert(appointment)

                Log.i("NewAppointment", appointment.toString())

                finish()
            } else {
                Notification.notification(this, "Selecione a data e o horário!")
            }
        }

        setContentView(binding.root)
    }

    private fun calendarConfiguration(calendarView: CalendarView) {
        calendarView.minDate = System.currentTimeMillis().plus(86400000)
        // TODO: desabilitar os domingos
        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val calender = Calendar.getInstance()
            calender.set(year, month, dayOfMonth)
            calendarView.setDate(calender.timeInMillis, true, true)
        }
    }
}