package br.ifal.med_gestao.service

import android.util.Log
import br.ifal.med_gestao.clients.DoctorClient
import br.ifal.med_gestao.domain.Doctor
import br.ifal.med_gestao.domain.DoctorSchedule
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class DoctorService (
    private val doctorClient : DoctorClient){

    fun findDoctorById(id : Long) : Doctor? {
        Log.i("test", "Iniciando requisição")
        return doctorClient
            .findDoctorByID(id)
            .execute()
            .body()?.let {
                it.convertToDoctor
            }
    }

    fun getAll(doctorName : String, specialtyName : String) : List<Doctor> {
        Log.i("test", "Iniciando requisição")
        val doctors = doctorClient
            .getAll(doctorName, specialtyName)
            .execute()
            .body()?.let {
                it.map {
                    doctorDTO -> doctorDTO.convertToDoctor
                }
            }

        Log.i("test", "Finalizando requisição ${doctors.toString()}")

        return if(doctors.isNullOrEmpty()) emptyList() else doctors
    }

    fun getScheduleByDoctorById(id : Long, selectedDate : LocalDate) : List<DoctorSchedule> {
        Log.i("test", "Iniciando requisição")
        val selectedDay = selectedDate.dayOfWeek.value
        val adjustedDayOfWeek = if (selectedDay == 7) 1 else selectedDay + 1

        val schedules = doctorClient
            .getScheduleByDoctorById(id, selectedDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")), adjustedDayOfWeek)
            .execute()
            .body()?.let {
                it.map {
                        scheduleDTO -> scheduleDTO.convertToDoctorSchedule
                }
            }

        Log.i("test", "Finalizando requisição ${schedules.toString()}")

        return if(schedules.isNullOrEmpty()) emptyList() else schedules
    }



}