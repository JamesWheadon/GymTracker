package com.example.gymtracker.data.exercise

import kotlinx.coroutines.flow.Flow

interface ExerciseRepository {

    fun getAllExercisesStream(): Flow<List<Exercise>>

    fun getAllMuscleGroupsStream(): Flow<List<String>>

    fun getAllExerciseNames(): Flow<List<String>>

    suspend fun insertExercise(exercise: Exercise)

    suspend fun deleteExercise(exercise: Exercise)

    suspend fun updateExercise(exercise: Exercise)
}
