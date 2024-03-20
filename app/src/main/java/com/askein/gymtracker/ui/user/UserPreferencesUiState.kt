package com.askein.gymtracker.ui.user

import androidx.compose.runtime.compositionLocalOf
import com.askein.gymtracker.enums.DistanceUnits
import com.askein.gymtracker.enums.WeightUnits

data class UserPreferencesUiState(
    val defaultDistanceUnit: DistanceUnits = DistanceUnits.KILOMETERS,
    val defaultWeightUnit: WeightUnits = WeightUnits.KILOGRAMS,
    val displayHighestWeight: Boolean = true,
    val displayShortestTime: Boolean = false,
)

val LocalUserPreferences = compositionLocalOf<UserPreferencesUiState> { error("No UiState provided") }
