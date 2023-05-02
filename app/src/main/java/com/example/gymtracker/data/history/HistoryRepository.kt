package com.example.gymtracker.data.history

import kotlinx.coroutines.flow.Flow

interface HistoryRepository {

    fun getHistoryStream(id: Int): Flow<ExerciseHistory?>

    suspend fun insertHistory(history: ExerciseHistory)
}
