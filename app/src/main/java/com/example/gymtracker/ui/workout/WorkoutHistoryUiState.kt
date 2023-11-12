package com.example.gymtracker.ui.workout

import com.example.gymtracker.data.workoutHistory.WorkoutHistory
import java.time.LocalDate

data class WorkoutHistoryUiState(
    val workoutHistoryId: Int = 0,
    val workoutId: Int = 0,
    var date: LocalDate = LocalDate.now()
)

fun WorkoutHistory.toWorkoutHistoryUiState(): WorkoutHistoryUiState = WorkoutHistoryUiState(
    workoutHistoryId = workoutHistoryId,
    workoutId = workoutId,
    date = date
)
