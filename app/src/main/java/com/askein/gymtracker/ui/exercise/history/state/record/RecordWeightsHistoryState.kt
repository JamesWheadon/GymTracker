package com.askein.gymtracker.ui.exercise.history.state.record

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import com.askein.gymtracker.enums.WeightUnits
import com.askein.gymtracker.enums.convertToKilograms
import com.askein.gymtracker.enums.convertToWeightUnit
import com.askein.gymtracker.ui.exercise.history.state.WeightsExerciseHistoryUiState
import java.time.LocalDate

data class RecordWeightsHistoryState(
    override val historyId: Int,
    override val exerciseId: Int,
    override val workoutHistoryId: Int?,
    override val dateState: LocalDate,
    val rest: Int?,
    val setsState: String,
    val repsState: SnapshotStateList<String>,
    val minutesState: SnapshotStateList<String>,
    val secondsState: SnapshotStateList<String>,
    val weightsState: SnapshotStateList<String>,
    val unitState: WeightUnits,
    val recordReps: Boolean,
    val recordWeight: Boolean
) : RecordExerciseHistoryState {
    override fun isValid(): Boolean {
        val validSets = setsState.isNotEmpty() && setsState != "0"
        val validWeights = weightsState.none { it.isEmpty() }
        val validReps = !recordReps || repsState.none { it.isEmpty() }
        val validSeconds = recordReps ||
                minutesState.none { it.isEmpty() } && secondsState.none { it.isEmpty() || it.toInt() >= 60 }
        return validSets && validWeights && validReps && validSeconds
    }

    override fun toHistoryUiState() = WeightsExerciseHistoryUiState(
        id = historyId,
        exerciseId = exerciseId,
        workoutHistoryId = workoutHistoryId,
        date = dateState,
        rest = rest,
        sets = setsState.toInt(),
        reps = if (recordReps) repsState.map { it.toInt() } else null,
        seconds = if (recordReps) null else minutesState.zip(secondsState)
            .map { it.first.toInt() * 60 + it.second.toInt() },
        weight = weightsState.map { convertToKilograms(unitState, it.toDouble()) }
    )

    override fun toHistoryUiState(date: LocalDate) = WeightsExerciseHistoryUiState(
        id = historyId,
        exerciseId = exerciseId,
        workoutHistoryId = workoutHistoryId,
        date = date,
        rest = rest,
        sets = setsState.toInt(),
        reps = if (recordReps) repsState.map { it.toInt() } else null,
        seconds = if (recordReps) null else minutesState.zip(secondsState)
            .map { it.first.toInt() * 60 + it.second.toInt() },
        weight = weightsState.map { convertToKilograms(unitState, it.toDouble()) }
    )
}

fun RecordWeightsHistoryState.updateState() {
    if (recordReps) {
        minutesState.clear()
        secondsState.clear()
        while (repsState.size > setsState.toInt()) {
            repsState.removeLast()
            if (recordWeight) {
                weightsState.removeLast()
            }
        }
        while (repsState.size < setsState.toInt()) {
            repsState.add("0")
            if (recordWeight) {
                weightsState.add("0.0")
            }
        }
    } else {
        repsState.clear()
        while (minutesState.size > setsState.toInt()) {
            minutesState.removeLast()
            secondsState.removeLast()
            if (recordWeight) {
                weightsState.removeLast()
            }
        }
        while (minutesState.size < setsState.toInt()) {
            minutesState.add("0")
            secondsState.add("0")
            if (recordWeight) {
                weightsState.add("0.0")
            }
        }
    }
}

fun RecordWeightsHistoryState.allSetsSameAsFirst() {
    for (j in 1 until setsState.toInt()) {
        if (recordReps) {
            repsState[j] = repsState[0]
        } else {
            minutesState[j] = minutesState[0]
            secondsState[j] = secondsState[0]
        }
        if (recordWeight) {
            weightsState[j] = weightsState[0]
        }
    }
}

fun RecordWeightsHistoryState.allSetsEqual(): Boolean {
    val allWeightsSame = weightsState.isEmpty() || weightsState.distinct().size == 1
    return if (recordReps) {
        repsState.distinct().size == 1 && allWeightsSame
    } else {
        minutesState.distinct().size == 1 && secondsState.distinct().size == 1 && allWeightsSame
    }
}

fun WeightsExerciseHistoryUiState.toRecordWeightsHistoryState(
    exerciseId: Int,
    recordWeight: Boolean,
    weightUnit: WeightUnits
) = RecordWeightsHistoryState(
    historyId = id,
    exerciseId = exerciseId,
    workoutHistoryId = workoutHistoryId,
    dateState = date,
    rest = rest,
    setsState = sets.toString(),
    repsState = reps?.map { it.toString() }?.toMutableStateList() ?: mutableStateListOf(),
    minutesState = seconds?.map { (it / 60).toString() }?.toMutableStateList()
        ?: mutableStateListOf(),
    secondsState = seconds?.map { (it % 60).toString() }?.toMutableStateList()
        ?: mutableStateListOf(),
    weightsState = weight.map { getWeightForUnit(it, weightUnit) }.toMutableStateList(),
    unitState = weightUnit,
    recordReps = seconds.isNullOrEmpty(),
    recordWeight = recordWeight
)

private fun getWeightForUnit(
    weight: Double,
    weightUnit: WeightUnits
): String = if (weightUnit == WeightUnits.KILOGRAMS) {
    weight.toString()
} else {
    convertToWeightUnit(weightUnit, weight).toString()
}