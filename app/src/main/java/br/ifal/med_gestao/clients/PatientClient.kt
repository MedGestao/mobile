package br.ifal.med_gestao.clients

import br.ifal.med_gestao.dto.DoctorDTO
import br.ifal.med_gestao.dto.PatientAuthDTO
import br.ifal.med_gestao.dto.PatientAuthDTOResponse
import br.ifal.med_gestao.dto.PatientDTO
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path

interface PatientClient {

    @POST(value = "/api/patients")
    fun createPatient(@Body patientDTO: PatientDTO) : Call<PatientDTO>

    @POST(value = "/api/patients/login")
    fun login(@Body patientDTO: PatientAuthDTO) : Call<PatientAuthDTOResponse>

    @GET(value = "/api/patients/{id}")
    fun findPatientByID(@Path("id") id: Long) : Call<PatientDTO>

    @PUT(value = "/api/patients/{id}")
    fun editPatient(@Path("id") id: Long, @Body patientDTO: PatientDTO) : Call<PatientDTO>

//    @PUT(value = "/api/patients/{id}")
//    fun editPatient(@Path ("id") id: Long, @Body patientDTO: PatientDTO) : Call<PatientDTO>
}

