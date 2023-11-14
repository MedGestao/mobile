package br.ifal.med_gestao.domain

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "patient", indices = [Index(value = ["email"], unique = true)])
class Patient (
    @PrimaryKey(autoGenerate = true) var id : Long = 0L,
    var name: String,

    @ColumnInfo(name = "email")
    var email: String,
    var cpf: String,

    @ColumnInfo(name = "birth_date")
    var birthDate: String,
    var sex: String,
    var telephone: String,
    var password: String
): Parcelable{
    constructor(
        name: String,
        email: String,
        cpf: String,
        birthDate: String,
        sex: String,
        telephone: String,
        password: String
    ): this(
        0L,
        name,
        email,
        cpf,
        birthDate,
        sex,
        telephone,
        password)

    constructor(email: String, password: String): this(
        0L,
        "",
        email,
        "",
        "",
        "",
        "",
        password,
    )
}