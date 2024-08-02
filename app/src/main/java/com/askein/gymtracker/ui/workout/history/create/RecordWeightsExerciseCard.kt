package com.askein.gymtracker.ui.workout.history.create

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.askein.gymtracker.R
import com.askein.gymtracker.enums.FormTypes
import com.askein.gymtracker.enums.WeightUnits
import com.askein.gymtracker.enums.convertToKilograms
import com.askein.gymtracker.enums.convertToWeightUnit
import com.askein.gymtracker.ui.DropdownBox
import com.askein.gymtracker.ui.FormInformationField
import com.askein.gymtracker.ui.exercise.ExerciseUiState
import com.askein.gymtracker.ui.exercise.history.RecordRepsWeightsExercise
import com.askein.gymtracker.ui.exercise.history.RecordTimeWeightsExercise
import com.askein.gymtracker.ui.exercise.history.isValid
import com.askein.gymtracker.ui.exercise.history.state.WeightsExerciseHistoryUiState
import com.askein.gymtracker.ui.exercise.history.toRecordWeightsHistoryState
import com.askein.gymtracker.ui.exercise.history.updateState
import com.askein.gymtracker.ui.user.LocalUserPreferences
import com.askein.gymtracker.ui.user.UserPreferencesUiState

@Composable
fun RecordWeightsExerciseCard(
    exercise: ExerciseUiState,
    exerciseHistory: WeightsExerciseHistoryUiState?,
    selectExerciseFunction: () -> Unit,
    deselectExerciseFunction: () -> Unit,
    errorStateChange: (Int, Boolean) -> Unit,
    recordWeight: Boolean = true
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
                var recordReps by remember { mutableStateOf(true) }
                var setsState by remember { mutableStateOf(exerciseHistory.sets.toString()) }
                val repsState = remember {
                    exerciseHistory.reps?.map { it.toString() }?.toMutableStateList()
                        ?: mutableListOf()
                }
                val weightsState = remember {
                    exerciseHistory.weight.map { value ->
                        getWeightForUnit(value, userPreferencesUiState)
                    }.toMutableStateList()
                }
                var unitState by remember { mutableStateOf(userPreferencesUiState.defaultWeightUnit) }
                var showRepsWeightVisibleState by remember { mutableStateOf(true) }
                var weightsHistoryState by remember {
                    mutableStateOf(
                        exerciseHistory.toRecordWeightsHistoryState(
                            exercise.id,
                            recordWeight,
                            userPreferencesUiState.defaultWeightUnit
                        )
                    )
                }
                val setsError = setsState == "" || setsState.toInt() < 1
                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.Top,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 0.dp)
                ) {
                    FormInformationField(
                        label = R.string.sets,
                        value = setsState,
                        onChange = { sets ->
                            setsState = sets
                            weightsHistoryState = weightsHistoryState.copy(setsState = sets)
                            if (setsState != "") {
                                exerciseHistory.sets = setsState.toInt()
                                weightsHistoryState.updateState()
                            }
                        },
                        formType = FormTypes.INTEGER,
                        error = setsError,
                        errorMessage = R.string.positive_number_error,
                        modifier = Modifier
                            .weight(1f)
                            .padding(0.dp)
                    )
                    if (recordWeight) {
                        Spacer(modifier = Modifier.width(12.dp))
                        val unitsContentDescription = stringResource(id = R.string.units)
                        DropdownBox(
                            options = WeightUnits.values().associateWith { it.shortForm },
                            onChange = { value ->
                                unitState = value
                                exerciseHistory.weight.map {
                                    convertToKilograms(
                                        unitState,
                                        it
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
                }
                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.Top,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 0.dp)
                ) {
                    Button(onClick = { recordReps = true }, enabled = !recordReps) {
                        Text(text = stringResource(id = R.string.reps))
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Button(onClick = { recordReps = false }, enabled = recordReps) {
                        Text(text = stringResource(id = R.string.time))
                    }
                }
                if (setsState != "" && setsState.toInt() > 0) {
                    Row(
                        horizontalArrangement = Arrangement.End,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        IconButton(onClick = {
                            showRepsWeightVisibleState = !showRepsWeightVisibleState
                        }) {
                            Icon(
                                imageVector = if (showRepsWeightVisibleState) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                                contentDescription = ""
                            )
                        }
                    }
                    AnimatedVisibility(visible = showRepsWeightVisibleState) {
                        Column {
                            for (i in 0 until setsState.toInt()) {
                                if (recordReps) {
                                    RecordRepsWeightsExercise(
                                        weightsHistoryState = weightsHistoryState,
                                        set = i,
                                        recordWeight = recordWeight
                                    )
                                } else {
                                    RecordTimeWeightsExercise(
                                        weightsHistoryState = weightsHistoryState,
                                        set = i,
                                        recordWeight = recordWeight
                                    )
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
                                                exerciseHistory.reps =
                                                    repsState.map { it.toInt() }.toList()
                                                exerciseHistory.weight = weightsState.map {
                                                    convertToKilograms(
                                                        unitState,
                                                        it.toDouble()
                                                    )
                                                }.toList()
                                            }
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
                errorStateChange(exercise.id, !weightsHistoryState.isValid())
            } else {
                errorStateChange(exercise.id, false)
            }
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
