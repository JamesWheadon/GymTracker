package com.askein.gymtracker.ui.workout.history.create.live

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.askein.gymtracker.R
import com.askein.gymtracker.enums.DistanceUnits
import com.askein.gymtracker.enums.FormTypes
import com.askein.gymtracker.enums.convertToKilometers
import com.askein.gymtracker.ui.DropdownBox
import com.askein.gymtracker.ui.FormInformationField
import com.askein.gymtracker.ui.FormTimeField
import com.askein.gymtracker.ui.customCardElevation
import com.askein.gymtracker.ui.exercise.ExerciseUiState
import com.askein.gymtracker.ui.exercise.history.state.CardioExerciseHistoryUiState
import com.askein.gymtracker.ui.theme.GymTrackerTheme
import com.askein.gymtracker.ui.user.LocalUserPreferences

@Composable
fun LiveRecordCardioExercise(
    uiState: ExerciseUiState,
    exerciseComplete: (CardioExerciseHistoryUiState) -> Unit,
    exerciseCancel: () -> Unit,
    modifier: Modifier = Modifier
) {
    val userPreferencesUiState = LocalUserPreferences.current
    var minutesState by remember { mutableStateOf("") }
    var secondsState by remember { mutableStateOf("") }
    var caloriesState by remember { mutableStateOf("") }
    var distanceState by remember { mutableStateOf("") }
    var unitState by remember { mutableStateOf(userPreferencesUiState.defaultDistanceUnit) }
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
                    minutesState = minutes
                },
                secondsOnChange = { seconds ->
                    secondsState = seconds
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
                    label = R.string.distance,
                    value = distanceState,
                    onChange = { distance ->
                        distanceState = distance
                    },
                    formType = FormTypes.DOUBLE,
                    modifier = Modifier
                        .weight(1f)
                        .height(intrinsicSize = IntrinsicSize.Max)
                        .padding(0.dp)
                )
                val unitsContentDescription = stringResource(id = R.string.units)
                DropdownBox(
                    options = DistanceUnits.values().associateWith { unit -> unit.shortForm },
                    onChange = { value ->
                        unitState = value
                    },
                    modifier = Modifier
                        .weight(1f)
                        .padding(0.dp)
                        .height(intrinsicSize = IntrinsicSize.Max)
                        .semantics { contentDescription = unitsContentDescription },
                    selected = unitState
                )
            }
            FormInformationField(
                label = R.string.calories,
                value = caloriesState,
                onChange = { calories ->
                    caloriesState = calories
                },
                formType = FormTypes.INTEGER,
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
                            convertToKilometers(unitState, distanceState.toDouble())
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
                    Text(text = stringResource(id = R.string.save))
                }
                Button(
                    onClick = {
                        exerciseCancel()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Text(text = stringResource(id = R.string.cancel))
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
