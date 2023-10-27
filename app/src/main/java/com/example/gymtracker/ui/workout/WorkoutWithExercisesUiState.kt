package com.example.gymtracker.ui.workout

import com.example.gymtracker.data.workoutWithExercises.WorkoutWithExercises
import com.example.gymtracker.ui.exercise.ExerciseUiState
import com.example.gymtracker.ui.exercise.toExerciseUiState

data class WorkoutWithExercisesUiState(
    val workoutId: Int = 0,
    val name: String = "",
    val exercises: List<ExerciseUiState> = listOf()
)

fun WorkoutWithExercises.toWorkoutWithExercisesUiState(): WorkoutWithExercisesUiState = WorkoutWithExercisesUiState(
    workoutId = workout.workoutId,
    name = workout.name,
    exercises = exercises.map { exercise -> exercise.toExerciseUiState() }
)
