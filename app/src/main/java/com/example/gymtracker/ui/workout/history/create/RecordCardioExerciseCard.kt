package com.example.gymtracker.ui.workout.history.create

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.gymtracker.converters.DistanceUnits
import com.example.gymtracker.converters.convertToDistanceUnit
import com.example.gymtracker.converters.convertToKilometers
import com.example.gymtracker.converters.getDistanceUnitFromShortForm
import com.example.gymtracker.ui.DropdownBox
import com.example.gymtracker.ui.FormInformationField
import com.example.gymtracker.ui.FormTimeField
import com.example.gymtracker.ui.exercise.ExerciseUiState
import com.example.gymtracker.ui.exercise.history.state.CardioExerciseHistoryUiState
import com.example.gymtracker.ui.user.LocalUserPreferences
import com.example.gymtracker.ui.user.UserPreferencesUiState

@Composable
fun RecordCardioExerciseCard(
    exercise: ExerciseUiState,
    exerciseHistory: CardioExerciseHistoryUiState?,
    selectExerciseFunction: () -> Unit,
    deselectExerciseFunction: () -> Unit,
    errorStateChange: (Int, Boolean) -> Unit
) {
    var expanded by remember { mutableStateOf(exerciseHistory != null) }
    Card {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
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
                val userPreferences = LocalUserPreferences.current
                var minutesState by remember {
                    mutableStateOf(
                        exerciseHistory.minutes?.toString() ?: ""
                    )
                }
                var secondsState by remember {
                    mutableStateOf(
                        exerciseHistory.seconds?.toString() ?: ""
                    )
                }
                var caloriesState by remember {
                    mutableStateOf(
                        exerciseHistory.calories?.toString() ?: ""
                    )
                }
                var distanceState by remember {
                    mutableStateOf(
                        getDistanceForUnit(exerciseHistory, userPreferences)
                    )
                }
                var unitState by remember { mutableStateOf(userPreferences.defaultDistanceUnit.shortForm) }
                val error = !((minutesState != "" && secondsState != "")
                        || distanceState != ""
                        || caloriesState != "")
                FormTimeField(
                    minutes = minutesState,
                    seconds = secondsState,
                    minutesOnChange = { minutes ->
                        minutesState = Regex("[^0-9]").replace(minutes, "")
                        if (minutesState != "") {
                            exerciseHistory.minutes = minutesState.toInt()
                        }
                    },
                    secondsOnChange = { seconds ->
                        secondsState = Regex("[^0-9]").replace(seconds, "")
                        if (secondsState != "") {
                            exerciseHistory.seconds = secondsState.toInt()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 0.dp)
                )
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.Top,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 0.dp)
                ) {
                    FormInformationField(
                        label = "Distance",
                        value = distanceState,
                        onChange = { entry ->
                            distanceState = Regex("[^0-9.]").replace(entry, "")
                            if (distanceState != "" && distanceState.toDoubleOrNull() != null) {
                                exerciseHistory.distance = convertToKilometers(
                                    getDistanceUnitFromShortForm(unitState),
                                    distanceState.toDouble()
                                )
                            }
                        },
                        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Decimal),
                        modifier = Modifier
                            .weight(1f)
                            .height(intrinsicSize = IntrinsicSize.Max)
                            .padding(0.dp)
                    )
                    DropdownBox(
                        options = listOf(
                            DistanceUnits.METERS.shortForm,
                            DistanceUnits.KILOMETERS.shortForm,
                            DistanceUnits.MILES.shortForm
                        ),
                        onChange = { value ->
                            unitState = value
                        },
                        modifier = Modifier
                            .weight(1f)
                            .padding(0.dp)
                            .height(intrinsicSize = IntrinsicSize.Max)
                            .semantics { contentDescription = "Units" },
                        selected = unitState
                    )
                }
                FormInformationField(
                    label = "Calories",
                    value = caloriesState,
                    onChange = { entry ->
                        caloriesState = Regex("[^0-9]").replace(entry, "")
                        if (caloriesState != "") {
                            exerciseHistory.calories = caloriesState.toInt()
                        }
                    },
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 0.dp)
                )
                if (error) {
                    Text(
                        text = "Must have a time, distance or calories entered",
                        color = MaterialTheme.colorScheme.error
                    )
                }
                errorStateChange(exercise.id, error)
            } else {
                errorStateChange(exercise.id, false)
            }
        }
    }
}

private fun getDistanceForUnit(exerciseHistory: CardioExerciseHistoryUiState, userPreferencesUiState: UserPreferencesUiState) = if (exerciseHistory.distance != null) {
    if (userPreferencesUiState.defaultDistanceUnit == DistanceUnits.KILOMETERS) {
        exerciseHistory.distance.toString()
    } else {
        convertToDistanceUnit(userPreferencesUiState.defaultDistanceUnit, exerciseHistory.distance!!).toString()
    }
} else {
    ""
}
