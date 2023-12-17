package br.ifal.med_gestao.service

import android.util.Log
import br.ifal.med_gestao.clients.AppointmentClient
import br.ifal.med_gestao.domain.Appointment
import br.ifal.med_gestao.domain.AppointmentWithDoctor
import br.ifal.med_gestao.dto.AppointmentDTO
import br.ifal.med_gestao.dto.AppointmentSimpleDTO
import retrofit2.Response
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class AppointmentService (
    private val appointmentClient : AppointmentClient){

    fun createAppointment(appointment: Appointment): Response<Void> {
        Log.i("test", "Criando agendamento")

        val time = LocalTime.of(0, 0, 0)
        val localDateTime = LocalDateTime.of(appointment.date, time)

        return appointmentClient
            .createAppointment(AppointmentDTO.of(appointment, localDateTime))
            .execute();
    }

    fun getAppointmentsByPatientID(id: Long): List<AppointmentWithDoctor> {
        var appointments = appointmentClient
            .getAppointmentsByPatientID(id)
            .execute()
            .body()?.let {
                it.map {
                        appointment -> appointment.convertToAppointment
                }
            }
        return if (appointments.isNullOrEmpty()) emptyList() else appointments
    }

}