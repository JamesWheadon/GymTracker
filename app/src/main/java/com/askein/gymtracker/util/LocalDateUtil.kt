package com.askein.gymtracker.util

import java.time.LocalDate
import java.time.format.DateTimeFormatter

fun convertLocalDateToString(date: LocalDate?): String {
    if (date == null) {
        return "00000000"
    }
    return date.format(DateTimeFormatter.BASIC_ISO_DATE)
}

fun convertStringToLocalDate(dateString: String): LocalDate? {
    return try {
        LocalDate.parse(dateString, DateTimeFormatter.BASIC_ISO_DATE)
    } catch (_: Exception) {
        null
    }
}
