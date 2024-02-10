package com.example.gymtracker.ui.exercise.history.state

import com.example.gymtracker.data.exerciseHistory.cardio.CardioExerciseHistory
import java.time.LocalDate


data class CardioExerciseHistoryUiState(
    override val id: Int = 0,
    override val exerciseId: Int = 0,
    override val date: LocalDate = LocalDate.now(),
    var minutes: Int? = null,
    var seconds: Int? = null,
    var calories: Int? = null,
    var distance: Double? = null
) : ExerciseHistoryUiState

fun CardioExerciseHistory.toCardioExerciseHistoryUiState(): CardioExerciseHistoryUiState = CardioExerciseHistoryUiState(
    id = id,
    exerciseId = exerciseId,
    date = date,
    minutes = minutes,
    seconds = seconds,
    calories = calories,
    distance = distance
)

fun CardioExerciseHistoryUiState.toCardioExerciseHistory(): CardioExerciseHistory = CardioExerciseHistory(
    id = id,
    exerciseId = exerciseId,
    date = date,
    minutes = minutes,
    seconds = seconds,
    calories = calories,
    distance = distance
)
