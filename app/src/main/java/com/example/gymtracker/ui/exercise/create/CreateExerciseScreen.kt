package com.example.gymtracker.ui.exercise.create

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
    navigateFunction: () -> Unit,
    viewModel: ExercisesScreenViewModel = viewModel(
        factory = AppViewModelProvider.Factory
    )
) {
    CreateExerciseScreen(
        modifier = modifier,
        createFunction = { exercise ->
            viewModel.saveExercise(exercise)
        },
        navigateFunction = navigateFunction
    )
}

@Composable
fun CreateExerciseScreen(
    modifier: Modifier = Modifier,
    createFunction: (Exercise) -> Unit,
    navigateFunction: () -> Unit
) {
    val customCardElevation = CardDefaults.cardElevation(
        defaultElevation = 16.dp
    )
    var nameState by remember { mutableStateOf("") }
    var equipmentState by remember { mutableStateOf("") }
    var muscleState by remember { mutableStateOf("") }

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
                if (nameState != "" && equipmentState != "" && muscleState != "") {
                    Button(onClick = {
                        saveExercise(
                            createFunction = createFunction,
                            name = nameState,
                            equipment = equipmentState,
                            muscle = muscleState
                        )
                        navigateFunction()
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
                Spacer(modifier = Modifier.height(6.dp))
            }
        }
    }
}

fun saveExercise(
    createFunction: (Exercise) -> Unit,
    name: String,
    equipment: String,
    muscle: String
) {
    val newExercise = Exercise(
        name = name,
        muscleGroup = muscle,
        equipment = equipment
    )
    createFunction(newExercise)
}

@Preview(showBackground = true)
@Composable
fun CreateExerciseDetailsScreenPreview() {
    GymTrackerTheme(darkTheme = false) {
        CreateExerciseScreen(
            createFunction = {},
            navigateFunction = {}
        )
    }
}
