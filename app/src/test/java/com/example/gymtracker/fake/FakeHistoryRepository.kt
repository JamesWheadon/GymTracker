package com.example.gymtracker.fake

import com.example.gymtracker.data.history.ExerciseHistory
import com.example.gymtracker.data.history.HistoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.flowOf

class FakeHistoryRepository : HistoryRepository {

    private val allHistoryFlow = MutableSharedFlow<List<ExerciseHistory>>()

    suspend fun emitAllHistory(value: List<ExerciseHistory>) {
        allHistoryFlow.emit(value)
    }
    override fun getHistoryStream(id: Int): Flow<ExerciseHistory?> {
        return flowOf(null)
    }

    override fun getFullExerciseHistoryStream(exerciseId: Int): Flow<List<ExerciseHistory>?> = allHistoryFlow

    override fun getLatestHistoryStream(id: Int): Flow<ExerciseHistory?> = flowOf(null)

    override fun getRecentHistoryStream(id: Int, days: Int): Flow<List<ExerciseHistory>?> = allHistoryFlow

    override suspend fun insertHistory(history: ExerciseHistory) {
    }

    override suspend fun update(history: ExerciseHistory) {
    }

    override suspend fun delete(history: ExerciseHistory) {
    }

}
