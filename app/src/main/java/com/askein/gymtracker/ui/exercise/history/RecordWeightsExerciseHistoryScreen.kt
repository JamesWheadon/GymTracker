package com.askein.gymtracker.ui.exercise.history

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.askein.gymtracker.R
import com.askein.gymtracker.enums.FormTypes
import com.askein.gymtracker.enums.WeightUnits
import com.askein.gymtracker.enums.convertToKilograms
import com.askein.gymtracker.enums.convertToWeightUnit
import com.askein.gymtracker.ui.DatePickerDialog
import com.askein.gymtracker.ui.DropdownBox
import com.askein.gymtracker.ui.FormInformationField
import com.askein.gymtracker.ui.FormTimeField
import com.askein.gymtracker.ui.customCardElevation
import com.askein.gymtracker.ui.exercise.history.state.ExerciseHistoryUiState
import com.askein.gymtracker.ui.exercise.history.state.WeightsExerciseHistoryUiState
import com.askein.gymtracker.ui.theme.GymTrackerTheme
import com.askein.gymtracker.ui.user.LocalUserPreferences
import com.askein.gymtracker.ui.user.UserPreferencesUiState
import java.time.LocalDate
import kotlin.math.max

@Composable
fun RecordWeightsExerciseHistoryCard(
    exerciseId: Int,
    cardTitle: String,
    saveFunction: (ExerciseHistoryUiState) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    savedHistory: WeightsExerciseHistoryUiState = WeightsExerciseHistoryUiState(),
    recordWeight: Boolean = true
) {
    val userPreferencesUiState = LocalUserPreferences.current
    var recordState by remember {
        mutableStateOf(
            savedHistory.toRecordWeightsHistoryState(
                exerciseId = exerciseId,
                recordWeight = recordWeight,
                weightUnit = userPreferencesUiState.defaultWeightUnit
            )
        )
    }
    Card(
        modifier = modifier
            .padding(vertical = 10.dp, horizontal = 10.dp),
        elevation = customCardElevation()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(0.dp, 12.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = cardTitle
            )
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.Top,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 0.dp)
            ) {
                FormInformationField(
                    label = R.string.sets,
                    value = recordState.setsState,
                    onChange = { sets ->
                        recordState = recordState.copy(setsState = sets)
                        if (sets.isNotEmpty()) {
                            recordState.updateState()
                        }
                    },
                    formType = FormTypes.INTEGER,
                    modifier = Modifier
                        .weight(1f)
                        .padding(0.dp)
                )
                if (recordWeight) {
                    val unitsContentDescription = stringResource(id = R.string.units)
                    DropdownBox(
                        options = WeightUnits.values().associateWith { unit -> unit.shortForm },
                        onChange = { unit ->
                            recordState = recordState.copy(unitState = unit)
                        },
                        modifier = Modifier
                            .weight(1f)
                            .padding(0.dp)
                            .semantics { contentDescription = unitsContentDescription },
                        selected = recordState.unitState
                    )
                }
            }
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.Top,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 0.dp)
            ) {
                Button(
                    onClick = {
                        recordState = recordState.copy(recordReps = true)
                        recordState.updateState()
                    },
                    enabled = !recordState.recordReps
                ) {
                    Text(text = stringResource(id = R.string.reps))
                }
                Button(
                    onClick = {
                        recordState = recordState.copy(recordReps = false)
                        recordState.updateState()
                    },
                    enabled = recordState.recordReps
                ) {
                    Text(text = stringResource(id = R.string.time))
                }
            }
            val numberOfSets =
                max(recordState.repsState.size, recordState.minutesState.size)
            for (set in 0 until numberOfSets) {
                if (recordState.recordReps) {
                    RecordRepsWeightsExercise(
                        weightsHistoryState = recordState,
                        set = set,
                        recordWeight = recordWeight
                    )
                } else {
                    RecordTimeWeightsExercise(
                        weightsHistoryState = recordState,
                        set = set,
                        recordWeight = recordWeight
                    )
                }
                if (set == 0 && numberOfSets > 1) {
                    Row(
                        horizontalArrangement = Arrangement.Absolute.Right,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        Text(text = stringResource(id = R.string.copy_sets))
                        Checkbox(
                            checked = recordState.allSetsEqual(),
                            onCheckedChange = {
                                recordState.allSetsSameAsFirst()
                            }
                        )
                    }
                }
            }
            if (savedHistory.workoutHistoryId == null) {
                DatePickerDialog(
                    date = recordState.dateState,
                    onDateChange = { newDate ->
                        recordState = recordState.copy(dateState = newDate)
                    }
                )
            }
            SaveWeightsExerciseHistoryButton(
                weightsHistoryState = recordState,
                saveFunction = saveFunction,
                onDismiss = onDismiss
            )
        }
    }
}

@Composable
fun RecordRepsWeightsExercise(
    weightsHistoryState: RecordWeightsHistoryState,
    set: Int,
    recordWeight: Boolean,
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.Top,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 0.dp)
    ) {
        FormInformationField(
            label = R.string.reps,
            value = weightsHistoryState.repsState[set],
            onChange = { entry ->
                weightsHistoryState.repsState[set] = entry
            },
            formType = FormTypes.INTEGER,
            modifier = Modifier
                .weight(1f)
                .padding(0.dp)
        )
        if (recordWeight) {
            FormInformationField(
                label = R.string.weight,
                value = weightsHistoryState.weightsState[set],
                onChange = { entry ->
                    weightsHistoryState.weightsState[set] = entry
                },
                formType = FormTypes.DOUBLE,
                modifier = Modifier
                    .weight(1f)
                    .padding(0.dp)
            )
        }
    }
}

@Composable
fun RecordTimeWeightsExercise(
    weightsHistoryState: RecordWeightsHistoryState,
    set: Int,
    recordWeight: Boolean,
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.Top,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 0.dp)
    ) {
        FormTimeField(
            minutes = weightsHistoryState.minutesState[set],
            seconds = weightsHistoryState.secondsState[set],
            minutesOnChange = { entry ->
                weightsHistoryState.minutesState[set] = entry
            },
            secondsOnChange = { entry ->
                weightsHistoryState.secondsState[set] = entry
            }
        )
    }
    if (recordWeight) {
        Row {
            FormInformationField(
                label = R.string.weight,
                value = weightsHistoryState.weightsState[set],
                onChange = { entry ->
                    weightsHistoryState.weightsState[set] = entry
                },
                formType = FormTypes.DOUBLE,
                modifier = Modifier
                    .weight(1f)
                    .padding(0.dp)
            )
        }
    }
}

@Composable
private fun SaveWeightsExerciseHistoryButton(
    weightsHistoryState: RecordWeightsHistoryState,
    saveFunction: (ExerciseHistoryUiState) -> Unit,
    onDismiss: () -> Unit
) {
    Button(
        onClick = {
            saveFunction(weightsHistoryState.toWeightsExerciseHistoryUiState())
            onDismiss()
        },
        enabled = weightsHistoryState.isValid()
    ) {
        Text(text = stringResource(id = R.string.save))
    }
}

data class RecordWeightsHistoryState(
    val historyId: Int,
    val exerciseId: Int,
    val workoutHistoryId: Int?,
    val rest: Int?,
    val setsState: String,
    val repsState: SnapshotStateList<String>,
    val minutesState: SnapshotStateList<String>,
    val secondsState: SnapshotStateList<String>,
    val weightsState: SnapshotStateList<String>,
    val dateState: LocalDate,
    val unitState: WeightUnits,
    val recordReps: Boolean,
    val recordWeight: Boolean
)

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

fun RecordWeightsHistoryState.isValid(): Boolean {
    val validSets = setsState.isNotEmpty() && setsState != "0"
    val validWeights = weightsState.none { it.isEmpty() }
    val validReps = !recordReps || repsState.none { it.isEmpty() }
    val validSeconds = recordReps ||
            minutesState.none { it.isEmpty() } && secondsState.none { it.isEmpty() || it.toInt() >= 60 }
    return validSets && validWeights && validReps && validSeconds
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

fun RecordWeightsHistoryState.toWeightsExerciseHistoryUiState() = WeightsExerciseHistoryUiState(
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

@Preview(showBackground = true)
@Composable
fun RecordExerciseHistoryScreenPreview() {
    val userPreferencesUiState = UserPreferencesUiState()
    CompositionLocalProvider(LocalUserPreferences provides userPreferencesUiState) {
        GymTrackerTheme(darkTheme = false) {
            RecordWeightsExerciseHistoryCard(
                exerciseId = 1,
                cardTitle = "",
                saveFunction = {},
                onDismiss = {},
                savedHistory = WeightsExerciseHistoryUiState(
                    sets = 2,
                    reps = listOf(2, 5),
                    weight = listOf(0.0, 0.0)
                )
            )
        }
    }
}
