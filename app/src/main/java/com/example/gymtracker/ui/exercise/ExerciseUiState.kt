package com.example.gymtracker.ui.exercise

import com.example.gymtracker.data.exercise.Exercise
import com.example.gymtracker.ui.exercise.history.state.WeightsExerciseHistoryUiState

data class ExerciseUiState(
    val id: Int = 0,
    val name: String = "",
    val muscleGroup: String = "",
    val equipment: String = "",
)

data class ExerciseDetailsUiState(
    val id: Int = 0,
    val name: String = "",
    val muscleGroup: String = "",
    val equipment: String = "",
    var history: List<WeightsExerciseHistoryUiState>? = listOf()
)

data class ExerciseListUiState(val exerciseList: List<ExerciseUiState> = listOf())

fun Exercise.toExerciseUiState(): ExerciseUiState = ExerciseUiState(
    id = exerciseId,
    name = name,
    muscleGroup = muscleGroup,
    equipment = equipment
)

fun Exercise.toExerciseDetailsUiState(): ExerciseDetailsUiState = ExerciseDetailsUiState(
    id = exerciseId,
    name = name,
    muscleGroup = muscleGroup,
    equipment = equipment,
    history = listOf()
)

fun ExerciseUiState.toExercise(): Exercise = Exercise(
    exerciseId = id,
    name = name,
    equipment = equipment,
    muscleGroup = muscleGroup
)

fun ExerciseDetailsUiState.toExerciseUiState(): ExerciseUiState = ExerciseUiState(
    id = id,
    name = name,
    muscleGroup = muscleGroup,
    equipment = equipment
)

fun ExerciseDetailsUiState.toExercise(): Exercise = Exercise(
    exerciseId = id,
    name = name,
    equipment = equipment,
    muscleGroup = muscleGroup
)
