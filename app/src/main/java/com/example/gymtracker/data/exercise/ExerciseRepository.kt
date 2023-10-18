package com.example.gymtracker.data.exercise

import kotlinx.coroutines.flow.Flow

interface ExerciseRepository {
    fun getExerciseStream(id: Int): Flow<Exercise?>

    fun getAllExercisesStream(): Flow<List<Exercise>>

    fun getAllMuscleGroupExercisesStream(muscleGroup: String): Flow<List<Exercise>>

    fun getAllMuscleGroupsStream(): Flow<List<String>>

    suspend fun insertExercise(exercise: Exercise)

    suspend fun deleteExercise(exercise: Exercise)

    suspend fun updateExercise(exercise: Exercise)
}
