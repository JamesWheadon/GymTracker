package com.example.gymtracker.data

import kotlinx.coroutines.flow.Flow

interface ExercisesRepository {

    fun getExerciseStream(id: Int): Flow<Exercise?>

    fun getAllExercisesStream(): Flow<List<Exercise?>>

    suspend fun insertExercise(exercise: Exercise)

    suspend fun deleteExercise(exercise: Exercise)

    suspend fun updateExercise(exercise: Exercise)
}
