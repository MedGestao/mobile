package br.ifal.med_gestao.clients

import br.ifal.med_gestao.dto.AppointmentDTO
import br.ifal.med_gestao.dto.AppointmentSimpleDTO
import br.ifal.med_gestao.dto.DoctorDTO
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface AppointmentClient {

    @POST(value = "/api/patientDoctorConsultation")
    fun createAppointment(@Body appointment: AppointmentDTO) : Call<Void>

    @GET(value = "/api/patientDoctorConsultation/searchByPatient/{id}")
    fun getAppointmentsByPatientID(@Path("id") id: Long) : Call<List<AppointmentSimpleDTO>>

}

