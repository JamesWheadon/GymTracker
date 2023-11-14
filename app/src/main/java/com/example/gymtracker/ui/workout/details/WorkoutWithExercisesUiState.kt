package com.example.gymtracker.ui.workout.details

import com.example.gymtracker.data.workout.Workout
import com.example.gymtracker.data.workoutWithExercises.WorkoutWithExercises
import com.example.gymtracker.ui.exercise.ExerciseUiState
import com.example.gymtracker.ui.exercise.toExerciseUiState
import com.example.gymtracker.ui.workout.history.WorkoutHistoryUiState
import com.example.gymtracker.ui.workout.history.toWorkoutHistoryUiState

data class WorkoutWithExercisesUiState(
    val workoutId: Int = 0,
    val name: String = "",
    val exercises: List<ExerciseUiState> = listOf(),
    val workoutHistory: List<WorkoutHistoryUiState> = listOf()
)

fun WorkoutWithExercises.toWorkoutWithExercisesUiState(): WorkoutWithExercisesUiState = WorkoutWithExercisesUiState(
    workoutId = workout.workoutId,
    name = workout.name,
    exercises = exercises.map { exercise -> exercise.toExerciseUiState() },
    workoutHistory = workoutHistory.map { workoutHistory -> workoutHistory.toWorkoutHistoryUiState() }
)

fun WorkoutWithExercisesUiState.toWorkout(): Workout = Workout(
    workoutId = workoutId,
    name = name
)
