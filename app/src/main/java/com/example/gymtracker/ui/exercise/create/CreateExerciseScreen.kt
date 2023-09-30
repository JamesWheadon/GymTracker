package com.example.gymtracker.ui.exercise.create

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.gymtracker.data.exercise.Exercise
import com.example.gymtracker.ui.AppViewModelProvider
import com.example.gymtracker.ui.exercise.ExerciseInformationField
import com.example.gymtracker.ui.exercise.ExercisesScreenViewModel
import com.example.gymtracker.ui.theme.GymTrackerTheme

@Composable
fun CreateExerciseScreen(
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit,
    viewModel: ExercisesScreenViewModel = viewModel(
        factory = AppViewModelProvider.Factory
    )
) {
    CreateExerciseScreen(
        modifier = modifier,
        createFunction = { exercise ->
            viewModel.saveExercise(exercise)
        },
        onDismiss = onDismiss
    )
}

@Composable
fun CreateExerciseScreen(
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit,
    createFunction: (Exercise) -> Unit
) {
    val customCardElevation = CardDefaults.cardElevation(
        defaultElevation = 16.dp
    )
    var nameState by remember { mutableStateOf("") }
    var equipmentState by remember { mutableStateOf("") }
    var muscleState by remember { mutableStateOf("") }
    Box {
        Card(
            modifier = modifier
                .padding(vertical = 10.dp, horizontal = 10.dp),
            elevation = customCardElevation
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Create New Exercise",
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
                    Spacer(modifier = Modifier.height(6.dp))
                    ExerciseInformationField(
                        label = "Exercise Name",
                        value = nameState,
                        onChange = { entry ->
                            nameState = entry
                        }
                    )
                    ExerciseInformationField(
                        label = "Equipment",
                        value = equipmentState,
                        onChange = { entry ->
                            equipmentState = entry
                        }
                    )
                    ExerciseInformationField(
                        label = "Muscle Group",
                        value = muscleState,
                        onChange = { entry ->
                            muscleState = entry
                        }
                    )
                    CreateExerciseButton(nameState, equipmentState, muscleState, createFunction, onDismiss)
                }
            }
        }
        IconButton(
            modifier = Modifier.align(Alignment.TopEnd).offset((-8).dp, 8.dp),
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
private fun CreateExerciseButton(
    nameState: String,
    equipmentState: String,
    muscleState: String,
    createFunction: (Exercise) -> Unit,
    onDismiss: () -> Unit
) {
    if (nameState != "" && equipmentState != "" && muscleState != "") {
        Button(onClick = {
            createFunction(
                Exercise(
                    name = nameState,
                    muscleGroup = muscleState,
                    equipment = equipmentState
                )
            )
            onDismiss()
        }) {
            Text("Create")
        }
    } else {
        Button(
            onClick = { },
            enabled = false
        ) {
            Text("Create")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CreateExerciseDetailsScreenPreview() {
    GymTrackerTheme(darkTheme = false) {
        CreateExerciseScreen(
            createFunction = {},
            onDismiss = {}
        )
    }
}
