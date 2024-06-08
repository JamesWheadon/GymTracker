package com.askein.gymtracker.ui.exercise.history

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Card
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
    var setsState by remember { mutableStateOf(if (savedHistory == WeightsExerciseHistoryUiState()) "" else savedHistory.sets.toString()) }
    var repsState by remember { mutableStateOf(if (savedHistory == WeightsExerciseHistoryUiState()) "" else savedHistory.reps.toString()) }
    var weightState by remember {
        mutableStateOf(
            if (savedHistory == WeightsExerciseHistoryUiState()) "" else getWeightForUnit(
                savedHistory,
                userPreferencesUiState
            )
        )
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
                .padding(0.dp, 12.dp),
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
                FormInformationField(
                    label = R.string.reps,
                    value = repsState,
                    onChange = { entry ->
                        repsState = entry
                    },
                    formType = FormTypes.INTEGER,
                    modifier = Modifier
                        .weight(1f)
                        .padding(0.dp)
                )
            }
            if (recordWeight) {
                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.Top,
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 0.dp)
                ) {
                    FormInformationField(
                        label = R.string.weight,
                        value = weightState,
                        onChange = { entry ->
                            weightState = entry
                        },
                        formType = FormTypes.DOUBLE,
                        modifier = Modifier
                            .weight(1f)
                            .padding(0.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
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
            if (savedHistory.workoutHistoryId == null) {
                DatePickerDialog(
                    date = date,
                    onDateChange = { newDate -> date = newDate }
                )
            }
            SaveWeightsExerciseHistoryButton(
                setsState = setsState,
                repsState = repsState,
                weightState = weightState,
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
    repsState: String,
    weightState: String,
    unitState: WeightUnits,
    dateState: LocalDate,
    recordWeight: Boolean,
    exerciseId: Int,
    savedHistory: WeightsExerciseHistoryUiState,
    saveFunction: (ExerciseHistoryUiState) -> Unit,
    onDismiss: () -> Unit
) {
    if (setsState != "" && repsState != "" && (weightState != "" || !recordWeight)) {
        val weight = if (recordWeight) {
            weightState.toDouble()
        } else {
            0.0
        }
        val history = if (savedHistory == WeightsExerciseHistoryUiState()) {
            WeightsExerciseHistory(
                exerciseId = exerciseId,
                weight = convertToKilograms(unitState, weight),
                sets = setsState.toInt(),
                reps = repsState.toInt(),
                date = dateState
            )
        } else {
            val tempHistory = savedHistory.copy(
                sets = setsState.toInt(),
                reps = repsState.toInt(),
                weight = convertToKilograms(unitState, weight),
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
    exerciseHistory: WeightsExerciseHistoryUiState,
    userPreferencesUiState: UserPreferencesUiState
) =
    if (userPreferencesUiState.defaultWeightUnit == WeightUnits.KILOGRAMS) {
        exerciseHistory.weight.toString()
    } else {
        convertToWeightUnit(
            userPreferencesUiState.defaultWeightUnit,
            exerciseHistory.weight
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
                onDismiss = {}
            )
        }
    }
}
