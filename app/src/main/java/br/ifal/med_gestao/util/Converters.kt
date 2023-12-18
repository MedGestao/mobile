package br.ifal.med_gestao.util

import androidx.room.TypeConverter
import java.math.BigDecimal
import java.time.LocalDate

class Converters {
    @TypeConverter
    fun fromTimestamp(value: String?): LocalDate? {
        return value?.let {
            LocalDate.parse(it)
        }
    }
    @TypeConverter
    fun dateToTimestamp(date: LocalDate?): String? {
        return date?.toString()
    }

    @TypeConverter
    fun bigDecimalToString(input: BigDecimal?): String {
        return input?.toPlainString() ?: ""
    }

    @TypeConverter
    fun stringToBigDecimal(input: String?): BigDecimal {
        if (input.isNullOrBlank()) return BigDecimal.valueOf(0.0)
        return input.toBigDecimalOrNull() ?: BigDecimal.valueOf(0.0)
    }

    @TypeConverter
    fun booleanToInteger(input: Boolean): Int{
        return if(input) 1
        else 0
    }

    @TypeConverter
    fun integerToBoolean(input: Int): Boolean{
        return input == 1
    }

}