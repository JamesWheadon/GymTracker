package com.example.gymtracker.data.exercise

import kotlinx.coroutines.flow.Flow

interface ExerciseRepository {
    fun getExerciseStream(id: Int): Flow<Exercise?>

    fun getAllExercisesStream(): Flow<List<Exercise>>

    suspend fun getAllMuscleGroupExercisesStream(muscleGroup: String): Flow<List<Exercise>>

    suspend fun insertExercise(exercise: Exercise)

    suspend fun deleteExercise(exercise: Exercise)

    suspend fun updateExercise(exercise: Exercise)
}
