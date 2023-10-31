package br.ifal.med_gestao.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import br.ifal.med_gestao.database.dao.DoctorDao
import br.ifal.med_gestao.domain.Doctor

@Database(entities = [Doctor::class], version = 1)
abstract class DatabaseHelper : RoomDatabase() {

    // Declarar os Daos
    abstract fun doctorDao(): DoctorDao

    companion object {
        fun getInstance(context: Context): DatabaseHelper {

            return Room.databaseBuilder(context, DatabaseHelper::class.java, "medgestao.db")
                .allowMainThreadQueries()
                .build()
        }
    }

}