package com.askein.gymtracker.ui.workout.details

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.askein.gymtracker.R
import com.askein.gymtracker.ui.AppViewModelProvider
import com.askein.gymtracker.ui.exercise.ExerciseDetail
import com.askein.gymtracker.ui.exercise.ExerciseUiState
import com.askein.gymtracker.ui.exercise.ExercisesScreenViewModel
import com.askein.gymtracker.ui.theme.GymTrackerTheme
import com.askein.gymtracker.ui.workout.WorkoutUiState

@Composable
fun EditWorkoutExercisesScreen(
    workout: WorkoutUiState,
    existingExercises: List<ExerciseUiState>,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    exercisesViewModel: ExercisesScreenViewModel = viewModel(
        factory = AppViewModelProvider.Factory
    ),
    workoutExerciseCrossRefViewModel: WorkoutExerciseCrossRefViewModel = viewModel(
        factory = AppViewModelProvider.Factory
    )
) {
    val chosenExercises = remember { existingExercises.toMutableStateList() }
    val allExercises = exercisesViewModel.exerciseListUiState.collectAsState().value.exerciseList
    val remainingExercises = allExercises.filter { exercise ->
        !chosenExercises.contains(exercise)
    }
    EditWorkoutExercisesScreen(
        chosenExercises = chosenExercises,
        remainingExercises = remainingExercises,
        selectFunction = { exercise ->
            chosenExercises.add(exercise)
            workoutExerciseCrossRefViewModel.saveExerciseToWorkout(exercise, workout)
        },
        deselectFunction = { exercise ->
            chosenExercises.remove(exercise)
            workoutExerciseCrossRefViewModel.deleteExerciseFromWorkout(exercise, workout)
        },
        onDismiss = onDismiss,
        modifier = modifier
    )
}

@Composable
fun EditWorkoutExercisesScreen(
    chosenExercises: List<ExerciseUiState>,
    remainingExercises: List<ExerciseUiState>,
    selectFunction: (ExerciseUiState) -> Unit,
    deselectFunction: (ExerciseUiState) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.4f))
    ) {
        Box(
            modifier = modifier.fillMaxSize(0.8F)
        ) {
            Card {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier
                        .padding(
                            horizontal = 0.dp,
                            vertical = 40.dp
                        )
                        .verticalScroll(rememberScrollState())
                ) {
                    if (chosenExercises.isNotEmpty()) {
                        ExercisesList(
                            exercises = chosenExercises,
                            clickFunction = deselectFunction,
                            listTitle = R.string.workout_exercises,
                            exercisesSelected = true
                        )
                    }
                    if (remainingExercises.isNotEmpty()) {
                        ExercisesList(
                            exercises = remainingExercises,
                            clickFunction = selectFunction,
                            listTitle = R.string.available_exercises,
                            exercisesSelected = false
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
}

@Composable
fun ExercisesList(
    exercises: List<ExerciseUiState>,
    clickFunction: (ExerciseUiState) -> Unit,
    @StringRes listTitle: Int,
    exercisesSelected: Boolean
) {
    Text(
        text = stringResource(id = listTitle),
        style = MaterialTheme.typography.headlineLarge
    )
    exercises.forEach { exercise ->
        AddRemoveExerciseCard(
            exercise = exercise,
            checked = exercisesSelected,
            clickFunction = clickFunction
        )
    }
}

@Composable
fun AddRemoveExerciseCard(
    exercise: ExerciseUiState,
    checked: Boolean,
    clickFunction: (ExerciseUiState) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            val checkboxContentDescription = if (checked) {
                stringResource(id = R.string.deselect_exercise, exercise.name)
            } else {
                stringResource(id = R.string.select_exercise, exercise.name)
            }
            Checkbox(
                checked = checked,
                onCheckedChange = { clickFunction(exercise) },
                modifier = Modifier.semantics {
                    contentDescription = checkboxContentDescription
                }
            )
            Text(
                text = exercise.name,
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.fillMaxWidth(0.45f),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            if (exercise.equipment != "") {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    ExerciseDetail(
                        exerciseInfo = exercise.muscleGroup,
                        iconId = R.drawable.info_48px,
                        iconDescription = R.string.muscle_icon
                    )
                    ExerciseDetail(
                        exerciseInfo = exercise.equipment,
                        iconId = R.drawable.exercise_filled_48px,
                        iconDescription = R.string.equipment_icon
                    )
                }
            } else {
                ExerciseDetail(
                    exerciseInfo = stringResource(id = R.string.cardio),
                    iconId = R.drawable.cardio_48dp,
                    iconDescription = R.string.cardio_icon,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AddExerciseScreenPreview() {
    GymTrackerTheme(darkTheme = false) {
        EditWorkoutExercisesScreen(
            chosenExercises = listOf(ExerciseUiState(0, "Curls", "Biceps", "Dumbbells")),
            remainingExercises = listOf(
                ExerciseUiState(
                    1,
                    "Dips",
                    "Triceps",
                    "Dumbbells And Bars"
                ),
                ExerciseUiState(
                    1,
                    "Treadmill"
                ),
            ),
            selectFunction = { },
            deselectFunction = {},
            onDismiss = { }
        )
    }
}
