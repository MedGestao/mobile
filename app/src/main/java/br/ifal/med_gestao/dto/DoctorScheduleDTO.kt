package br.ifal.med_gestao.dto

import br.ifal.med_gestao.domain.DoctorSchedule
import com.squareup.moshi.JsonClass
import java.math.BigDecimal

@JsonClass(generateAdapter = false)
class DoctorScheduleDTO(
    var id: Long,
    var period1: String,
    var period2: String,
    var dayOfService: String,
    var queryValue: BigDecimal
) {
    val convertToDoctorSchedule : DoctorSchedule get() = DoctorSchedule(
        id = id,
        period1 = period1,
        period2 = period2,
        dayOfService = dayOfService,
        queryValue = queryValue)
}