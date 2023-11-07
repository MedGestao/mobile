package br.ifal.med_gestao.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import br.ifal.med_gestao.database.dao.AppointmentDao
import br.ifal.med_gestao.database.dao.DoctorDao
import br.ifal.med_gestao.database.dao.PatientDao
import br.ifal.med_gestao.database.migration.MIGRATION_1_2
import br.ifal.med_gestao.database.migration.MIGRATION_2_3
import br.ifal.med_gestao.domain.Appointment
import br.ifal.med_gestao.domain.Doctor
import br.ifal.med_gestao.domain.Patient
import br.ifal.med_gestao.util.Converters

@Database(
    entities = [
        Doctor::class,
        Appointment::class,
        Patient::class
    ],
    version = 3,
    exportSchema = true
)

@TypeConverters(Converters::class)
abstract class DatabaseHelper : RoomDatabase() {

    // Declarar os Daos
    abstract fun doctorDao(): DoctorDao

    abstract fun appointmentDao(): AppointmentDao

    abstract fun patientDao(): PatientDao

    companion object {
        fun getInstance(context: Context): DatabaseHelper {

            return Room.databaseBuilder(context, DatabaseHelper::class.java, "medgestao.db")
                .allowMainThreadQueries()
                .addMigrations(MIGRATION_1_2)
                .addMigrations(MIGRATION_2_3)
                .build()
        }
    }

}