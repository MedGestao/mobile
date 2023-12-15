package br.ifal.med_gestao.service

import android.util.Log
import br.ifal.med_gestao.clients.PatientClient
import br.ifal.med_gestao.domain.Patient
import br.ifal.med_gestao.dto.PatientAuthDTO
import br.ifal.med_gestao.dto.PatientDTO
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class PatientService (
    private val patientClient : PatientClient){

    fun login(patient: Patient): Patient? {
        Log.i("test", "Login" + patient.email)
        var patientDTO = patientClient
            .login(PatientAuthDTO(patient.email, patient.password))
            .execute()
            .body()?.let {
                    patientDTO -> patientDTO
            }

        return Patient(patientDTO?.id!!.toLong())
    }

    fun createPatient(patient: Patient): Patient? {
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        val localDate = LocalDate.parse(patient.birthDate, formatter)
        val time = LocalTime.of(0, 0, 0)
        val localDateTime = LocalDateTime.of(localDate, time)

        return patientClient
            .createPatient(PatientDTO.of(patient, localDateTime))
            .execute()
            .body()?.let {
                    patientDTO -> patientDTO.convertToPatient
            }
    }
}