package br.ifal.med_gestao.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import br.ifal.med_gestao.domain.Doctor

@Dao
interface DoctorDao {
    @Query("SELECT * FROM Doctor LIMIT 6")
    fun findAll() :List<Doctor>
    @Query("SELECT * FROM Doctor WHERE id = :id")
    fun findByID(id : Long) : Doctor
    @Query("SELECT * FROM Doctor WHERE name LIKE '%' || :text || '%'")
    fun findByName(text : String?) : List<Doctor>
    @Insert
    fun insert(doctor : Doctor)
    @Insert
    fun insertAll(doctors : List<Doctor>)
    @Delete
    fun delete(doctor : Doctor)
    @Query("DELETE FROM Doctor")
    fun deleteAll()

}