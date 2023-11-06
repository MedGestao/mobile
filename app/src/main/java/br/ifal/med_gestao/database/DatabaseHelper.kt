package br.ifal.med_gestao.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import br.ifal.med_gestao.database.dao.DoctorDao
import br.ifal.med_gestao.database.dao.PatientDao
import br.ifal.med_gestao.database.migration.MIGRATION_1_2
import br.ifal.med_gestao.domain.Doctor
import br.ifal.med_gestao.domain.Patient

@Database(
    entities = [
        Doctor::class,
        Patient::class
               ],
    version = 2,
    exportSchema = true
)
abstract class DatabaseHelper : RoomDatabase() {

    // Declarar os Daos
    abstract fun doctorDao(): DoctorDao

    abstract fun patientDao(): PatientDao

    companion object {
        fun getInstance(context: Context): DatabaseHelper {

            return Room.databaseBuilder(context, DatabaseHelper::class.java, "medgestao.db")
                .allowMainThreadQueries()
                .addMigrations(MIGRATION_1_2)
                .build()
        }
    }

}