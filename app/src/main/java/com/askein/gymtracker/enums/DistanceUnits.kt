package com.askein.gymtracker.enums

import androidx.annotation.StringRes
import com.askein.gymtracker.R
import kotlin.math.round

enum class DistanceUnits(@StringRes val displayName: Int, @StringRes val shortForm: Int, val kilometerConversion: Float) {
    METERS(R.string.meters_display_name, R.string.meters_short_form, 1000f),
    KILOMETERS(R.string.kilometers_display_name, R.string.kilometers_short_form, 1f),
    MILES(R.string.miles_display_name, R.string.miles_short_form, 0.62137f)
}

fun convertToKilometers(unit: DistanceUnits, weight: Double): Double = round((weight / unit.kilometerConversion) * 100) / 100

fun convertToDistanceUnit(unit: DistanceUnits, weight: Double): Double = round((weight * unit.kilometerConversion) * 100) / 100

fun getDistanceUnitFromShortForm(shortForm: Int): DistanceUnits {
    return DistanceUnits.values().first { it.shortForm == shortForm }
}
