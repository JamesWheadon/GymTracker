package com.askein.gymtracker.ui.workout.history

import com.askein.gymtracker.data.workoutHistory.WorkoutHistory
import com.askein.gymtracker.data.workoutWithExercises.WorkoutHistoryWithExerciseHistory
import com.askein.gymtracker.ui.exercise.history.state.ExerciseHistoryUiState
import com.askein.gymtracker.ui.exercise.history.state.toCardioExerciseHistoryUiState
import com.askein.gymtracker.ui.exercise.history.state.toWeightsExerciseHistoryUiState
import java.time.LocalDate

data class WorkoutHistoryWithExercisesUiState(
    val workoutHistoryId: Int = 0,
    val workoutId: Int = 0,
    var date: LocalDate = LocalDate.now(),
    var exerciseHistories: List<ExerciseHistoryUiState> = listOf()
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
        exerciseHistories = listOf(weightsExercises.map { it.toWeightsExerciseHistoryUiState() }, cardioExercises.map { it.toCardioExerciseHistoryUiState() }).flatten()
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
