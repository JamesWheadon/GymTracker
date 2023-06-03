package com.example.gymtracker.converters

import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import java.time.LocalDate


class LocalDateConverterTest {

    private val localDateConverter = LocalDateConverter()

    @Test
    fun dateFromLong() {
        val today = LocalDate.now()
        val days = today.toEpochDay()

        val result = localDateConverter.localDateFromLong(days)

        assertThat(result, equalTo(today))
    }

    @Test
    fun dateToLong() {
        val testDate = LocalDate.now()
        val expected = testDate.toEpochDay()

        val result = localDateConverter.localDateToLong(testDate)

        assertThat(result, equalTo(expected))
    }
}
