package com.example.gymtracker.ui.history

import com.example.gymtracker.data.history.ExerciseHistory
import java.util.Date

data class ExerciseHistoryUiState(
    val id: Int = 0,
    var weight: Double = 0.0,
    var sets: Int = 0,
    var reps: Int = 0,
    var rest: Int = 0,
    val date: Date = Date()
)

fun ExerciseHistory.toExerciseHistoryUiState(): ExerciseHistoryUiState = ExerciseHistoryUiState(
    id = id,
    weight = weight,
    sets = sets,
    reps = reps,
    rest = rest,
    date = date
)
