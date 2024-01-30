package com.example.gymtracker.ui.exercise.create

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.gymtracker.data.exercise.Exercise
import com.example.gymtracker.ui.AppViewModelProvider
import com.example.gymtracker.ui.ExerciseInformationFieldWithSuggestions
import com.example.gymtracker.ui.FormInformationField
import com.example.gymtracker.ui.exercise.ExerciseUiState
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
    ExerciseInformationForm(
        formTitle = "Create New Exercise",
        buttonText = "Create",
        onDismiss = onDismiss,
        createFunction = { exercise ->
            viewModel.saveExercise(exercise)
        },
        modifier = modifier
    )
}

@Composable
fun ExerciseInformationForm(
    formTitle: String,
    buttonText: String,
    onDismiss: () -> Unit,
    createFunction: (Exercise) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ExercisesScreenViewModel = viewModel(
        factory = AppViewModelProvider.Factory
    ),
    exercise: ExerciseUiState = ExerciseUiState()
) {
    val customCardElevation = CardDefaults.cardElevation(
        defaultElevation = 16.dp
    )
    val savedMuscleGroups = viewModel.muscleGroupUiState.collectAsState().value
    val savedExerciseNames = viewModel.exerciseNamesUiState.collectAsState().value
    val idState by remember { mutableStateOf(exercise.id) }
    var nameState by remember { mutableStateOf(exercise.name) }
    var equipmentState by remember { mutableStateOf(exercise.equipment) }
    var muscleState by remember { mutableStateOf(TextFieldValue(text = exercise.muscleGroup)) }
    val nameError = savedExerciseNames.map(String::lowercase).contains(nameState.lowercase())
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
                    text = formTitle,
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
                    FormInformationField(
                        label = "Exercise Name",
                        value = nameState,
                        onChange = { entry ->
                            nameState = entry
                        },
                        keyboardOptions = KeyboardOptions.Default,
                        error = nameError,
                        errorMessage = "Name already taken"
                    )
                    FormInformationField(
                        label = "Equipment",
                        value = equipmentState,
                        onChange = { entry ->
                            equipmentState = entry
                        },
                        keyboardOptions = KeyboardOptions.Default
                    )
                    ExerciseInformationFieldWithSuggestions(
                        label = "Muscle Group",
                        value = muscleState,
                        onChange = { entry ->
                            muscleState = entry
                        },
                        suggestions = savedMuscleGroups
                    )
                    SaveExerciseFormButton(
                        exerciseId = idState,
                        exerciseName = nameState,
                        exerciseEquipment = equipmentState,
                        exerciseMuscleGroup = muscleState.text,
                        errors = nameError,
                        buttonText = buttonText,
                        saveFunction = createFunction,
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
private fun SaveExerciseFormButton(
    exerciseId: Int,
    exerciseName: String,
    exerciseEquipment: String,
    exerciseMuscleGroup: String,
    errors: Boolean,
    buttonText: String,
    saveFunction: (Exercise) -> Unit,
    closeForm: () -> Unit
) {
    if (exerciseName != "" && exerciseEquipment != "" && exerciseMuscleGroup != "" && !errors) {
        Button(onClick = {
            saveFunction(
                Exercise(
                    exerciseId = exerciseId,
                    name = exerciseName,
                    muscleGroup = exerciseMuscleGroup,
                    equipment = exerciseEquipment
                )
            )
            closeForm()
        }) {
            Text(buttonText)
        }
    } else {
        Button(
            onClick = { },
            enabled = false
        ) {
            Text(buttonText)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CreateExerciseDetailsScreenPreview() {
    GymTrackerTheme(darkTheme = false) {
        ExerciseInformationForm(
            formTitle = "Create New Exercise",
            onDismiss = {},
            createFunction = {},
            buttonText = "Create"
        )
    }
}
