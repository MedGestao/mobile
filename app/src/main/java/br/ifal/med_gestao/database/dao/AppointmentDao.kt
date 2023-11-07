package br.ifal.med_gestao.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import br.ifal.med_gestao.domain.Appointment

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
}