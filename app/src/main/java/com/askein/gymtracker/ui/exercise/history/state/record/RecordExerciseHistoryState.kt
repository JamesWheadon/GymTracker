package com.askein.gymtracker.ui.exercise.history.state.record

import com.askein.gymtracker.ui.exercise.history.state.ExerciseHistoryUiState
import java.time.LocalDate

sealed interface RecordExerciseHistoryState {
    val historyId: Int
    val exerciseId: Int
    val workoutHistoryId: Int?
    val dateState: LocalDate

    fun isValid(): Boolean

    fun toHistoryUiState(): ExerciseHistoryUiState

    fun toHistoryUiState(date: LocalDate): ExerciseHistoryUiState
}
