package com.example.gymtracker.ui.workout.details

import com.example.gymtracker.data.workoutWithExercises.WorkoutWithExercises
import com.example.gymtracker.ui.exercise.ExerciseUiState
import com.example.gymtracker.ui.exercise.toExerciseUiState
import com.example.gymtracker.ui.workout.WorkoutUiState
import com.example.gymtracker.ui.workout.history.WorkoutHistoryWithExercisesUiState
import com.example.gymtracker.ui.workout.history.toWorkoutHistoryWithExercisesUiState

data class WorkoutWithExercisesUiState(
    val workoutId: Int = 0,
    val name: String = "",
    val exercises: List<ExerciseUiState> = listOf(),
    val workoutHistory: List<WorkoutHistoryWithExercisesUiState> = listOf()
)

fun WorkoutWithExercises.toWorkoutWithExercisesUiState(): WorkoutWithExercisesUiState = WorkoutWithExercisesUiState(
    workoutId = workout.workoutId,
    name = workout.name,
    exercises = exercises.map { exercise -> exercise.toExerciseUiState() },
    workoutHistory = workoutHistory.map { workoutHistory -> workoutHistory.toWorkoutHistoryWithExercisesUiState() }
)

fun WorkoutWithExercisesUiState.toWorkoutUiState(): WorkoutUiState = WorkoutUiState(
    workoutId = workoutId,
    name = name
)
