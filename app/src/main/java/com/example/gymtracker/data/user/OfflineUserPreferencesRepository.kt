package com.example.gymtracker.data.user

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.gymtracker.converters.DistanceUnits
import com.example.gymtracker.converters.WeightUnits
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

class OfflineUserPreferencesRepository(private val context: Context): UserPreferencesRepository {
    private companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settingPrefs")
        val DEFAULT_WEIGHT_UNIT = intPreferencesKey("default_weight_unit")
        val DEFAULT_DISTANCE_UNIT = intPreferencesKey("default_distance_unit")
        val DISPLAY_HIGHEST_WEIGHT = booleanPreferencesKey("display_highest_weight")
        val DISPLAY_SHORTEST_TIME = booleanPreferencesKey("display_shortest_time")
    }

    override suspend fun saveDefaultWeightUnit(defaultWeightUnit: Int) {
        context.dataStore.edit { preferences ->
            preferences[DEFAULT_WEIGHT_UNIT] = defaultWeightUnit
        }
    }

    override suspend fun saveDefaultDistanceUnit(defaultDistanceUnit: Int) {
        context.dataStore.edit { preferences ->
            preferences[DEFAULT_DISTANCE_UNIT] = defaultDistanceUnit
        }
    }

    override suspend fun saveDisplayHighestWeight(displayHighestWeight: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[DISPLAY_HIGHEST_WEIGHT] = displayHighestWeight
        }
    }

    override suspend fun saveDisplayShortestTime(displayShortestTime: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[DISPLAY_SHORTEST_TIME] = displayShortestTime
        }
    }

    override val isDefaultWeightUnit: Flow<Int> = context.dataStore.data.catch {
        if (it is IOException) {
            emit(emptyPreferences())
        } else {
            throw it
        }
    }.map { preferences ->
        preferences[DEFAULT_WEIGHT_UNIT] ?: WeightUnits.KILOGRAMS.shortForm
    }

    override val isDefaultDistanceUnit: Flow<Int> = context.dataStore.data.catch {
        if (it is IOException) {
            emit(emptyPreferences())
        } else {
            throw it
        }
    }.map { preferences ->
        preferences[DEFAULT_DISTANCE_UNIT] ?: DistanceUnits.KILOMETERS.shortForm
    }

    override val isDisplayHighestWeight: Flow<Boolean> = context.dataStore.data.catch {
        if (it is IOException) {
            emit(emptyPreferences())
        } else {
            throw it
        }
    }.map { preferences ->
        preferences[DISPLAY_HIGHEST_WEIGHT] ?: true
    }

    override val isDisplayShortestTime: Flow<Boolean> = context.dataStore.data.catch {
        if (it is IOException) {
            emit(emptyPreferences())
        } else {
            throw it
        }
    }.map { preferences ->
        preferences[DISPLAY_SHORTEST_TIME] ?: true
    }
}
