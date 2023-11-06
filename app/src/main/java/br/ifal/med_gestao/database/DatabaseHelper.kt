package br.ifal.med_gestao.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import br.ifal.med_gestao.database.dao.AppointmentDao
import br.ifal.med_gestao.database.dao.DoctorDao
import br.ifal.med_gestao.domain.Appointment
import br.ifal.med_gestao.domain.Doctor
import br.ifal.med_gestao.util.Converters

@Database(entities = [Doctor::class, Appointment::class], version = 1)
@TypeConverters(Converters::class)
abstract class DatabaseHelper : RoomDatabase() {

    // Declarar os Daos
    abstract fun doctorDao(): DoctorDao

    abstract fun appointmentDao(): AppointmentDao

    companion object {
        fun getInstance(context: Context): DatabaseHelper {

            return Room.databaseBuilder(context, DatabaseHelper::class.java, "medgestao.db")
                .allowMainThreadQueries()
                .build()
        }
    }

}