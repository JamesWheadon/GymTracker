package com.example.gymtracker.converters

import kotlin.math.round

enum class DistanceUnits(val shortForm: String, val kilometerConversion: Float) {
    METERS("m", 0.001f),
    KILOMETERS("km", 1f),
    MILES("mi", 0.62137f)
}

fun convertToKilometers(unit: DistanceUnits, weight: Double): Double = round((weight / unit.kilometerConversion) * 100) / 100

fun getDistanceUnitFromShortForm(shortForm: String): DistanceUnits {
    return DistanceUnits.values().first { it.shortForm == shortForm }
}
