package br.ifal.med_gestao.domain

import java.math.BigDecimal

class DoctorSchedule(
    var id: Long,
    var period1: String,
    var period2: String,
    var dayOfService: String,
    var queryValue: BigDecimal
)