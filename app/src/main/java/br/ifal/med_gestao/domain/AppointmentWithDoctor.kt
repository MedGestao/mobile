package br.ifal.med_gestao.domain

import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Relation
import kotlinx.parcelize.Parcelize

@Parcelize
data class AppointmentWithDoctor(
    @Embedded val doctor: Doctor,
    @Relation(
        parentColumn = "id",
        entityColumn = "doctorId"
    )
    val appointment: Appointment
) : Parcelable