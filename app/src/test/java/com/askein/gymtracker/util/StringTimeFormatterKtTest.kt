package com.askein.gymtracker.util

import com.askein.gymtracker.R
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test

class StringTimeFormatterKtTest {

    @Test
    fun shouldReturnCorrectFormatForATimeInSingleDigitSeconds() {
        val resourceInfo = getTimeStringResourceFromSeconds(5)

        assertThat(resourceInfo.resourceId, equalTo(R.string.display_seconds))
        assertThat(resourceInfo.arguments.size, equalTo(1))
        assertThat(resourceInfo.arguments[0], equalTo("05"))
    }

    @Test
    fun shouldReturnCorrectFormatForATimeInSeconds() {
        val resourceInfo = getTimeStringResourceFromSeconds(50)

        assertThat(resourceInfo.resourceId, equalTo(R.string.display_seconds))
        assertThat(resourceInfo.arguments.size, equalTo(1))
        assertThat(resourceInfo.arguments[0], equalTo("50"))
    }

    @Test
    fun shouldReturnCorrectFormatForATimeInSingleDigitMinutes() {
        val resourceInfo = getTimeStringResourceFromSeconds(120)

        assertThat(resourceInfo.resourceId, equalTo(R.string.display_minutes))
        assertThat(resourceInfo.arguments.size, equalTo(2))
        assertThat(resourceInfo.arguments[0], equalTo("02"))
        assertThat(resourceInfo.arguments[1], equalTo("00"))
    }

    @Test
    fun shouldReturnCorrectFormatForATimeInMinutes() {
        val resourceInfo = getTimeStringResourceFromSeconds(630)

        assertThat(resourceInfo.resourceId, equalTo(R.string.display_minutes))
        assertThat(resourceInfo.arguments.size, equalTo(2))
        assertThat(resourceInfo.arguments[0], equalTo("10"))
        assertThat(resourceInfo.arguments[1], equalTo("30"))
    }

    @Test
    fun shouldReturnCorrectFormatForATimeInHours() {
        val resourceInfo = getTimeStringResourceFromSeconds(6330)

        assertThat(resourceInfo.resourceId, equalTo(R.string.display_hours))
        assertThat(resourceInfo.arguments.size, equalTo(3))
        assertThat(resourceInfo.arguments[0], equalTo("1"))
        assertThat(resourceInfo.arguments[1], equalTo("45"))
        assertThat(resourceInfo.arguments[2], equalTo("30"))
    }
}