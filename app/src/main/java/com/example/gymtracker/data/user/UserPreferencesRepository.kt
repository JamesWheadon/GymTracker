package com.example.gymtracker.data.user

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.gymtracker.converters.DistanceUnits
import com.example.gymtracker.converters.WeightUnits
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

class UserPreferencesRepository(private val context: Context) {
    private companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settingPrefs")
        val DEFAULT_WEIGHT_UNIT = stringPreferencesKey("default_weight_unit")
        val DEFAULT_DISTANCE_UNIT = stringPreferencesKey("default_distance_unit")
        val DISPLAY_HIGHEST_WEIGHT = booleanPreferencesKey("display_highest_weight")
        val DISPLAY_SHORTEST_TIME = booleanPreferencesKey("display_shortest_time")
    }

    suspend fun saveDefaultWeightUnit(defaultWeightUnit: String) {
        context.dataStore.edit { preferences ->
            preferences[DEFAULT_WEIGHT_UNIT] = defaultWeightUnit
        }
    }

    suspend fun saveDefaultDistanceUnit(defaultDistanceUnit: String) {
        context.dataStore.edit { preferences ->
            preferences[DEFAULT_DISTANCE_UNIT] = defaultDistanceUnit
        }
    }

    suspend fun saveDisplayHighestWeight(displayHighestWeight: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[DISPLAY_HIGHEST_WEIGHT] = displayHighestWeight
        }
    }

    suspend fun saveDisplayShortestTime(displayShortestTime: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[DISPLAY_SHORTEST_TIME] = displayShortestTime
        }
    }

    val isDefaultWeightUnit: Flow<String> = context.dataStore.data.catch {
        if (it is IOException) {
            emit(emptyPreferences())
        } else {
            throw it
        }
    }.map { preferences ->
        preferences[DEFAULT_WEIGHT_UNIT] ?: WeightUnits.KILOGRAMS.shortForm
    }

    val isDefaultDistanceUnit: Flow<String> = context.dataStore.data.catch {
        if (it is IOException) {
            emit(emptyPreferences())
        } else {
            throw it
        }
    }.map { preferences ->
        preferences[DEFAULT_DISTANCE_UNIT] ?: DistanceUnits.KILOMETERS.shortForm
    }

    val isDisplayHighestWeight: Flow<Boolean> = context.dataStore.data.catch {
        if (it is IOException) {
            emit(emptyPreferences())
        } else {
            throw it
        }
    }.map { preferences ->
        preferences[DISPLAY_HIGHEST_WEIGHT] ?: true
    }

    val isDisplayShortestTime: Flow<Boolean> = context.dataStore.data.catch {
        if (it is IOException) {
            emit(emptyPreferences())
        } else {
            throw it
        }
    }.map { preferences ->
        preferences[DISPLAY_SHORTEST_TIME] ?: true
    }
}
