package br.ifal.med_gestao.clients

import br.ifal.med_gestao.dto.DoctorDTO
import br.ifal.med_gestao.dto.DoctorScheduleDTO
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface DoctorClient {

    @GET(value = "/api/doctors")
    fun getAll(@Query("doctorName") doctorName : String) : Call<List<DoctorDTO>>

    @GET(value = "/api/doctors/{id}")
    fun findDoctorByID(@Path("id") id: Long) : Call<DoctorDTO>

    @GET(value = "/api/doctors/{id}/schedule")
    fun getScheduleByDoctorById(@Path("id") id: Long,
                                @Query("selectedDate") selectedDate: String,
                                @Query("selectedDay") selectedDay: Int) : Call<List<DoctorScheduleDTO>>
}

