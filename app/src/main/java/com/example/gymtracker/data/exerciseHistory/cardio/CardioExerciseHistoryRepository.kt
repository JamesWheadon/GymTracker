package com.example.gymtracker.data.exerciseHistory.cardio

import com.example.gymtracker.data.exercise.Exercise

interface CardioExerciseHistoryRepository {

    suspend fun insert(history: CardioExerciseHistory)

    suspend fun update(history: CardioExerciseHistory)

    suspend fun delete(history: CardioExerciseHistory)

    suspend fun deleteAllForExercise(exercise: Exercise)
}
