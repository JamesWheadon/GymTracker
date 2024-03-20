package com.askein.gymtracker.data.user

import kotlinx.coroutines.flow.Flow

interface UserPreferencesRepository {
    suspend fun saveDefaultWeightUnit(defaultWeightUnit: Int)
    suspend fun saveDefaultDistanceUnit(defaultDistanceUnit: Int)
    suspend fun saveDisplayHighestWeight(displayHighestWeight: Boolean)
    suspend fun saveDisplayShortestTime(displayShortestTime: Boolean)
    val isDefaultWeightUnit: Flow<Int>
    val isDefaultDistanceUnit: Flow<Int>
    val isDisplayHighestWeight: Flow<Boolean>
    val isDisplayShortestTime: Flow<Boolean>
}
