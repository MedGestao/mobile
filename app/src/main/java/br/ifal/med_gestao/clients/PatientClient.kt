package br.ifal.med_gestao.clients

import br.ifal.med_gestao.dto.PatientAuthDTO
import br.ifal.med_gestao.dto.PatientAuthDTOResponse
import br.ifal.med_gestao.dto.PatientDTO
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface PatientClient {

    @POST(value = "/api/patients")
    fun createPatient(@Body patientDTO: PatientDTO) : Call<PatientDTO>

    @POST(value = "/api/patients/login")
    fun login(@Body patientDTO: PatientAuthDTO) : Call<PatientAuthDTOResponse>
}

