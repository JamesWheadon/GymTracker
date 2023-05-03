package com.example.gymtracker.converters

import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import java.time.Instant
import java.util.Calendar
import java.util.Date


class DateConverterTest {

    private val dateConverter = DateConverter()

    @Test
    fun dateFromLong() {
        val today = Calendar.getInstance().time
        val longDate = today.time

        val result = dateConverter.dateFromLong(longDate)

        assertThat(result, equalTo(today))
    }

    @Test
    fun dateToLong() {
        val now = Instant.now()
        val testDate = Date.from(now)
        val expected = testDate.time

        val result = dateConverter.dateToLong(testDate)

        assertThat(result, equalTo(expected))
    }
}
