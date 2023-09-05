package com.example.gymtracker.ui.exercise

import com.example.gymtracker.data.exercise.Exercise
import com.example.gymtracker.ui.history.ExerciseHistoryUiState

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
    var history: List<ExerciseHistoryUiState>? = listOf()
)

data class ExerciseListUiState(val exerciseList: List<Exercise> = listOf())

fun Exercise.toExerciseUiState(): ExerciseUiState = ExerciseUiState(
    id = id,
    name = name,
    muscleGroup = muscleGroup,
    equipment = equipment
)

fun Exercise.toExerciseDetailsUiState(): ExerciseDetailsUiState = ExerciseDetailsUiState(
    id = id,
    name = name,
    muscleGroup = muscleGroup,
    equipment = equipment,
    history = listOf()
)
