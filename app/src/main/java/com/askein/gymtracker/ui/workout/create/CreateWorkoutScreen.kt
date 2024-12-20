package com.askein.gymtracker.ui.workout.create

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.askein.gymtracker.R
import com.askein.gymtracker.enums.FormTypes
import com.askein.gymtracker.ui.FormInformationField
import com.askein.gymtracker.ui.theme.GymTrackerTheme
import com.askein.gymtracker.ui.workout.WorkoutUiState

@Composable
fun CreateWorkoutForm(
    screenTitle: String,
    saveFunction: (WorkoutUiState) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    workout: WorkoutUiState = WorkoutUiState()
) {
    var nameState by remember { mutableStateOf(workout.name) }
    Box {
        Card(
            modifier = modifier
                .padding(vertical = 10.dp, horizontal = 10.dp),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 16.dp
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = screenTitle,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.headlineLarge,
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.CenterHorizontally)
                        .padding(vertical = 16.dp, horizontal = 16.dp)
                )
                Column(
                    modifier = modifier
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    FormInformationField(
                        label = R.string.workout_name,
                        value = nameState,
                        onChange = { entry ->
                            nameState = entry
                        },
                        formType = FormTypes.STRING,
                    )
                    SaveWorkoutFormButton(
                        workoutName = nameState,
                        saveFunction = saveFunction,
                        closeForm = onDismiss,
                        existingWorkout = workout
                    )
                }
            }
        }
        IconButton(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .offset((-8).dp, 8.dp),
            onClick = { onDismiss() }
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = stringResource(id = R.string.close)
            )
        }
    }
}

@Composable
fun SaveWorkoutFormButton(
    workoutName: String,
    saveFunction: (WorkoutUiState) -> Unit,
    closeForm: () -> Unit,
    existingWorkout: WorkoutUiState
) {
    Button(
        onClick = {
            saveFunction(
                WorkoutUiState(
                    workoutId = existingWorkout.workoutId,
                    name = workoutName
                )
            )
            closeForm()
        },
        enabled = workoutName != ""
    ) {
        Text(stringResource(id = R.string.save))
    }
}

@Preview(showBackground = true)
@Composable
fun CreateWorkoutFormPreview() {
    GymTrackerTheme(darkTheme = false) {
        CreateWorkoutForm(
            saveFunction = { },
            onDismiss = { },
            screenTitle = "Create Workout"
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CreateWorkoutFormExistingWorkoutPreview() {
    GymTrackerTheme(darkTheme = false) {
        CreateWorkoutForm(
            saveFunction = { },
            onDismiss = { },
            screenTitle = "Create Workout",
            workout = WorkoutUiState(name = "Test")
        )
    }
}
