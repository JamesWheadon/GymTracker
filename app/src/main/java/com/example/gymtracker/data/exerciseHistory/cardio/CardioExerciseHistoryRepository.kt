package com.example.gymtracker.data.exerciseHistory.cardio

import com.example.gymtracker.data.exercise.Exercise
import kotlinx.coroutines.flow.Flow

interface CardioExerciseHistoryRepository {
    fun getStream(id: Int): Flow<CardioExerciseHistory?>

    fun getFullExerciseHistoryStream(exerciseId: Int): Flow<List<CardioExerciseHistory>?>

    suspend fun insert(history: CardioExerciseHistory)

    suspend fun update(history: CardioExerciseHistory)

    suspend fun delete(history: CardioExerciseHistory)

    suspend fun deleteAllForExercise(exercise: Exercise)
}
