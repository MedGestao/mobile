package br.ifal.med_gestao.domain

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.math.BigDecimal
import java.time.LocalDate

@Parcelize
@Entity
data class Appointment(
    @PrimaryKey(autoGenerate = true) var id : Long = 0L,
    var doctorId: Long,
//    var userId: Long,
    var time: String,
    var date: LocalDate,
    var price: BigDecimal,
) : Parcelable