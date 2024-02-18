package com.example.gymtracker.fake

import com.example.gymtracker.data.exercise.Exercise
import com.example.gymtracker.data.exerciseHistory.weights.WeightsExerciseHistory
import com.example.gymtracker.data.exerciseHistory.weights.WeightsExerciseHistoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.flowOf

class FakeWeightsExerciseHistoryRepository : WeightsExerciseHistoryRepository {

    private val allHistoryFlow = MutableSharedFlow<List<WeightsExerciseHistory>>()

    suspend fun emitAllHistory(value: List<WeightsExerciseHistory>) {
        allHistoryFlow.emit(value)
    }
    override fun getHistoryStream(id: Int): Flow<WeightsExerciseHistory?> {
        return flowOf(null)
    }

    override fun getFullExerciseHistoryStream(exerciseId: Int): Flow<List<WeightsExerciseHistory>?> = allHistoryFlow

    override fun getLatestHistoryStream(id: Int): Flow<WeightsExerciseHistory?> = flowOf(null)

    override fun getRecentHistoryStream(id: Int, days: Int): Flow<List<WeightsExerciseHistory>?> = allHistoryFlow

    override suspend fun insertHistory(history: WeightsExerciseHistory) {
    }

    override suspend fun update(history: WeightsExerciseHistory) {
    }

    override suspend fun delete(history: WeightsExerciseHistory) {
    }

    override suspend fun deleteAllForExercise(exercise: Exercise) {
    }
}
