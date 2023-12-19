package br.ifal.med_gestao.clients

import com.squareup.moshi.FromJson
import com.squareup.moshi.Moshi
import com.squareup.moshi.ToJson
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.math.BigDecimal

class RetrofitHelper {

    private fun moshi() = Moshi.Builder()
        .add(BigDecimalAdapter)
        .build()

    private fun buildRetrofit() = Retrofit.Builder()
        .baseUrl(Companion.BASE_URL)
        .addConverterFactory(MoshiConverterFactory.create(moshi()))
        .build();

    fun doctorClient(): DoctorClient = buildRetrofit().create(DoctorClient::class.java)
    fun appointmentClient(): AppointmentClient = buildRetrofit().create(AppointmentClient::class.java)
    fun patientClient(): PatientClient = buildRetrofit().create(PatientClient::class.java)

    companion object {
        const val BASE_URL = "http://10.0.0.121:3001"
    }
}

object BigDecimalAdapter {
    @FromJson
    fun fromJson(string: String) = BigDecimal(string)

    @ToJson
    fun toJson(value: BigDecimal) = value.toString()
}

