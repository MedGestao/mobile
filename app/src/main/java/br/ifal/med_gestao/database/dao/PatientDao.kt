package br.ifal.med_gestao.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import br.ifal.med_gestao.domain.Patient

@Dao
interface PatientDao {
    @Insert
    fun insertPatient(patient: Patient)

    @Query("SELECT * FROM patient WHERE email = :email")
    fun validatePatient(email: String): Patient
}