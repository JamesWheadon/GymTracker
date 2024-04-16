package com.askein.gymtracker.util

import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import org.junit.experimental.runners.Enclosed
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import java.time.LocalDate

@RunWith(Enclosed::class)
class LocalDateUtilKtTest {

    @RunWith(value = Parameterized::class)
    class ConvertLocalDateToStringTest(
        private val date: LocalDate?,
        private val expected: String
    ) {
        @Test
        fun testDateConversionToString() {
            val result = convertLocalDateToString(date)

            assertThat(result, equalTo(expected))
        }

        companion object {
            @JvmStatic
            @Parameterized.Parameters(name = "{index}: isValid({0})={1}")
            fun data(): List<Array<Any?>> {
                return listOf(
                    arrayOf(LocalDate.of(2024, 1, 1), "20240101"),
                    arrayOf(LocalDate.of(2023, 12, 25), "20231225"),
                    arrayOf(LocalDate.of(2022, 7, 30), "20220730"),
                    arrayOf(LocalDate.of(2021, 5, 13), "20210513"),
                    arrayOf(null, "00000000")
                )
            }
        }
    }

    @RunWith(value = Parameterized::class)
    class ConvertStringToLocalDateTest(
        private val dateString: String,
        private val expected: LocalDate?
    ) {
        @Test
        fun testStringConversionToDate() {
            val result = convertStringToLocalDate(dateString)

            assertThat(result, equalTo(expected))
        }

        companion object {
            @JvmStatic
            @Parameterized.Parameters(name = "{index}: isValid({0})={1}")
            fun data(): List<Array<Any?>> {
                return listOf(
                    arrayOf("20240101", LocalDate.of(2024, 1, 1)),
                    arrayOf("20231225", LocalDate.of(2023, 12, 25)),
                    arrayOf("20220730", LocalDate.of(2022, 7, 30)),
                    arrayOf("20210513", LocalDate.of(2021, 5, 13)),
                    arrayOf("00000000", null)
                )
            }
        }
    }
}
