package com.askein.gymtracker.fake

import com.askein.gymtracker.data.exercise.Exercise
import com.askein.gymtracker.data.exerciseHistory.weights.WeightsExerciseHistory
import com.askein.gymtracker.data.exerciseHistory.weights.WeightsExerciseHistoryRepository

class FakeWeightsExerciseHistoryRepository : WeightsExerciseHistoryRepository {

    override suspend fun insert(history: WeightsExerciseHistory) {
    }

    override suspend fun update(history: WeightsExerciseHistory) {
    }

    override suspend fun delete(history: WeightsExerciseHistory) {
    }

    override suspend fun deleteAllForExercise(exercise: Exercise) {
    }

    override suspend fun deleteAllForWorkoutHistory(workoutHistoryId: Int) {
    }
}
