package com.example.gymtracker.ui.exercise.history

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
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
import androidx.compose.ui.unit.dp
import com.example.gymtracker.converters.DistanceUnits
import com.example.gymtracker.converters.convertToKilometers
import com.example.gymtracker.converters.getDistanceUnitFromShortForm
import com.example.gymtracker.ui.DropdownBox
import com.example.gymtracker.ui.FormInformationField
import com.example.gymtracker.ui.FormTimeField
import com.example.gymtracker.ui.customCardElevation
import com.example.gymtracker.ui.exercise.history.state.CardioExerciseHistoryUiState
import com.example.gymtracker.ui.exercise.history.state.ExerciseHistoryUiState


@Composable
fun RecordCardioExerciseHistoryCard(
    exerciseId: Int,
    cardTitle: String,
    saveFunction: (ExerciseHistoryUiState) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    savedHistory: CardioExerciseHistoryUiState = CardioExerciseHistoryUiState()
) {
    var minutesState by remember { mutableStateOf(savedHistory.minutes?.toString() ?: "") }
    var secondsState by remember { mutableStateOf(savedHistory.seconds?.toString() ?: "") }
    var caloriesState by remember { mutableStateOf(savedHistory.calories?.toString() ?: "") }
    var distanceState by remember { mutableStateOf(savedHistory.distance?.toString() ?: "") }
    var unitState by remember { mutableStateOf(DistanceUnits.KILOMETERS.shortForm) }
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
            FormTimeField(
                minutes = minutesState,
                seconds = secondsState,
                minutesOnChange = { minutes ->
                    minutesState = Regex("[^0-9]").replace(minutes, "")
                },
                secondsOnChange = { seconds ->
                    secondsState = Regex("[^0-9]").replace(seconds, "")
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 0.dp)
            )
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.Top,
                modifier = modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 0.dp)
            ) {
                FormInformationField(
                    label = "Distance",
                    value = distanceState,
                    onChange = { entry ->
                        distanceState = Regex("[^0-9.]").replace(entry, "")
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
                        .semantics { contentDescription = "Units" }
                )
            }
            FormInformationField(
                label = "Calories",
                value = caloriesState,
                onChange = { entry ->
                    caloriesState = Regex("[^0-9]").replace(entry, "")
                },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 0.dp)
            )
            SaveCardioExerciseHistoryButton(
                minutesState = minutesState,
                secondsState = secondsState,
                caloriesState = caloriesState,
                distanceState = distanceState,
                unitState = unitState,
                exerciseId = exerciseId,
                savedHistory = savedHistory,
                saveFunction = saveFunction,
                onDismiss = onDismiss
            )
        }
    }
}

@Composable
fun SaveCardioExerciseHistoryButton(
    minutesState: String,
    secondsState: String,
    caloriesState: String,
    distanceState: String,
    unitState: String,
    exerciseId: Int,
    savedHistory: CardioExerciseHistoryUiState,
    saveFunction: (ExerciseHistoryUiState) -> Unit,
    onDismiss: () -> Unit
) {
    if ((minutesState != "" && secondsState != "" && secondsState.toInt() < 60) || caloriesState != "" || distanceState != "") {
        Button(onClick = {
            val distance = if (distanceState == "") {
                null
            } else {
                val unit = getDistanceUnitFromShortForm(unitState)
                convertToKilometers(unit, distanceState.toDouble())
            }
            val minutes = minutesState.toIntOrNull()
            val seconds = secondsState.toIntOrNull()
            val history = if (savedHistory == CardioExerciseHistoryUiState()) {
                CardioExerciseHistoryUiState(
                    exerciseId = exerciseId,
                    distance = distance,
                    minutes = minutes,
                    seconds = seconds,
                    calories = caloriesState.toIntOrNull()
                )
            } else {
                savedHistory.distance = distance
                savedHistory.minutes = minutes
                savedHistory.seconds = seconds
                savedHistory.calories = caloriesState.toIntOrNull()
                savedHistory
            }
            saveFunction(history)
            onDismiss()
        }) {
            Text("Save")
        }
    } else {
        Button(
            onClick = { },
            enabled = false
        ) {
            Text("Save")
        }
    }
}
