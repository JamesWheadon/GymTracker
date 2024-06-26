package com.askein.gymtracker.util

import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test

class ListConverterTest {

    private val listConverter: ListConverter = ListConverter()

    @Test
    fun doubleArrayFromString() {
        val stringList = "2.0|5.0"

        val result = listConverter.doubleArrayFromString(stringList)

        assertThat(result, equalTo(listOf(2.0, 5.0)))
    }

    @Test
    fun doubleArrayToString() {
        val doubleList = listOf(2.0, 5.0)

        val result = listConverter.doubleArrayToString(doubleList)

        assertThat(result, equalTo("2.0|5.0"))
    }

    @Test
    fun intArrayFromString() {
        val stringList = "2|5"

        val result = listConverter.intArrayFromString(stringList)

        assertThat(result, equalTo(listOf(2, 5)))
    }

    @Test
    fun intArrayToString() {
        val intList = listOf(2, 5)

        val result = listConverter.intArrayToString(intList)

        assertThat(result, equalTo("2|5"))
    }

}