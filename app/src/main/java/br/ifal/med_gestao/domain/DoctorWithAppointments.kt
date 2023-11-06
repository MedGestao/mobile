package br.ifal.med_gestao.domain

import androidx.room.Embedded
import androidx.room.Relation

data class DoctorWithAppointments(
    @Embedded val doctor: Doctor,
    @Relation(
        parentColumn = "id",
        entityColumn = "doctorId"
    )
    val appointments: List<Appointment>
)