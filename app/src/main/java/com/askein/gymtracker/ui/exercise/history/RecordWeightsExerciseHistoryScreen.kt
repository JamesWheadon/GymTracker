package com.askein.gymtracker.ui.exercise.history

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.askein.gymtracker.R
import com.askein.gymtracker.data.exerciseHistory.weights.WeightsExerciseHistory
import com.askein.gymtracker.enums.FormTypes
import com.askein.gymtracker.enums.WeightUnits
import com.askein.gymtracker.enums.convertToKilograms
import com.askein.gymtracker.enums.convertToWeightUnit
import com.askein.gymtracker.ui.DatePickerDialog
import com.askein.gymtracker.ui.DropdownBox
import com.askein.gymtracker.ui.FormInformationField
import com.askein.gymtracker.ui.customCardElevation
import com.askein.gymtracker.ui.exercise.history.state.ExerciseHistoryUiState
import com.askein.gymtracker.ui.exercise.history.state.WeightsExerciseHistoryUiState
import com.askein.gymtracker.ui.exercise.history.state.toWeightsExerciseHistory
import com.askein.gymtracker.ui.exercise.history.state.toWeightsExerciseHistoryUiState
import com.askein.gymtracker.ui.theme.GymTrackerTheme
import com.askein.gymtracker.ui.user.LocalUserPreferences
import com.askein.gymtracker.ui.user.UserPreferencesUiState
import java.time.LocalDate

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
    var setsState by remember { mutableStateOf(savedHistory.sets.toString()) }
    val repsState = remember { savedHistory.reps.map { it.toString() }.toMutableStateList() }
    val weightsState = remember {
        savedHistory.weight.map { value ->
            getWeightForUnit(value, userPreferencesUiState)
        }.toMutableStateList()
    }
    var date by remember { mutableStateOf(savedHistory.date) }
    var unitState by remember { mutableStateOf(userPreferencesUiState.defaultWeightUnit) }
    Card(
        modifier = modifier
            .padding(vertical = 10.dp, horizontal = 10.dp),
        elevation = customCardElevation()
    ) {
        Column(
            modifier = modifier
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
                modifier = modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 0.dp)
            ) {
                FormInformationField(
                    label = R.string.sets,
                    value = setsState,
                    onChange = { entry ->
                        setsState = entry
                    },
                    formType = FormTypes.INTEGER,
                    modifier = Modifier
                        .weight(1f)
                        .padding(0.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                if (recordWeight) {
                    val unitsContentDescription = stringResource(id = R.string.units)
                    DropdownBox(
                        options = WeightUnits.values().associateWith { unit -> unit.shortForm },
                        onChange = { value ->
                            unitState = value
                        },
                        modifier = Modifier
                            .weight(1f)
                            .padding(0.dp)
                            .semantics { contentDescription = unitsContentDescription },
                        selected = unitState
                    )
                }
            }
            if (setsState != "") {
                for (i in 0 until setsState.toInt()) {
                    if (repsState.size <= i) {
                        repsState.add("0")
                        weightsState.add("0.0")
                    }
                    Row(
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.Top,
                        modifier = modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp, vertical = 0.dp)
                    ) {
                        FormInformationField(
                            label = R.string.reps,
                            value = repsState[i],
                            onChange = { entry ->
                                repsState[i] = entry
                            },
                            formType = FormTypes.INTEGER,
                            modifier = Modifier
                                .weight(1f)
                                .padding(0.dp)
                        )
                        if (recordWeight) {
                            Spacer(modifier = Modifier.width(12.dp))
                            FormInformationField(
                                label = R.string.weight,
                                value = weightsState[i],
                                onChange = { entry ->
                                    weightsState[i] = entry
                                },
                                formType = FormTypes.DOUBLE,
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(0.dp)
                            )
                        }
                    }
                    if (i == 0 && setsState.toInt() > 1) {
                        Row(
                            horizontalArrangement = Arrangement.Absolute.Right,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            Text(text = stringResource(id = R.string.copy_sets))
                            Checkbox(
                                checked = repsState.distinct().size == 1 && weightsState.distinct().size == 1,
                                onCheckedChange = {
                                    for (j in 1 until setsState.toInt()) {
                                        repsState[j] = repsState[0]
                                        weightsState[j] = weightsState[0]
                                    }
                                }
                            )
                        }
                    }
                }
            }
            if (savedHistory.workoutHistoryId == null) {
                DatePickerDialog(
                    date = date,
                    onDateChange = { newDate -> date = newDate }
                )
            }
            SaveWeightsExerciseHistoryButton(
                setsState = setsState,
                repsState = repsState,
                weightsState = weightsState,
                unitState = unitState,
                dateState = date,
                recordWeight = recordWeight,
                exerciseId = exerciseId,
                savedHistory = savedHistory,
                saveFunction = saveFunction,
                onDismiss = onDismiss
            )
        }
    }
}

@Composable
private fun SaveWeightsExerciseHistoryButton(
    setsState: String,
    repsState: List<String>,
    weightsState: List<String>,
    unitState: WeightUnits,
    dateState: LocalDate,
    recordWeight: Boolean,
    exerciseId: Int,
    savedHistory: WeightsExerciseHistoryUiState,
    saveFunction: (ExerciseHistoryUiState) -> Unit,
    onDismiss: () -> Unit
) {
    if (setsState != "" && setsState != "0" && !repsState.contains("") && !repsState.contains("0") && (!weightsState.contains("") || !recordWeight)) {
        val weight = if (recordWeight) {
            weightsState.map { convertToKilograms(unitState, it.toDouble()) }
                .subList(0, setsState.toInt())
        } else {
            emptyList()
        }
        val reps = repsState.map { it.toInt() }.subList(0, setsState.toInt())
        val history = if (savedHistory == WeightsExerciseHistoryUiState()) {
            WeightsExerciseHistory(
                exerciseId = exerciseId,
                weight = weight,
                sets = setsState.toInt(),
                reps = reps,
                date = dateState
            )
        } else {
            val tempHistory = savedHistory.copy(
                sets = setsState.toInt(),
                reps = reps,
                weight = weight,
                date = dateState
            )
            tempHistory.toWeightsExerciseHistory(exerciseId)
        }
        Button(onClick = {
            saveFunction(history.toWeightsExerciseHistoryUiState())
            onDismiss()
        }) {
            Text(text = stringResource(id = R.string.save))
        }
    } else {
        Button(
            onClick = { },
            enabled = false
        ) {
            Text(text = stringResource(id = R.string.save))
        }
    }
}

private fun getWeightForUnit(
    weight: Double,
    userPreferencesUiState: UserPreferencesUiState
): String =
    if (userPreferencesUiState.defaultWeightUnit == WeightUnits.KILOGRAMS) {
        weight.toString()
    } else {
        convertToWeightUnit(
            userPreferencesUiState.defaultWeightUnit,
            weight
        ).toString()
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
                savedHistory = WeightsExerciseHistoryUiState(sets = 2, reps = listOf(2, 5), weight = listOf(0.0, 0.0))
            )
        }
    }
}
