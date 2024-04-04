package com.askein.gymtracker.ui.workout

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
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
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.askein.gymtracker.R
import com.askein.gymtracker.ui.AppViewModelProvider
import com.askein.gymtracker.ui.customCardElevation
import com.askein.gymtracker.ui.theme.GymTrackerTheme
import com.askein.gymtracker.ui.workout.create.CreateWorkoutForm
import com.askein.gymtracker.ui.workout.details.EditWorkoutExercisesScreen

@Composable
fun WorkoutsScreen(
    workoutNavigationFunction: (Int) -> Unit,
    showCreateWorkout: Boolean,
    dismissCreateWorkout: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: WorkoutScreenViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val workoutListUiState by viewModel.workoutListUiState.collectAsState()
    WorkoutsScreen(
        workoutListUiState = workoutListUiState,
        showCreateWorkout = showCreateWorkout,
        dismissCreateWorkout = dismissCreateWorkout,
        createWorkout = { workout -> viewModel.saveWorkout(workout) },
        workoutNavigationFunction = workoutNavigationFunction,
        modifier = modifier,
    )
}

@Composable
fun WorkoutsScreen(
    workoutListUiState: WorkoutListUiState,
    showCreateWorkout: Boolean,
    dismissCreateWorkout: () -> Unit,
    createWorkout: (WorkoutUiState) -> Unit,
    workoutNavigationFunction: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    var showWorkoutExercises by remember { mutableStateOf(false) }
    var newWorkoutName by remember { mutableStateOf("") }
    WorkoutsScreen(
        workoutListUiState = workoutListUiState,
        workoutNavigationFunction = workoutNavigationFunction,
        modifier = modifier
    )
    if (showCreateWorkout) {
        Dialog(
            onDismissRequest = dismissCreateWorkout
        ) {
            CreateWorkoutForm(
                saveFunction = { workout ->
                    createWorkout(workout)
                    showWorkoutExercises = true
                    newWorkoutName = workout.name
                },
                onDismiss = dismissCreateWorkout,
                screenTitle = stringResource(id = R.string.create_workout)
            )
        }
    }
    if (showWorkoutExercises) {
        val newWorkout = workoutListUiState.workoutList.firstOrNull { it.name == newWorkoutName }
        if (newWorkout != null) {
            EditWorkoutExercisesScreen(
                workout = newWorkout,
                existingExercises = listOf(),
                onDismiss = { showWorkoutExercises = false }
            )
        }
    }
}

@Composable
private fun WorkoutsScreen(
    workoutListUiState: WorkoutListUiState,
    workoutNavigationFunction: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.padding(vertical = 16.dp, horizontal = 16.dp)
    ) {
        val workoutColumnContentDescription = stringResource(id = R.string.workout_column)
        LazyColumn(
            modifier = modifier
                .fillMaxWidth()
                .semantics { contentDescription = workoutColumnContentDescription },
            verticalArrangement = Arrangement.spacedBy(0.dp)
        ) {
            items(workoutListUiState.workoutList.sortedBy { it.name }) { workout ->
                WorkoutCard(
                    workout = workout,
                    navigationFunction = workoutNavigationFunction
                )
            }
        }
    }
}

@Composable
fun WorkoutCard(
    workout: WorkoutUiState,
    navigationFunction: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        shape = RectangleShape,
        onClick = { navigationFunction(workout.workoutId) },
        contentPadding = PaddingValues(12.dp)
    ) {
        Card(
            modifier = modifier,
            elevation = customCardElevation()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = workout.name,
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun WorkoutsScreenPreview() {
    GymTrackerTheme(darkTheme = false) {
        WorkoutsScreen(
            workoutListUiState = WorkoutListUiState(
                listOf(
                    WorkoutUiState(0, "Arms"),
                    WorkoutUiState(1, "Shoulders"),
                    WorkoutUiState(2, "Back")
                )
            ),
            workoutNavigationFunction = { }
        )
    }
}
