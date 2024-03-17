package com.example.gymtracker.ui.exercise.create

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.gymtracker.R
import com.example.gymtracker.enums.FormTypes
import com.example.gymtracker.ui.AppViewModelProvider
import com.example.gymtracker.ui.FormInformationField
import com.example.gymtracker.ui.FormInformationFieldWithSuggestions
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
        formTitle = R.string.create_exercise_title,
        buttonText = R.string.create,
        onDismiss = onDismiss,
        createFunction = { exercise ->
            viewModel.saveExercise(exercise)
        },
        modifier = modifier
    )
}

@Composable
fun ExerciseInformationForm(
    @StringRes formTitle: Int,
    @StringRes buttonText: Int,
    onDismiss: () -> Unit,
    createFunction: (ExerciseUiState) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ExercisesScreenViewModel = viewModel(
        factory = AppViewModelProvider.Factory
    ),
    exercise: ExerciseUiState = ExerciseUiState()
) {
    val savedMuscleGroups = viewModel.muscleGroupUiState.collectAsState().value
    val savedExerciseNames = viewModel.exerciseNamesUiState.collectAsState().value
    ExerciseInformationForm(
        formTitle = formTitle,
        buttonText = buttonText,
        savedExerciseNames = savedExerciseNames,
        savedMuscleGroups = savedMuscleGroups,
        exercise = exercise,
        createFunction = createFunction,
        onDismiss = onDismiss,
        modifier = modifier
    )
}

@Composable
fun ExerciseInformationForm(
    @StringRes formTitle: Int,
    @StringRes buttonText: Int,
    savedExerciseNames: List<String>,
    savedMuscleGroups: List<String>,
    exercise: ExerciseUiState,
    createFunction: (ExerciseUiState) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    val customCardElevation = CardDefaults.cardElevation(
        defaultElevation = 16.dp
    )
    val exerciseId = exercise.id
    var nameState by remember { mutableStateOf(exercise.name) }
    var equipmentState by remember { mutableStateOf(exercise.equipment) }
    var muscleState by remember { mutableStateOf(TextFieldValue(text = exercise.muscleGroup)) }
    var optionState by remember { mutableStateOf(exercise == ExerciseUiState() || exercise.equipment != "") }
    val nameError = savedExerciseNames.map(String::lowercase).contains(nameState.lowercase())
    Box {
        Card(
            modifier = modifier
                .padding(vertical = 10.dp, horizontal = 10.dp),
            elevation = customCardElevation
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(id = formTitle),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.headlineLarge,
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.CenterHorizontally)
                        .padding(vertical = 16.dp, horizontal = 16.dp)
                )
                if (exercise == ExerciseUiState()) {
                    Row(
                        horizontalArrangement = Arrangement.SpaceAround,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 48.dp)
                    ) {
                        Text(
                            text = stringResource(id = R.string.cardio),
                            fontWeight = if (optionState) null else FontWeight.Bold
                        )
                        val toggleContentDescription =
                            stringResource(id = R.string.exercise_type_toggle)
                        Switch(
                            checked = optionState,
                            onCheckedChange = { option -> optionState = option },
                            modifier = Modifier.semantics {
                                contentDescription = toggleContentDescription
                            }
                        )
                        Text(
                            text = stringResource(id = R.string.weights),
                            fontWeight = if (optionState) FontWeight.Bold else null
                        )
                    }
                }
                if (optionState) {
                    WeightsExerciseInformationForm(
                        nameState = nameState,
                        nameError = nameError,
                        nameStateOnChange = { name ->
                            nameState = name
                        },
                        equipmentState = equipmentState,
                        equipmentStateOnChange = { equipment ->
                            equipmentState = equipment
                        },
                        muscleState = muscleState,
                        muscleStateOnChange = { muscle ->
                            muscleState = muscle
                        },
                        savedMuscleGroups = savedMuscleGroups
                    )
                } else {
                    CardioExerciseInformationForm(
                        nameState = nameState,
                        nameError = nameError,
                        nameStateOnChange = { name ->
                            nameState = name
                        }
                    )
                }
                SaveExerciseFormButton(
                    exerciseId = exerciseId,
                    exerciseName = nameState,
                    exerciseEquipment = equipmentState,
                    exerciseMuscleGroup = muscleState.text,
                    exerciseToggle = optionState,
                    errors = nameError,
                    buttonText = buttonText,
                    saveFunction = createFunction,
                    closeForm = onDismiss
                )
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
private fun WeightsExerciseInformationForm(
    nameState: String,
    nameStateOnChange: (String) -> Unit,
    nameError: Boolean,
    equipmentState: String,
    equipmentStateOnChange: (String) -> Unit,
    muscleState: TextFieldValue,
    muscleStateOnChange: (TextFieldValue) -> Unit,
    savedMuscleGroups: List<String>
) {
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        FormInformationField(
            label = R.string.exercise_name,
            value = nameState,
            onChange = nameStateOnChange,
            formType = FormTypes.STRING,
            error = nameError,
            errorMessage = R.string.exercise_name_taken
        )
        FormInformationField(
            label = R.string.equipment,
            value = equipmentState,
            onChange = equipmentStateOnChange,
            formType = FormTypes.STRING,
        )
        FormInformationFieldWithSuggestions(
            label = R.string.muscle_group,
            value = muscleState,
            onChange = muscleStateOnChange,
            suggestions = savedMuscleGroups
        )
    }
}

@Composable
private fun CardioExerciseInformationForm(
    nameState: String,
    nameStateOnChange: (String) -> Unit,
    nameError: Boolean
) {
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        FormInformationField(
            label = R.string.exercise_name,
            value = nameState,
            onChange = nameStateOnChange,
            formType = FormTypes.STRING,
            error = nameError,
            errorMessage = R.string.exercise_name_taken
        )
    }
}

@Composable
private fun SaveExerciseFormButton(
    exerciseId: Int,
    exerciseName: String,
    exerciseEquipment: String,
    exerciseMuscleGroup: String,
    exerciseToggle: Boolean,
    errors: Boolean,
    @StringRes buttonText: Int,
    saveFunction: (ExerciseUiState) -> Unit,
    closeForm: () -> Unit
) {
    val enabled =
        (!exerciseToggle && exerciseName != "") || (exerciseName != "" && exerciseEquipment != "" && exerciseMuscleGroup != "" && !errors)
    Button(onClick = {
        saveFunction(
            ExerciseUiState(
                id = exerciseId,
                name = exerciseName,
                muscleGroup = exerciseMuscleGroup,
                equipment = exerciseEquipment
            )
        )
        closeForm()
    }, enabled = enabled) {
        Text(text = stringResource(id = buttonText))
    }
}

@Preview(showBackground = true)
@Composable
fun CreateExerciseDetailsScreenPreview() {
    GymTrackerTheme(darkTheme = false) {
        ExerciseInformationForm(
            formTitle = R.string.create_exercise_title,
            buttonText = R.string.create,
            savedExerciseNames = listOf(),
            savedMuscleGroups = listOf(),
            exercise = ExerciseUiState(),
            createFunction = {},
            onDismiss = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun UpdateExerciseDetailsScreenPreview() {
    GymTrackerTheme(darkTheme = false) {
        ExerciseInformationForm(
            formTitle = R.string.create_exercise_title,
            buttonText = R.string.create,
            savedExerciseNames = listOf(),
            savedMuscleGroups = listOf(),
            exercise = ExerciseUiState(name = "Update exercise", equipment = "Dumbbells"),
            createFunction = {},
            onDismiss = {}
        )
    }
}
