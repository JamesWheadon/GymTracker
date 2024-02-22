package com.example.gymtracker.data.user

import kotlinx.coroutines.flow.Flow

interface UserPreferencesRepository {
    suspend fun saveDefaultWeightUnit(defaultWeightUnit: String)
    suspend fun saveDefaultDistanceUnit(defaultDistanceUnit: String)
    suspend fun saveDisplayHighestWeight(displayHighestWeight: Boolean)
    suspend fun saveDisplayShortestTime(displayShortestTime: Boolean)
    val isDefaultWeightUnit: Flow<String>
    val isDefaultDistanceUnit: Flow<String>
    val isDisplayHighestWeight: Flow<Boolean>
    val isDisplayShortestTime: Flow<Boolean>
}
