package com.askein.gymtracker.fake

import com.askein.gymtracker.data.exercise.Exercise
import com.askein.gymtracker.data.workout.Workout
import com.askein.gymtracker.data.workoutWithExercises.WorkoutWithExercises
import com.askein.gymtracker.data.workoutWithExercises.WorkoutWithExercisesRepository
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