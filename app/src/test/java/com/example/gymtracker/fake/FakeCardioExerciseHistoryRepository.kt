package com.example.gymtracker.fake

import com.example.gymtracker.data.exercise.Exercise
import com.example.gymtracker.data.exerciseHistory.cardio.CardioExerciseHistory
import com.example.gymtracker.data.exerciseHistory.cardio.CardioExerciseHistoryRepository

class FakeCardioExerciseHistoryRepository: CardioExerciseHistoryRepository {

    override suspend fun insert(history: CardioExerciseHistory) {
    }

    override suspend fun update(history: CardioExerciseHistory) {
    }

    override suspend fun delete(history: CardioExerciseHistory) {
    }

    override suspend fun deleteAllForExercise(exercise: Exercise) {
    }
}
