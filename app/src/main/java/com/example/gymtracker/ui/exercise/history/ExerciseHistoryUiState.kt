package com.example.gymtracker.ui.exercise.history

import com.example.gymtracker.data.exerciseHistory.weights.WeightsExerciseHistory
import java.time.LocalDate

data class ExerciseHistoryUiState(
    val id: Int = 0,
    val exerciseId: Int = 0,
    var weight: Double = 0.0,
    var sets: Int = 0,
    var reps: Int = 0,
    var rest: Int = 0,
    val date: LocalDate = LocalDate.now()
)

fun WeightsExerciseHistory.toExerciseHistoryUiState(): ExerciseHistoryUiState = ExerciseHistoryUiState(
    id = id,
    exerciseId = exerciseId,
    weight = weight,
    sets = sets,
    reps = reps,
    date = date
)

fun ExerciseHistoryUiState.toExerciseHistory(exerciseId: Int): WeightsExerciseHistory = WeightsExerciseHistory(
    id = id,
    exerciseId = exerciseId,
    weight = weight,
    sets = sets,
    reps = reps,
    date = date
)

fun ExerciseHistoryUiState.toExerciseHistory(): WeightsExerciseHistory = WeightsExerciseHistory(
    id = id,
    exerciseId = exerciseId,
    weight = weight,
    sets = sets,
    reps = reps,
    date = date
)
