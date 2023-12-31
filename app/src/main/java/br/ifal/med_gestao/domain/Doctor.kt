package br.ifal.med_gestao.domain

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.math.BigDecimal

@Parcelize
@Entity
class Doctor(
    @PrimaryKey(autoGenerate = true) var id : Long = 0L,
    var image: String,
    var name: String,
    var specialty: String,
    var crm: String,
    var price: BigDecimal = BigDecimal.ZERO
) : Parcelable{
    constructor(name: String, imageURL: String, specialty: String): this(
        0L,
        imageURL,
        name,
        specialty,
        "",
        BigDecimal.ZERO
    )
}