package com.askein.gymtracker.ui.user

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.askein.gymtracker.data.user.UserPreferencesRepository
import com.askein.gymtracker.enums.getDistanceUnitFromShortForm
import com.askein.gymtracker.enums.getWeightUnitFromShortForm
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class UserPreferencesViewModel(
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }

    private val isDefaultDistanceUnit = userPreferencesRepository.isDefaultDistanceUnit
    private val isDefaultWeightUnit = userPreferencesRepository.isDefaultWeightUnit
    private val isDisplayHighestWeight = userPreferencesRepository.isDisplayHighestWeight
    private val isDisplayShortestTime = userPreferencesRepository.isDisplayShortestTime

    val uiState = combine(
        isDefaultDistanceUnit,
        isDefaultWeightUnit,
        isDisplayHighestWeight,
        isDisplayShortestTime
    )
    { defaultDistanceUnit, defaultWeightUnit, displayHighestWeight, displayShortestTime ->
        UserPreferencesUiState(
            defaultDistanceUnit = getDistanceUnitFromShortForm(defaultDistanceUnit),
            defaultWeightUnit = getWeightUnitFromShortForm(defaultWeightUnit),
            displayHighestWeight = displayHighestWeight,
            displayShortestTime = displayShortestTime
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
        initialValue = UserPreferencesUiState()
    )

    fun updateDefaultDistanceUnit(distanceUnit: Int) {
        viewModelScope.launch {
            userPreferencesRepository.saveDefaultDistanceUnit(distanceUnit)
        }
    }

    fun updateDefaultWeightUnit(weightUnit: Int) {
        viewModelScope.launch {
            userPreferencesRepository.saveDefaultWeightUnit(weightUnit)
        }
    }

    fun updateDisplayHighestWeight(displayHighestWeight: Boolean) {
        viewModelScope.launch {
            userPreferencesRepository.saveDisplayHighestWeight(displayHighestWeight)
        }
    }

    fun updateDisplayShortestTime(displayShortestTime: Boolean) {
        viewModelScope.launch {
            userPreferencesRepository.saveDisplayShortestTime(displayShortestTime)
        }
    }
}
