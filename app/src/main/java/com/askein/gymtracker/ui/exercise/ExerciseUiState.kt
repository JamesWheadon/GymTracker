package com.askein.gymtracker.ui.exercise

import com.askein.gymtracker.data.exercise.Exercise
import com.askein.gymtracker.data.exercise.ExerciseType

data class ExerciseUiState(
    val id: Int = 0,
    val name: String = "",
    val muscleGroup: String = "",
    val equipment: String = "",
    val type: ExerciseType = ExerciseType.WEIGHTS,
)

data class ExerciseListUiState(val exerciseList: List<ExerciseUiState> = listOf())

fun Exercise.toExerciseUiState(): ExerciseUiState = ExerciseUiState(
    id = exerciseId,
    type = exerciseType,
    name = name,
    muscleGroup = muscleGroup,
    equipment = equipment
)

fun ExerciseUiState.toExercise(): Exercise = Exercise(
    exerciseId = id,
    exerciseType = type,
    name = name,
    equipment = equipment,
    muscleGroup = muscleGroup
)
