package com.askein.gymtracker.ui.exercise.history

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.askein.gymtracker.ui.DatePickerDialog
import com.askein.gymtracker.ui.DropdownBox
import com.askein.gymtracker.ui.FormInformationField
import com.askein.gymtracker.ui.FormTimeField
import com.askein.gymtracker.ui.exercise.history.state.ExerciseHistoryUiState
import com.askein.gymtracker.ui.exercise.history.state.WeightsExerciseHistoryUiState
import com.askein.gymtracker.ui.exercise.history.state.record.RecordWeightsHistoryState
import com.askein.gymtracker.ui.exercise.history.state.record.allSetsEqual
import com.askein.gymtracker.ui.exercise.history.state.record.allSetsSameAsFirst
import com.askein.gymtracker.ui.exercise.history.state.record.toRecordWeightsHistoryState
import com.askein.gymtracker.ui.exercise.history.state.record.updateState
import com.askein.gymtracker.ui.theme.GymTrackerTheme
import com.askein.gymtracker.ui.user.LocalUserPreferences
import com.askein.gymtracker.ui.user.UserPreferencesUiState
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

    var recordWeightsHistory by remember {
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
        elevation = CardDefaults.cardElevation(
            defaultElevation = 16.dp
        )
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
            SelectsSetsAndUnits(
                recordWeightsHistory = recordWeightsHistory,
                recordWeightsHistoryOnChange = { newState -> recordWeightsHistory = newState }
            )
            RepsOrTimeSelector(
                recordWeightsHistory = recordWeightsHistory,
                recordWeightsHistoryOnChange = { newState -> recordWeightsHistory = newState }
            )
            InputSetDetails(recordWeightsHistory = recordWeightsHistory)
            if (recordWeightsHistory.workoutHistoryId == null) {
                DatePickerDialog(
                    date = recordWeightsHistory.dateState,
                    onDateChange = { newDate ->
                        recordWeightsHistory = recordWeightsHistory.copy(dateState = newDate)
                    }
                )
            }
            SaveWeightsExerciseHistoryButton(
                recordWeightsHistory = recordWeightsHistory,
                saveFunction = saveFunction,
                onDismiss = onDismiss
            )
        }
    }
}

@Composable
fun SelectsSetsAndUnits(
    recordWeightsHistory: RecordWeightsHistoryState,
    recordWeightsHistoryOnChange: (RecordWeightsHistoryState) -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.Top,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 0.dp)
    ) {
        FormInformationField(
            label = R.string.sets,
            value = recordWeightsHistory.setsState,
            onChange = { sets ->
                val tempState = recordWeightsHistory.copy(setsState = sets)
                if (sets.isNotEmpty()) {
                    tempState.updateState()
                }
                recordWeightsHistoryOnChange(tempState)
            },
            formType = FormTypes.INTEGER,
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
        )
        if (recordWeightsHistory.recordWeight) {
            val unitsContentDescription = stringResource(id = R.string.units)
            DropdownBox(
                options = WeightUnits.values().associateWith { unit -> unit.shortForm },
                onChange = { unit ->
                    recordWeightsHistoryOnChange(recordWeightsHistory.copy(unitState = unit))
                },
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .semantics { contentDescription = unitsContentDescription },
                selected = recordWeightsHistory.unitState
            )
        }
    }
}

@Composable
fun RepsOrTimeSelector(
    recordWeightsHistory: RecordWeightsHistoryState,
    recordWeightsHistoryOnChange: (RecordWeightsHistoryState) -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.Top,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 0.dp)
    ) {
        Button(
            onClick = {
                val tempState = recordWeightsHistory.copy(recordReps = true)
                tempState.updateState()
                recordWeightsHistoryOnChange(tempState)
            },
            enabled = !recordWeightsHistory.recordReps
        ) {
            Text(text = stringResource(id = R.string.reps))
        }
        Button(
            onClick = {
                val tempState = recordWeightsHistory.copy(recordReps = false)
                tempState.updateState()
                recordWeightsHistoryOnChange(tempState)
            },
            enabled = recordWeightsHistory.recordReps
        ) {
            Text(text = stringResource(id = R.string.time))
        }
    }
}

@Composable
fun InputSetDetails(
    recordWeightsHistory: RecordWeightsHistoryState
) {
    val numberOfSets =
        max(recordWeightsHistory.repsState.size, recordWeightsHistory.minutesState.size)
    for (set in 0 until numberOfSets) {
        if (recordWeightsHistory.recordReps) {
            RecordRepsWeightsExercise(
                recordWeightsHistory = recordWeightsHistory,
                set = set
            )
        } else {
            RecordTimeWeightsExercise(
                recordWeightsHistory = recordWeightsHistory,
                set = set
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
                    checked = recordWeightsHistory.allSetsEqual(),
                    onCheckedChange = { checked ->
                        if (checked) {
                            recordWeightsHistory.allSetsSameAsFirst()
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun RecordRepsWeightsExercise(
    recordWeightsHistory: RecordWeightsHistoryState,
    set: Int,
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.Top,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 0.dp)
    ) {
        FormInformationField(
            label = R.string.reps,
            value = recordWeightsHistory.repsState[set],
            onChange = { entry ->
                recordWeightsHistory.repsState[set] = entry
            },
            formType = FormTypes.INTEGER,
            modifier = Modifier
                .weight(1f)
        )
        if (recordWeightsHistory.recordWeight) {
            FormInformationField(
                label = R.string.weight,
                value = recordWeightsHistory.weightsState[set],
                onChange = { entry ->
                    recordWeightsHistory.weightsState[set] = entry
                },
                formType = FormTypes.DOUBLE,
                modifier = Modifier
                    .weight(1f)
            )
        }
    }
}

@Composable
fun RecordTimeWeightsExercise(
    recordWeightsHistory: RecordWeightsHistoryState,
    set: Int,
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
            minutes = recordWeightsHistory.minutesState[set],
            seconds = recordWeightsHistory.secondsState[set],
            minutesOnChange = { entry ->
                recordWeightsHistory.minutesState[set] = entry
            },
            secondsOnChange = { entry ->
                recordWeightsHistory.secondsState[set] = entry
            }
        )
    }
    if (recordWeightsHistory.recordWeight) {
        FormInformationField(
            label = R.string.weight,
            value = recordWeightsHistory.weightsState[set],
            onChange = { entry ->
                recordWeightsHistory.weightsState[set] = entry
            },
            formType = FormTypes.DOUBLE,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 0.dp)
        )
    }
}

@Composable
private fun SaveWeightsExerciseHistoryButton(
    recordWeightsHistory: RecordWeightsHistoryState,
    saveFunction: (ExerciseHistoryUiState) -> Unit,
    onDismiss: () -> Unit
) {
    Button(
        onClick = {
            saveFunction(recordWeightsHistory.toHistoryUiState())
            onDismiss()
        },
        enabled = recordWeightsHistory.isValid()
    ) {
        Text(text = stringResource(id = R.string.save))
    }
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
