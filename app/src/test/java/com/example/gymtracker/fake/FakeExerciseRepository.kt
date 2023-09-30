package com.example.gymtracker.fake

import com.example.gymtracker.data.exercise.Exercise
import com.example.gymtracker.data.exercise.ExerciseRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.flowOf

class FakeExerciseRepository : ExerciseRepository {

    private val allExercisesFlow = MutableSharedFlow<List<Exercise>>()
    suspend fun emitAllExercises(value: List<Exercise>) {
        allExercisesFlow.emit(value)
    }

    override fun getExerciseStream(id: Int): Flow<Exercise?> {
        return flowOf(null)
    }

    override fun getAllExercisesStream(): Flow<List<Exercise>> = allExercisesFlow

    override fun getAllMuscleGroupExercisesStream(muscleGroup: String): Flow<List<Exercise>> =
        allExercisesFlow

    override suspend fun insertExercise(exercise: Exercise) {
    }

    override suspend fun deleteExercise(exercise: Exercise) {
    }

    override suspend fun updateExercise(exercise: Exercise) {
    }
}