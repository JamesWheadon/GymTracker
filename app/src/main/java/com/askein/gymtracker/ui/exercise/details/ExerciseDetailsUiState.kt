package com.askein.gymtracker.ui.exercise.details

import com.askein.gymtracker.data.exerciseWithHistory.ExerciseWithHistory
import com.askein.gymtracker.ui.exercise.ExerciseUiState
import com.askein.gymtracker.ui.exercise.history.state.CardioExerciseHistoryUiState
import com.askein.gymtracker.ui.exercise.history.state.WeightsExerciseHistoryUiState
import com.askein.gymtracker.ui.exercise.history.state.toCardioExerciseHistoryUiState
import com.askein.gymtracker.ui.exercise.history.state.toWeightsExerciseHistoryUiState
import com.askein.gymtracker.ui.exercise.toExerciseUiState

data class ExerciseDetailsUiState(
    val exercise: ExerciseUiState = ExerciseUiState(),
    val weightsHistory: List<WeightsExerciseHistoryUiState> = listOf(),
    val cardioHistory: List<CardioExerciseHistoryUiState> = listOf()
)

fun ExerciseWithHistory.toExerciseDetailsUiState(): ExerciseDetailsUiState = ExerciseDetailsUiState(
    exercise = exercise.toExerciseUiState(),
    weightsHistory = weightsHistory.map { it.toWeightsExerciseHistoryUiState() },
    cardioHistory = cardioHistory.map { it.toCardioExerciseHistoryUiState() }
)

fun ExerciseDetailsUiState.toExerciseUiState(): ExerciseUiState = ExerciseUiState(
    id = exercise.id,
    name = exercise.name,
    muscleGroup = exercise.muscleGroup,
    equipment = exercise.equipment
)
