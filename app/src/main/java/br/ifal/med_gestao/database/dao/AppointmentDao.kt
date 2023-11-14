package br.ifal.med_gestao.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import br.ifal.med_gestao.domain.Appointment
import br.ifal.med_gestao.domain.AppointmentWithDoctor

@Dao
interface AppointmentDao {
    @Query("SELECT * FROM Appointment LIMIT 6")
    fun findAll() :List<Appointment>
    @Query("SELECT * FROM Appointment WHERE id = :id")
    fun findByID(id : Long) : Appointment
    @Insert
    fun insert(appointment : Appointment)
    @Insert
    fun insertAll(appointments : List<Appointment>)
    @Delete
    fun delete(appointment : Appointment)

    @Transaction
    @Query("SELECT * FROM Appointment a INNER JOIN Doctor d ON d.id = a.doctorId WHERE patientId = :patientId")
    fun getAppointmentsByPatientId(patientId : Long): List<AppointmentWithDoctor>
}