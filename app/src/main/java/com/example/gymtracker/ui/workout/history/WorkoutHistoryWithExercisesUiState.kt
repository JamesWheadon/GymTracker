package com.example.gymtracker.ui.workout.history

import com.example.gymtracker.data.workoutHistory.WorkoutHistory
import com.example.gymtracker.data.workoutWithExercises.WorkoutHistoryWithExerciseHistory
import com.example.gymtracker.ui.exercise.history.state.ExerciseHistoryUiState
import com.example.gymtracker.ui.exercise.history.state.toCardioExerciseHistoryUiState
import com.example.gymtracker.ui.exercise.history.state.toWeightsExerciseHistoryUiState
import java.time.LocalDate

data class WorkoutHistoryWithExercisesUiState(
    val workoutHistoryId: Int = 0,
    val workoutId: Int = 0,
    var date: LocalDate = LocalDate.now(),
    var exercises: List<ExerciseHistoryUiState> = listOf()
)

data class WorkoutHistoryUiState(
    val workoutHistoryId: Int = 0,
    val workoutId: Int = 0,
    var date: LocalDate = LocalDate.now()
)

fun WorkoutHistoryWithExerciseHistory.toWorkoutHistoryWithExercisesUiState(): WorkoutHistoryWithExercisesUiState =
    WorkoutHistoryWithExercisesUiState(
        workoutHistoryId = workoutHistory.workoutHistoryId,
        workoutId = workoutHistory.workoutId,
        date = workoutHistory.date,
        exercises = listOf(weightsExercises.map { it.toWeightsExerciseHistoryUiState() }, cardioExercises.map { it.toCardioExerciseHistoryUiState() }).flatten()
    )

fun WorkoutHistoryWithExercisesUiState.toWorkoutHistoryUiState(): WorkoutHistoryUiState =
    WorkoutHistoryUiState(
        workoutHistoryId = workoutHistoryId,
        workoutId = workoutId,
        date = date
    )

fun WorkoutHistoryUiState.toWorkoutHistory(): WorkoutHistory = WorkoutHistory(
    workoutHistoryId = workoutHistoryId,
    workoutId = workoutId,
    date = date
)
