package com.askein.gymtracker.ui.exercise.history.state.record

import com.askein.gymtracker.enums.DistanceUnits
import com.askein.gymtracker.enums.convertToDistanceUnit
import com.askein.gymtracker.enums.convertToKilometers
import com.askein.gymtracker.ui.exercise.history.state.CardioExerciseHistoryUiState
import java.time.LocalDate

data class RecordCardioHistoryState(
    override val historyId: Int,
    override val exerciseId: Int,
    override val workoutHistoryId: Int?,
    override val dateState: LocalDate,
    val minutesState: String,
    val secondsState: String,
    val caloriesState: String,
    val distanceState: String,
    val unitState: DistanceUnits
) : RecordExerciseHistoryState {
    override fun isValid() = validTime() || validCalories() || validDistance()

    override fun toHistoryUiState() = CardioExerciseHistoryUiState(
        id = historyId,
        exerciseId = exerciseId,
        workoutHistoryId = workoutHistoryId,
        date = dateState,
        minutes = minutesState.toIntOrNull(),
        seconds = secondsState.toIntOrNull(),
        calories = caloriesState.toIntOrNull(),
        distance = if (distanceState != "") convertToKilometers(
            unitState,
            distanceState.toDouble()
        ) else null
    )

    override fun toHistoryUiState(date: LocalDate) = CardioExerciseHistoryUiState(
        id = historyId,
        exerciseId = exerciseId,
        workoutHistoryId = workoutHistoryId,
        date = date,
        minutes = minutesState.toIntOrNull(),
        seconds = secondsState.toIntOrNull(),
        calories = caloriesState.toIntOrNull(),
        distance = if (distanceState != "") convertToKilometers(
            unitState,
            distanceState.toDouble()
        ) else null
    )
}

fun RecordCardioHistoryState.validTime() = minutesState != "" && secondsState != "" && secondsState.toInt() < 60
fun RecordCardioHistoryState.validCalories() = caloriesState != ""
fun RecordCardioHistoryState.validDistance() = distanceState != ""

fun CardioExerciseHistoryUiState.toRecordCardioHistoryState(
    exerciseId: Int,
    distanceUnit: DistanceUnits
) = RecordCardioHistoryState(
    historyId = id,
    exerciseId = exerciseId,
    workoutHistoryId = workoutHistoryId,
    dateState = date,
    minutesState = minutes?.toString() ?: "",
    secondsState = seconds?.toString() ?: "",
    caloriesState = calories?.toString() ?: "",
    distanceState = getDistanceForUnit(
        distance = distance,
        distanceUnit = distanceUnit
    ),
    unitState = distanceUnit,
)

private fun getDistanceForUnit(
    distance: Double?,
    distanceUnit: DistanceUnits
) = if (distance != null) {
    if (distanceUnit == DistanceUnits.KILOMETERS) {
        distance.toString()
    } else {
        convertToDistanceUnit(distanceUnit, distance).toString()
    }
} else {
    ""
}