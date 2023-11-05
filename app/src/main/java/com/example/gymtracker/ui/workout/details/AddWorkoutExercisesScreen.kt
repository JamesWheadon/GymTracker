package com.example.gymtracker.ui.workout.details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.gymtracker.R
import com.example.gymtracker.ui.AppViewModelProvider
import com.example.gymtracker.ui.exercise.ExerciseDetail
import com.example.gymtracker.ui.exercise.ExerciseUiState
import com.example.gymtracker.ui.exercise.ExercisesScreenViewModel
import com.example.gymtracker.ui.theme.GymTrackerTheme

@Composable
fun AddExerciseScreen(
    existingExercises: List<ExerciseUiState>,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ExercisesScreenViewModel = viewModel(
        factory = AppViewModelProvider.Factory
    )
) {
    val chosenExercises: SnapshotStateList<ExerciseUiState> =
        remember { existingExercises.toMutableStateList() }
    val allExercises = viewModel.exerciseListUiState.collectAsState().value.exerciseList
    val remainingExercises = allExercises.filter { exercise ->
        !chosenExercises.contains(exercise)
    }
    AddWorkoutExercisesScreen(
        chosenExercises = chosenExercises,
        remainingExercises = remainingExercises,
        selectFunction = { exercise -> chosenExercises.add(exercise) },
        deselectFunction = { exercise -> chosenExercises.remove(exercise) },
        onDismiss = onDismiss,
        modifier = modifier
    )
}

@Composable
fun AddWorkoutExercisesScreen(
    chosenExercises: List<ExerciseUiState>,
    remainingExercises: List<ExerciseUiState>,
    selectFunction: (ExerciseUiState) -> Unit,
    deselectFunction: (ExerciseUiState) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
    ) {
        Card {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(
                    horizontal = 0.dp,
                    vertical = 24.dp
                )
            ) {
                if (chosenExercises.isNotEmpty()) {
                    ExercisesList(chosenExercises, deselectFunction, "Workout Exercises", true)
                }
                if (remainingExercises.isNotEmpty()) {
                    ExercisesList(remainingExercises, selectFunction, "Available Exercises", false)
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
fun ExercisesList(
    chosenExercises: List<ExerciseUiState>,
    clickFunction: (ExerciseUiState) -> Unit,
    listTitle: String,
    exercisesSelected: Boolean
) {
    Text(
        text = listTitle,
        style = MaterialTheme.typography.headlineLarge
    )
    LazyColumn {
        items(chosenExercises) { exercise ->
            AddRemoveExerciseCard(
                exercise = exercise,
                checked = exercisesSelected,
                clickFunction = clickFunction
            )
        }
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
            Checkbox(
                checked = checked,
                onCheckedChange = { clickFunction(exercise) },
                modifier = Modifier.semantics {
                    contentDescription = "${if (checked) "Deselect" else "Select"} ${exercise.name}"
                }
            )
            Text(
                text = exercise.name,
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.fillMaxWidth(0.45f),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                ExerciseDetail(
                    exerciseInfo = exercise.muscleGroup,
                    iconId = R.drawable.info_48px,
                    iconDescription = "exercise icon"
                )
                ExerciseDetail(
                    exerciseInfo = exercise.equipment,
                    iconId = R.drawable.exercise_filled_48px,
                    iconDescription = "exercise icon"
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AddExerciseScreenPreview() {
    GymTrackerTheme(darkTheme = false) {
        AddWorkoutExercisesScreen(
            chosenExercises = listOf(ExerciseUiState(0, "Curls", "Biceps", "Dumbbells")),
            remainingExercises = listOf(
                ExerciseUiState(
                    1,
                    "Dips",
                    "Triceps",
                    "Dumbbells And Bars"
                ),
            ),
            selectFunction = { },
            deselectFunction = {},
            onDismiss = { }
        )
    }
}
