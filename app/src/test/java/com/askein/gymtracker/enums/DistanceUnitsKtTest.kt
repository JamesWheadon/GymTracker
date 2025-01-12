package com.askein.gymtracker.enums

import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import org.junit.experimental.runners.Enclosed
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import org.junit.runners.Parameterized.Parameters

@RunWith(Enclosed::class)
class DistanceUnitsKtTest {
    @RunWith(value = Parameterized::class)
    class ConvertToKilometersTest(
        private val unit: DistanceUnits,
        private val distance: Double,
        private val expected: Double
    ) {
        @Test
        fun testUnitToKilogramConversion() {
            val result = convertToKilometers(unit, distance)

            assertThat(result, CoreMatchers.equalTo(expected))
        }

        companion object {
            @JvmStatic
            @Parameters(name = "Correct conversion of {1} {0} to {2} kg")
            fun data(): List<Array<Any?>> {
                return listOf(
                    arrayOf(DistanceUnits.KILOMETERS, 4, 4.00),
                    arrayOf(DistanceUnits.KILOMETERS, 46.7, 46.70),
                    arrayOf(DistanceUnits.MILES, 10, 16.0935),
                    arrayOf(DistanceUnits.MILES, 15.8, 25.4277),
                    arrayOf(DistanceUnits.METERS, 16150, 16.15),
                    arrayOf(DistanceUnits.METERS, 1566.8, 1.5668)
                )
            }
        }
    }

    @RunWith(value = Parameterized::class)
    class ConvertToKilometersAndBackTest(
        private val unit: DistanceUnits,
        private val distance: Double
    ) {
        @Test
        fun testUnitToKilogramAndBackConversion() {
            val result = convertToDistanceUnit(unit, convertToKilometers(unit, distance))

            assertThat(result, CoreMatchers.equalTo(distance))
        }

        companion object {
            @JvmStatic
            @Parameters(name = "Correct conversion of {1} {0} to kg and back")
            fun data(): List<Array<Any?>> {
                return listOf(
                    arrayOf(DistanceUnits.KILOMETERS, 4),
                    arrayOf(DistanceUnits.KILOMETERS, 46.7),
                    arrayOf(DistanceUnits.MILES, 10),
                    arrayOf(DistanceUnits.MILES, 15.8),
                    arrayOf(DistanceUnits.MILES, 135.0),
                    arrayOf(DistanceUnits.MILES, 175.6),
                    arrayOf(DistanceUnits.MILES, 175.7),
                    arrayOf(DistanceUnits.MILES, 175.8),
                    arrayOf(DistanceUnits.MILES, 175.9),
                    arrayOf(DistanceUnits.MILES, 176.0),
                    arrayOf(DistanceUnits.MILES, 176.1),
                    arrayOf(DistanceUnits.MILES, 176.2),
                    arrayOf(DistanceUnits.MILES, 176.3),
                    arrayOf(DistanceUnits.METERS, 36713),
                    arrayOf(DistanceUnits.METERS, 10862),
                    arrayOf(DistanceUnits.METERS, 25921),
                    arrayOf(DistanceUnits.METERS, 753)
                )
            }
        }
    }
}
