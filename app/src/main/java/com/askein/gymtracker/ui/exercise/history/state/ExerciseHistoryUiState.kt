package com.askein.gymtracker.ui.exercise.history.state

import java.time.LocalDate

sealed interface ExerciseHistoryUiState {
    val id: Int
    val exerciseId: Int
    val date: LocalDate
    var workoutHistoryId: Int?
}