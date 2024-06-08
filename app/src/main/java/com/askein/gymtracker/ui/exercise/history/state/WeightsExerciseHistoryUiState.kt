package com.askein.gymtracker.ui.exercise.history.state

import com.askein.gymtracker.data.exerciseHistory.weights.WeightsExerciseHistory
import java.time.LocalDate

data class WeightsExerciseHistoryUiState(
    override val id: Int = 0,
    override val exerciseId: Int = 0,
    override var date: LocalDate = LocalDate.now(),
    override var workoutHistoryId: Int? = null,
    var weight: Double = 0.0,
    var sets: Int = 0,
    var reps: Int = 0,
    var rest: Int? = null
) : ExerciseHistoryUiState

fun WeightsExerciseHistory.toWeightsExerciseHistoryUiState(): WeightsExerciseHistoryUiState = WeightsExerciseHistoryUiState(
    id = id,
    exerciseId = exerciseId,
    workoutHistoryId = workoutHistoryId,
    weight = weight,
    sets = sets,
    reps = reps,
    date = date,
    rest = rest
)

fun WeightsExerciseHistoryUiState.toWeightsExerciseHistory(exerciseId: Int): WeightsExerciseHistory = WeightsExerciseHistory(
    id = id,
    exerciseId = exerciseId,
    workoutHistoryId = workoutHistoryId,
    weight = weight,
    sets = sets,
    reps = reps,
    date = date,
    rest = rest
)

fun WeightsExerciseHistoryUiState.toWeightsExerciseHistory(): WeightsExerciseHistory = WeightsExerciseHistory(
    id = id,
    exerciseId = exerciseId,
    workoutHistoryId = workoutHistoryId,
    weight = weight,
    sets = sets,
    reps = reps,
    date = date,
    rest = rest
)
