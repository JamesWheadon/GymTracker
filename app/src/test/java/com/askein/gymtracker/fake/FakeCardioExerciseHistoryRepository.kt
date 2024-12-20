package com.askein.gymtracker.fake

import com.askein.gymtracker.data.exercise.Exercise
import com.askein.gymtracker.data.exerciseHistory.cardio.CardioExerciseHistory
import com.askein.gymtracker.data.exerciseHistory.cardio.CardioExerciseHistoryRepository

class FakeCardioExerciseHistoryRepository: CardioExerciseHistoryRepository {

    override suspend fun insert(history: CardioExerciseHistory) {
    }

    override suspend fun update(history: CardioExerciseHistory) {
    }

    override suspend fun delete(history: CardioExerciseHistory) {
    }

    override suspend fun deleteAllForExercise(exercise: Exercise) {
    }

    override suspend fun deleteAllForWorkoutHistory(workoutHistoryId: Int) {
    }
}
