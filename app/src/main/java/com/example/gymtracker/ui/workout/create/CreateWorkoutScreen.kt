package com.example.gymtracker.ui.workout.create

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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.gymtracker.data.workout.Workout
import com.example.gymtracker.ui.FormInformationField
import com.example.gymtracker.ui.customCardElevation
import com.example.gymtracker.ui.theme.GymTrackerTheme

@Composable
fun CreateWorkoutForm(
    saveFunction: (Workout) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    var nameState by remember { mutableStateOf("") }
    Box {
        Card(
            modifier = modifier
                .padding(vertical = 10.dp, horizontal = 10.dp),
            elevation = customCardElevation()
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Create Workout",
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
                        label = "Workout Name",
                        value = nameState,
                        onChange = { entry ->
                            nameState = entry
                        }
                    )
                    SaveWorkoutFormButton(
                        workoutName = nameState,
                        saveFunction = saveFunction,
                        closeForm = onDismiss
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
                contentDescription = "Close"
            )
        }
    }
}

@Composable
fun SaveWorkoutFormButton(
    workoutName: String,
    saveFunction: (Workout) -> Unit,
    closeForm: () -> Unit
) {
    if (workoutName != "") {
        Button(onClick = {
            saveFunction(
                Workout(
                    name = workoutName
                )
            )
            closeForm()
        }) {
            Text("Save Workout")
        }
    } else {
        Button(
            onClick = { },
            enabled = false
        ) {
            Text("Save Workout")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CreateWorkoutFromPreview() {
    GymTrackerTheme(darkTheme = false) {
        CreateWorkoutForm(saveFunction = { }, onDismiss = { })
    }
}
