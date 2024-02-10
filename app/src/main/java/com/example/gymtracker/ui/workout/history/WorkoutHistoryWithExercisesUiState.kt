package com.example.gymtracker.ui.workout.history

import com.example.gymtracker.data.exerciseHistory.weights.WeightsExerciseHistory
import com.example.gymtracker.data.workoutHistory.WorkoutHistory
import com.example.gymtracker.data.workoutHistory.WorkoutHistoryWithExerciseHistory
import com.example.gymtracker.ui.exercise.history.state.WeightsExerciseHistoryUiState
import com.example.gymtracker.ui.exercise.history.state.toWeightsExerciseHistory
import com.example.gymtracker.ui.exercise.history.state.toWeightsExerciseHistoryUiState
import java.time.LocalDate

data class WorkoutHistoryWithExercisesUiState(
    val workoutHistoryId: Int = 0,
    val workoutId: Int = 0,
    var date: LocalDate = LocalDate.now(),
    var exercises: List<WeightsExerciseHistoryUiState> = listOf()
)

data class WorkoutHistoryUiState(
    val workoutHistoryId: Int = 0,
    val workoutId: Int = 0,
    var date: LocalDate = LocalDate.now()
)

fun WorkoutHistoryWithExerciseHistory.toWorkoutHistoryWithExercisesUiState(): WorkoutHistoryWithExercisesUiState = WorkoutHistoryWithExercisesUiState(
    workoutHistoryId = workoutHistory.workoutHistoryId,
    workoutId = workoutHistory.workoutId,
    date = workoutHistory.date,
    exercises = exercises.map { exerciseHistory -> exerciseHistory.toWeightsExerciseHistoryUiState() }
)

fun WorkoutHistoryWithExercisesUiState.toWorkoutUiState(): WorkoutHistoryUiState = WorkoutHistoryUiState(
    workoutHistoryId = workoutHistoryId,
    workoutId = workoutId,
    date = date
)

fun WorkoutHistoryWithExercisesUiState.toWorkoutAndExercises(): Pair<WorkoutHistory, List<WeightsExerciseHistory>> {
    return Pair(this.toWorkoutUiState().toWorkoutHistory(), this.exercises.map { exerciseHistory -> exerciseHistory.toWeightsExerciseHistory() })
}

fun WorkoutHistoryUiState.toWorkoutHistory(): WorkoutHistory = WorkoutHistory(
    workoutHistoryId = workoutHistoryId,
    workoutId = workoutId,
    date = date
)
