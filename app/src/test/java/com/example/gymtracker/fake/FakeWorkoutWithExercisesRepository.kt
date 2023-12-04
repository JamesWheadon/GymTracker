package com.example.gymtracker.fake

import com.example.gymtracker.data.exercise.Exercise
import com.example.gymtracker.data.workout.Workout
import com.example.gymtracker.data.workoutWithExercises.WorkoutWithExercises
import com.example.gymtracker.data.workoutWithExercises.WorkoutWithExercisesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeWorkoutWithExercisesRepository : WorkoutWithExercisesRepository {

    private val workoutWithExercises = WorkoutWithExercises(
        workout = Workout(
            workoutId = 1,
            name = "Test"
        ),
        exercises = listOf(
            Exercise(
                exerciseId = 1,
                name = "testName",
                muscleGroup = "muscleGroup",
                equipment = "equipment"
            )
        ),
        workoutHistory = listOf()
    )

    override fun getWorkoutWithExercisesStream(workoutId: Int): Flow<WorkoutWithExercises> = flowOf(workoutWithExercises)
}
