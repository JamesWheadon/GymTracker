package com.example.gymtracker.fake

import com.example.gymtracker.data.exercise.Exercise
import com.example.gymtracker.data.exercise.ExerciseRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.flowOf

class FakeExerciseRepository : ExerciseRepository {

    val exercise = Exercise(
        exerciseId = 1,
        name = "testName",
        muscleGroup = "muscleGroup",
        equipment = "equipment"
    )
    private val allExercisesFlow = MutableSharedFlow<List<Exercise>>()
    private val allMuscleGroupsFlow = MutableSharedFlow<List<String>>()
    private val allExerciseNamesFlow = MutableSharedFlow<List<String>>()

    suspend fun emitAllExercises(value: List<Exercise>) {
        allExercisesFlow.emit(value)
    }

    suspend fun emitAllMuscleGroups(value: List<String>) {
        allMuscleGroupsFlow.emit(value)
    }

    suspend fun emitAllExerciseNames(value: List<String>) {
        allExerciseNamesFlow.emit(value)
    }

    override fun getExerciseStream(id: Int): Flow<Exercise?> = flowOf(exercise)

    override fun getAllExercisesStream(): Flow<List<Exercise>> = allExercisesFlow

    override fun getAllMuscleGroupExercisesStream(muscleGroup: String): Flow<List<Exercise>> =
        allExercisesFlow

    override fun getAllMuscleGroupsStream(): Flow<List<String>> = allMuscleGroupsFlow

    override fun getAllExerciseNames(): Flow<List<String>> = allExerciseNamesFlow

    override suspend fun insertExercise(exercise: Exercise) {
    }

    override suspend fun deleteExercise(exercise: Exercise) {
    }

    override suspend fun updateExercise(exercise: Exercise) {
    }
}