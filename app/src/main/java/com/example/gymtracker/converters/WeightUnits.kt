package com.example.gymtracker.converters

import kotlin.math.round

enum class WeightUnits(val shortForm: String, val kiloConversion: Float) {
    KILOGRAMS("kg", 1f),
    POUNDS("lb", 2.20462f)
}

fun convertToKilograms(unit: WeightUnits, weight: Double): Double = round((weight / unit.kiloConversion) * 100) / 100

fun getWeightUnitFromShortForm(shortForm: String): WeightUnits {
    return WeightUnits.values().first { it.shortForm == shortForm }
}
