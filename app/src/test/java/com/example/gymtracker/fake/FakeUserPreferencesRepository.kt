package com.example.gymtracker.fake

import com.example.gymtracker.data.user.UserPreferencesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeUserPreferencesRepository: UserPreferencesRepository {
    override suspend fun saveDefaultWeightUnit(defaultWeightUnit: String) {
        TODO("Not yet implemented")
    }

    override suspend fun saveDefaultDistanceUnit(defaultDistanceUnit: String) {
        TODO("Not yet implemented")
    }

    override suspend fun saveDisplayHighestWeight(displayHighestWeight: Boolean) {
        TODO("Not yet implemented")
    }

    override suspend fun saveDisplayShortestTime(displayShortestTime: Boolean) {
        TODO("Not yet implemented")
    }

    override val isDefaultWeightUnit: Flow<String> = flowOf("lb")
    override val isDefaultDistanceUnit: Flow<String> = flowOf("m")
    override val isDisplayHighestWeight: Flow<Boolean> = flowOf(false)
    override val isDisplayShortestTime: Flow<Boolean> = flowOf(false)
}