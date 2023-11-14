package br.ifal.med_gestao.domain

import androidx.room.Embedded
import androidx.room.Relation

data class PatientWithAppointments(
    @Embedded val patient: Patient,
    @Relation(
        parentColumn = "id",
        entityColumn = "patientId"
    )
    val appointments: List<Appointment>
)