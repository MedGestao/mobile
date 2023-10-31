package br.ifal.med_gestao.domain

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity
class Doctor(
    @PrimaryKey(autoGenerate = true) var id : Long = 0L,
    var image: String,
    var name: String,
    var specialty: String,
) : Parcelable