package com.example.gymtracker.ui.workout

import com.example.gymtracker.data.workout.Workout

data class WorkoutUiState(
    val workoutId: Int = 0,
    val name: String = ""
)

data class WorkoutListUiState(val workoutList: List<WorkoutUiState> = listOf())

fun Workout.toWorkoutUiState(): WorkoutUiState = WorkoutUiState(
    workoutId = workoutId,
    name = name
)

fun WorkoutUiState.toWorkout(): Workout = Workout(
    workoutId = workoutId,
    name = name
)
