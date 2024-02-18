package com.example.gymtracker.ui.exercise.details

import com.example.gymtracker.data.exercise.Exercise
import com.example.gymtracker.data.exerciseWithHistory.ExerciseWithHistory
import com.example.gymtracker.ui.exercise.ExerciseUiState
import com.example.gymtracker.ui.exercise.history.state.CardioExerciseHistoryUiState
import com.example.gymtracker.ui.exercise.history.state.WeightsExerciseHistoryUiState
import com.example.gymtracker.ui.exercise.history.state.toCardioExerciseHistoryUiState
import com.example.gymtracker.ui.exercise.history.state.toWeightsExerciseHistoryUiState
import com.example.gymtracker.ui.exercise.toExerciseUiState


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

fun ExerciseDetailsUiState.toExercise(): Exercise = Exercise(
    exerciseId = exercise.id,
    name = exercise.name,
    muscleGroup = exercise.muscleGroup,
    equipment = exercise.equipment
)
