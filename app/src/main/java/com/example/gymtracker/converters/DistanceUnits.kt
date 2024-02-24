package com.example.gymtracker.converters

import kotlin.math.round

enum class DistanceUnits(val displayName: String, val shortForm: String, val kilometerConversion: Float) {
    METERS("Meters", "m", 0.001f),
    KILOMETERS("Kilometers", "km", 1f),
    MILES("Miles", "mi", 0.62137f)
}

fun convertToKilometers(unit: DistanceUnits, weight: Double): Double = round((weight / unit.kilometerConversion) * 100) / 100

fun getDistanceUnitFromShortForm(shortForm: String): DistanceUnits {
    return DistanceUnits.values().first { it.shortForm == shortForm }
}
