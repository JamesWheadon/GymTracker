package com.example.gymtracker.ui.user

import com.example.gymtracker.converters.DistanceUnits
import com.example.gymtracker.converters.WeightUnits


data class UserPreferencesUiState(
    val defaultDistanceUnit: DistanceUnits = DistanceUnits.KILOMETERS,
    val defaultWeightUnit: WeightUnits = WeightUnits.KILOGRAMS,
    val displayHighestWeight: Boolean = true,
    val displayShortestTime: Boolean = true,
)
