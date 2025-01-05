package com.askein.gymtracker.enums

import com.askein.gymtracker.enums.WeightUnits.KILOGRAMS
import com.askein.gymtracker.enums.WeightUnits.POUNDS
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import org.junit.experimental.runners.Enclosed
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import org.junit.runners.Parameterized.Parameters

@RunWith(Enclosed::class)
class WeightUnitsKtTest {
    @RunWith(value = Parameterized::class)
    class ConvertToKilogramsTest(
        private val unit: WeightUnits,
        private val weight: Double,
        private val expected: Double
    ) {
        @Test
        fun testUnitToKilogramConversion() {
            val result = convertToKilograms(unit, weight)

            assertThat(result, equalTo(expected))
        }

        companion object {
            @JvmStatic
            @Parameters(name = "Correct conversion of {1} {0} to {2} kg")
            fun data(): List<Array<Any?>> {
                return listOf(
                    arrayOf(KILOGRAMS, 4, 4.00),
                    arrayOf(KILOGRAMS, 46.7, 46.70),
                    arrayOf(POUNDS, 10, 4.54),
                    arrayOf(POUNDS, 15.8, 7.17)
                )
            }
        }
    }

    @RunWith(value = Parameterized::class)
    class ConvertToKilogramsAndBackTest(
        private val unit: WeightUnits,
        private val weight: Double
    ) {
        @Test
        fun testUnitToKilogramAndBackConversion() {
            val result = convertToWeightUnit(unit, convertToKilograms(unit, weight))

            assertThat(result, equalTo(weight))
        }

        companion object {
            @JvmStatic
            @Parameters(name = "Correct conversion of {1} {0} to kg and back")
            fun data(): List<Array<Any?>> {
                return listOf(
                    arrayOf(KILOGRAMS, 4),
                    arrayOf(KILOGRAMS, 46.7),
                    arrayOf(POUNDS, 10),
                    arrayOf(POUNDS, 15.8),
                    arrayOf(POUNDS, 135.0),
                    arrayOf(POUNDS, 1575.6),
                    arrayOf(POUNDS, 1575.7),
                    arrayOf(POUNDS, 1575.8),
                    arrayOf(POUNDS, 1575.9),
                    arrayOf(POUNDS, 1576.0),
                    arrayOf(POUNDS, 1576.1),
                    arrayOf(POUNDS, 1576.2),
                    arrayOf(POUNDS, 1576.3)
                )
            }
        }
    }
}
