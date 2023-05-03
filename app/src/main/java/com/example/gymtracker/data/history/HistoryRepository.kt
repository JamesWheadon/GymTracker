package com.example.gymtracker.data.history

import kotlinx.coroutines.flow.Flow

interface HistoryRepository {

    fun getHistoryStream(id: Int): Flow<ExerciseHistory?>

    fun getFullExerciseHistoryStream(exerciseId: Int): Flow<List<ExerciseHistory>?>

    fun getLatestHistoryStream(id: Int): Flow<ExerciseHistory?>

    fun getRecentHistoryStream(id: Int, days: Int): Flow<List<ExerciseHistory>?>

    suspend fun insertHistory(history: ExerciseHistory)
}
