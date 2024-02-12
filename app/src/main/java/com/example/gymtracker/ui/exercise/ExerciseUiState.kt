package com.example.gymtracker.ui.exercise

import com.example.gymtracker.data.exercise.Exercise

data class ExerciseUiState(
    val id: Int = 0,
    val name: String = "",
    val muscleGroup: String = "",
    val equipment: String = "",
)

data class ExerciseListUiState(val exerciseList: List<ExerciseUiState> = listOf())

fun Exercise.toExerciseUiState(): ExerciseUiState = ExerciseUiState(
    id = exerciseId,
    name = name,
    muscleGroup = muscleGroup,
    equipment = equipment
)

fun ExerciseUiState.toExercise(): Exercise = Exercise(
    exerciseId = id,
    name = name,
    equipment = equipment,
    muscleGroup = muscleGroup
)
