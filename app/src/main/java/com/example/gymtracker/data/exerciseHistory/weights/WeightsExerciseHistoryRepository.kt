package com.example.gymtracker.data.exerciseHistory.weights

import com.example.gymtracker.data.exercise.Exercise
import kotlinx.coroutines.flow.Flow

interface WeightsExerciseHistoryRepository {
    fun getHistoryStream(id: Int): Flow<WeightsExerciseHistory?>

    fun getFullExerciseHistoryStream(exerciseId: Int): Flow<List<WeightsExerciseHistory>?>

    fun getLatestHistoryStream(id: Int): Flow<WeightsExerciseHistory?>

    fun getRecentHistoryStream(id: Int, days: Int): Flow<List<WeightsExerciseHistory>?>

    suspend fun insertHistory(history: WeightsExerciseHistory)

    suspend fun update(history: WeightsExerciseHistory)

    suspend fun delete(history: WeightsExerciseHistory)

    suspend fun deleteAllForExercise(exercise: Exercise)
}
