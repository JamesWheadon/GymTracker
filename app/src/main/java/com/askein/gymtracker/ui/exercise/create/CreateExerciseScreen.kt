package com.askein.gymtracker.ui.exercise.create

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.Card
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.askein.gymtracker.R
import com.askein.gymtracker.data.exercise.ExerciseType
import com.askein.gymtracker.ui.AppViewModelProvider
import com.askein.gymtracker.ui.customCardElevation
import com.askein.gymtracker.ui.exercise.ExerciseUiState
import com.askein.gymtracker.ui.exercise.ExercisesScreenViewModel

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
    var exerciseInfo by remember { mutableStateOf(exercise.toExerciseInfo()) }
    val nameError = exerciseInfo.name != exercise.name &&
            savedExerciseNames.map(String::lowercase).contains(exerciseInfo.name.lowercase())
    Box {
        Card(
            modifier = modifier
                .padding(vertical = 10.dp, horizontal = 10.dp),
            elevation = customCardElevation()
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
                    ExerciseTypeSelection(
                        exerciseType = exerciseInfo.exerciseType,
                        exerciseTypeOnChange = { selected ->
                            exerciseInfo = exerciseInfo.copy(exerciseType = selected)
                        }
                    )
                }
                ExerciseTypeForm(
                    exerciseInfo = exerciseInfo,
                    exerciseInfoOnChange = { newInfo ->
                        exerciseInfo = newInfo
                    },
                    nameError = nameError,
                    savedMuscleGroups = savedMuscleGroups
                )
                SaveExerciseFormButton(
                    exerciseInfo = exerciseInfo,
                    nameTaken = nameError,
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
private fun ExerciseTypeForm(
    exerciseInfo: ExerciseInfo,
    exerciseInfoOnChange: (ExerciseInfo) -> Unit,
    nameError: Boolean,
    savedMuscleGroups: List<String>
) {
    when (exerciseInfo.exerciseType) {
        ExerciseType.WEIGHTS -> {
            WeightsExerciseForm(
                exerciseInfo = exerciseInfo,
                exerciseInfoOnChange = exerciseInfoOnChange,
                nameError = nameError,
                savedMuscleGroups = savedMuscleGroups
            )
        }

        ExerciseType.CARDIO -> {
            CardioExerciseForm(
                exerciseInfo = exerciseInfo,
                exerciseInfoOnChange = exerciseInfoOnChange,
                nameError = nameError
            )
        }

        ExerciseType.CALISTHENICS -> {
            CalisthenicsExerciseForm(
                exerciseInfo = exerciseInfo,
                exerciseInfoOnChange = exerciseInfoOnChange,
                nameError = nameError,
                savedMuscleGroups = savedMuscleGroups
            )
        }
    }
}

@Composable
private fun ExerciseTypeSelection(
    exerciseType: ExerciseType,
    exerciseTypeOnChange: (ExerciseType) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.width(IntrinsicSize.Min)
    ) {
        Button(
            onClick = { exerciseTypeOnChange(ExerciseType.WEIGHTS) },
            enabled = exerciseType != ExerciseType.WEIGHTS,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = stringResource(id = R.string.weights))
        }
        Button(
            onClick = { exerciseTypeOnChange(ExerciseType.CARDIO) },
            enabled = exerciseType != ExerciseType.CARDIO,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = stringResource(id = R.string.cardio))
        }
        Button(
            onClick = { exerciseTypeOnChange(ExerciseType.CALISTHENICS) },
            enabled = exerciseType != ExerciseType.CALISTHENICS,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = stringResource(id = R.string.calisthenics))
        }
    }
}

@Composable
private fun SaveExerciseFormButton(
    exerciseInfo: ExerciseInfo,
    nameTaken: Boolean,
    @StringRes buttonText: Int,
    saveFunction: (ExerciseUiState) -> Unit,
    closeForm: () -> Unit
) {
    val enabled = exerciseInfo.name != "" && !nameTaken
    Button(onClick = {
        saveFunction(
            ExerciseUiState(
                id = exerciseInfo.exerciseId,
                type = exerciseInfo.exerciseType,
                name = exerciseInfo.name,
                muscleGroup = exerciseInfo.muscleGroup,
                equipment = exerciseInfo.equipment
            )
        )
        closeForm()
    }, enabled = enabled) {
        Text(text = stringResource(id = buttonText))
    }
}

data class ExerciseInfo(
    val exerciseId: Int,
    val exerciseType: ExerciseType,
    val name: String,
    val equipment: String,
    val muscleGroup: String
)

fun ExerciseUiState.toExerciseInfo(): ExerciseInfo = ExerciseInfo(
    exerciseId = id,
    exerciseType = type,
    name = name,
    equipment = equipment,
    muscleGroup = muscleGroup
)
