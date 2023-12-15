package br.ifal.med_gestao.dto

import br.ifal.med_gestao.domain.Appointment
import br.ifal.med_gestao.domain.AppointmentWithDoctor
import br.ifal.med_gestao.domain.Doctor
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

data class AppointmentDTO(
    var doctorId: Long,
    var patientId: Long,
    var appointmentTime: String,
    var appointmentDate: String,
    var value: Float
) {
    companion object {
        fun of(appointment : Appointment, localDateTime : LocalDateTime) = AppointmentDTO(
            doctorId = appointment.doctorId,
            patientId = appointment.patientId,
            appointmentTime = appointment.time,
            appointmentDate = localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")),
            value = appointment.price.toFloat())
    }
}

class AppointmentSimpleDTO(
    var name : String,
    var specialty: String,
    var imageUrl: String,
    var patientDoctorConsultationResponse : AppointmentDTO2
) {
    val convertToAppointment : AppointmentWithDoctor
        get() = AppointmentWithDoctor(
        doctor = Doctor(name, imageUrl, specialty),
            appointment = Appointment(doctorId = patientDoctorConsultationResponse.doctorId,
                patientId= patientDoctorConsultationResponse.patientId,
                time = patientDoctorConsultationResponse.appointmentTime,
                date = LocalDate.parse(patientDoctorConsultationResponse.appointmentDate),
                price = patientDoctorConsultationResponse.value.toBigDecimal()))
}

data class AppointmentDTO2(
    var doctorId: Long,
    var patientId:Long,
    var appointmentTime: String,
    var appointmentDate: String,
    var value: Float,
    var status : String
) {
    companion object {
        fun of(appointment : Appointment, localDateTime : LocalDateTime) = AppointmentDTO(
            doctorId = appointment.doctorId,
            patientId = appointment.patientId,
            appointmentTime = appointment.time,
            appointmentDate = localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")),
            value = appointment.price.toFloat())
    }
}
