package com.example.gymtracker.ui.workout.history.create.live

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.gymtracker.converters.DistanceUnits
import com.example.gymtracker.converters.convertToKilometers
import com.example.gymtracker.converters.getDistanceUnitFromShortForm
import com.example.gymtracker.ui.DropdownBox
import com.example.gymtracker.ui.FormInformationField
import com.example.gymtracker.ui.FormTimeField
import com.example.gymtracker.ui.customCardElevation
import com.example.gymtracker.ui.exercise.ExerciseUiState
import com.example.gymtracker.ui.exercise.history.state.CardioExerciseHistoryUiState
import com.example.gymtracker.ui.theme.GymTrackerTheme


@Composable
fun LiveRecordCardioExercise(
    uiState: ExerciseUiState,
    exerciseComplete: (CardioExerciseHistoryUiState) -> Unit,
    exerciseCancel: () -> Unit,
    modifier: Modifier = Modifier
) {
    var minutesState by remember { mutableStateOf("") }
    var secondsState by remember { mutableStateOf("") }
    var caloriesState by remember { mutableStateOf("") }
    var distanceState by remember { mutableStateOf("") }
    var unitState by remember { mutableStateOf(DistanceUnits.KILOMETERS.shortForm) }
    val saveEnabled = (minutesState != "" && secondsState != "") ||
            caloriesState != "" || distanceState != ""
    Card(
        elevation = customCardElevation(),
        modifier = modifier
    ) {
        Column(
            modifier = modifier
                .padding(0.dp, 12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = uiState.name)
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
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround,
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(
                    enabled = saveEnabled,
                    onClick = {
                        val distance = if (distanceState == "") {
                            null
                        } else {
                            val unit = getDistanceUnitFromShortForm(unitState)
                            convertToKilometers(unit, distanceState.toDouble())
                        }
                        exerciseComplete(
                            CardioExerciseHistoryUiState(
                                exerciseId = uiState.id,
                                distance = distance,
                                minutes = minutesState.toIntOrNull(),
                                seconds = secondsState.toIntOrNull(),
                                calories = caloriesState.toIntOrNull()
                            )
                        )
                    }
                ) {
                    Text(text = "Save")
                }
                Button(
                    onClick = {
                        exerciseCancel()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Text(text = "Cancel")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LiveRecordCardioExercisePreview() {
    GymTrackerTheme(darkTheme = false) {
        LiveRecordCardioExercise(
            uiState = ExerciseUiState(name = "Treadmill"),
            exerciseCancel = {},
            exerciseComplete = {}
        )
    }
}
