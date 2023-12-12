package com.example.gymtracker.data.exerciseHistory

import com.example.gymtracker.data.exercise.Exercise
import kotlinx.coroutines.flow.Flow

interface ExerciseHistoryRepository {
    fun getHistoryStream(id: Int): Flow<ExerciseHistory?>

    fun getFullExerciseHistoryStream(exerciseId: Int): Flow<List<ExerciseHistory>?>

    fun getLatestHistoryStream(id: Int): Flow<ExerciseHistory?>

    fun getRecentHistoryStream(id: Int, days: Int): Flow<List<ExerciseHistory>?>

    suspend fun insertHistory(history: ExerciseHistory)

    suspend fun update(history: ExerciseHistory)

    suspend fun delete(history: ExerciseHistory)

    suspend fun deleteAllForExercise(exercise: Exercise)
}