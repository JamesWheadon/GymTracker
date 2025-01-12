package com.askein.gymtracker.enums

import androidx.annotation.StringRes
import com.askein.gymtracker.R
import kotlin.math.round

enum class WeightUnits(@StringRes val displayName: Int, @StringRes val shortForm: Int, val kiloConversion: Float) {
    KILOGRAMS(R.string.kilograms_display_name, R.string.kilograms_short_form, 1f),
    POUNDS(R.string.pounds_display_name, R.string.pounds_short_form, 2.20462f)
}

fun convertToKilograms(unit: WeightUnits, weight: Double): Double = round((weight / unit.kiloConversion) * 100) / 100

fun convertToWeightUnit(unit: WeightUnits, weight: Double): Double = round((weight * unit.kiloConversion) * 10) / 10

fun getWeightUnitFromShortForm(shortForm: Int): WeightUnits {
    return WeightUnits.values().first { it.shortForm == shortForm }
}
