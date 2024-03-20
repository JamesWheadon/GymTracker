package com.askein.gymtracker.fake

import com.askein.gymtracker.R
import com.askein.gymtracker.data.user.UserPreferencesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeUserPreferencesRepository: UserPreferencesRepository {
    override suspend fun saveDefaultWeightUnit(defaultWeightUnit: Int) {
    }

    override suspend fun saveDefaultDistanceUnit(defaultDistanceUnit: Int) {
    }

    override suspend fun saveDisplayHighestWeight(displayHighestWeight: Boolean) {
    }

    override suspend fun saveDisplayShortestTime(displayShortestTime: Boolean) {
    }

    override val isDefaultWeightUnit: Flow<Int> = flowOf(R.string.pounds_short_form)
    override val isDefaultDistanceUnit: Flow<Int> = flowOf(R.string.meters_short_form)
    override val isDisplayHighestWeight: Flow<Boolean> = flowOf(false)
    override val isDisplayShortestTime: Flow<Boolean> = flowOf(false)
}