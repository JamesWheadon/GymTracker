package com.askein.gymtracker.ui.history

import com.askein.gymtracker.ui.exercise.ExerciseUiState
import com.askein.gymtracker.ui.workout.WorkoutUiState
import java.time.LocalDate

data class HistoryUiState(
    val date: LocalDate? = null,
    val workouts: List<WorkoutUiState> = emptyList(),
    val exercises: List<ExerciseUiState> = emptyList()
)
