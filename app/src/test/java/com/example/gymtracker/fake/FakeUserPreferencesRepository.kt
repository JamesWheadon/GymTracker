package com.example.gymtracker.fake

import com.example.gymtracker.data.user.UserPreferencesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeUserPreferencesRepository: UserPreferencesRepository {
    override suspend fun saveDefaultWeightUnit(defaultWeightUnit: String) {
    }

    override suspend fun saveDefaultDistanceUnit(defaultDistanceUnit: String) {
    }

    override suspend fun saveDisplayHighestWeight(displayHighestWeight: Boolean) {
    }

    override suspend fun saveDisplayShortestTime(displayShortestTime: Boolean) {
    }

    override val isDefaultWeightUnit: Flow<String> = flowOf("lb")
    override val isDefaultDistanceUnit: Flow<String> = flowOf("m")
    override val isDisplayHighestWeight: Flow<Boolean> = flowOf(false)
    override val isDisplayShortestTime: Flow<Boolean> = flowOf(false)
}