package com.example.gymtracker.fake

import com.example.gymtracker.data.exercise.Exercise
import com.example.gymtracker.data.exerciseHistory.weights.WeightsExerciseHistory
import com.example.gymtracker.data.exerciseHistory.weights.WeightsExerciseHistoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeWeightsExerciseHistoryRepository : WeightsExerciseHistoryRepository {

    override fun getHistoryStream(id: Int): Flow<WeightsExerciseHistory?> {
        return flowOf(null)
    }

    override suspend fun insert(history: WeightsExerciseHistory) {
    }

    override suspend fun update(history: WeightsExerciseHistory) {
    }

    override suspend fun delete(history: WeightsExerciseHistory) {
    }

    override suspend fun deleteAllForExercise(exercise: Exercise) {
    }
}
