package com.example.gymtracker.ui.workout.history

import com.example.gymtracker.data.workoutHistory.WorkoutHistoryWithExerciseHistory
import com.example.gymtracker.ui.exercise.history.ExerciseHistoryUiState
import com.example.gymtracker.ui.exercise.history.toExerciseHistoryUiState
import java.time.LocalDate

data class WorkoutHistoryUiState(
    val workoutHistoryId: Int = 0,
    val workoutId: Int = 0,
    var date: LocalDate = LocalDate.now(),
    var exercises: List<ExerciseHistoryUiState> = listOf()
)

fun WorkoutHistoryWithExerciseHistory.toWorkoutHistoryUiState(): WorkoutHistoryUiState = WorkoutHistoryUiState(
    workoutHistoryId = workoutHistory.workoutHistoryId,
    workoutId = workoutHistory.workoutId,
    date = workoutHistory.date,
    exercises = exercises.map { exerciseHistory -> exerciseHistory.toExerciseHistoryUiState() }
)
