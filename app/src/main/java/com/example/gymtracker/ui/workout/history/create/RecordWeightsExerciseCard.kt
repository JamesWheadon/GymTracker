package com.example.gymtracker.ui.workout.history.create

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.gymtracker.R
import com.example.gymtracker.enums.FormTypes
import com.example.gymtracker.enums.WeightUnits
import com.example.gymtracker.enums.convertToKilograms
import com.example.gymtracker.enums.convertToWeightUnit
import com.example.gymtracker.ui.DropdownBox
import com.example.gymtracker.ui.FormInformationField
import com.example.gymtracker.ui.exercise.ExerciseUiState
import com.example.gymtracker.ui.exercise.history.state.WeightsExerciseHistoryUiState
import com.example.gymtracker.ui.user.LocalUserPreferences
import com.example.gymtracker.ui.user.UserPreferencesUiState

@Composable
fun RecordWeightsExerciseCard(
    exercise: ExerciseUiState,
    exerciseHistory: WeightsExerciseHistoryUiState?,
    selectExerciseFunction: () -> Unit,
    deselectExerciseFunction: () -> Unit,
    errorStateChange: (Int, Boolean) -> Unit
) {
    var expanded by remember { mutableStateOf(exerciseHistory != null) }
    Card {
        Column {
            Box(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = exercise.name,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .align(Alignment.Center),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Checkbox(
                    checked = expanded,
                    onCheckedChange = {
                        if (expanded) {
                            deselectExerciseFunction()
                        } else {
                            selectExerciseFunction()
                        }
                        expanded = !expanded
                    },
                    modifier = Modifier.align(Alignment.CenterEnd)
                )
            }
            if (exerciseHistory != null) {
                val userPreferencesUiState = LocalUserPreferences.current
                var setsState by remember { mutableStateOf(exerciseHistory.sets.toString()) }
                var repsState by remember { mutableStateOf(exerciseHistory.reps.toString()) }
                var weightState by remember {
                    mutableStateOf(
                        getWeightForUnit(
                            exerciseHistory,
                            userPreferencesUiState
                        )
                    )
                }
                var unitState by remember { mutableStateOf(userPreferencesUiState.defaultWeightUnit) }
                val setsError = setsState == "" || setsState.toInt() < 1
                val repsError = repsState == "" || repsState.toInt() < 1
                val weightError = weightState == "" || weightState.toDoubleOrNull() == null
                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 0.dp)
                ) {
                    FormInformationField(
                        label = R.string.sets,
                        value = setsState,
                        onChange = { sets ->
                            setsState = sets
                            if (setsState != "") {
                                exerciseHistory.sets = setsState.toInt()
                            }
                        },
                        formType = FormTypes.INTEGER,
                        error = setsError,
                        errorMessage = R.string.positive_number_error,
                        modifier = Modifier
                            .weight(1f)
                            .padding(0.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    FormInformationField(
                        label = R.string.reps,
                        value = repsState,
                        onChange = { reps ->
                            repsState = reps
                            if (repsState != "") {
                                exerciseHistory.reps = repsState.toInt()
                            }
                        },
                        formType = FormTypes.INTEGER,
                        error = repsError,
                        errorMessage = R.string.positive_number_error,
                        modifier = Modifier
                            .weight(1f)
                            .padding(0.dp)
                    )
                }
                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.Top,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 0.dp)
                ) {
                    FormInformationField(
                        label = R.string.weight,
                        value = weightState,
                        onChange = { weight ->
                            weightState = weight
                            if (weightState != "" && weightState.toDoubleOrNull() != null) {
                                exerciseHistory.weight = convertToKilograms(
                                    unitState,
                                    weightState.toDouble()
                                )
                            }
                        },
                        formType = FormTypes.DOUBLE,
                        error = weightError,
                        errorMessage = R.string.number_error,
                        modifier = Modifier
                            .weight(1f)
                            .padding(0.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    val unitsContentDescription = stringResource(id = R.string.units)
                    DropdownBox(
                        options = WeightUnits.values().associateWith { it.shortForm },
                        onChange = { value ->
                            unitState = value
                            if (weightState != "") {
                                exerciseHistory.weight = convertToKilograms(
                                    unitState,
                                    weightState.toDouble()
                                )
                            }
                        },
                        modifier = Modifier
                            .weight(1f)
                            .padding(0.dp)
                            .semantics { contentDescription = unitsContentDescription },
                        selected = unitState
                    )
                }
                errorStateChange(exercise.id, setsError || repsError || weightError)
            } else {
                errorStateChange(exercise.id, false)
            }
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
