package com.example.gymtracker.ui.workout.history

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.gymtracker.ui.ActionConfirmation
import com.example.gymtracker.ui.AppViewModelProvider
import com.example.gymtracker.ui.customCardElevation
import com.example.gymtracker.ui.exercise.ExerciseUiState
import com.example.gymtracker.ui.exercise.details.HistoryDetails
import com.example.gymtracker.ui.exercise.history.state.WeightsExerciseHistoryUiState
import com.example.gymtracker.ui.theme.GymTrackerTheme
import com.example.gymtracker.ui.workout.details.WorkoutWithExercisesUiState
import com.example.gymtracker.ui.workout.history.create.RecordWorkoutHistoryScreen
import java.time.LocalDate

@Composable
fun WorkoutHistoryScreen(
    workoutHistoryUiState: WorkoutHistoryWithExercisesUiState,
    workoutUiState: WorkoutWithExercisesUiState,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: WorkoutHistoryViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    var showEditWorkoutHistory by remember { mutableStateOf(false) }
    var showDeleteWorkoutHistory by remember { mutableStateOf(false) }
    Box {
        Card(
            elevation = customCardElevation(),
            modifier = modifier
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                workoutUiState.exercises.filter { exercise ->
                    workoutHistoryUiState.exercises
                        .map { exerciseHistory -> exerciseHistory.exerciseId }
                        .contains(exercise.id)
                }.forEach { exercise ->
                    WorkoutHistoryExerciseCard(
                        exercise = exercise,
                        exerciseHistory = workoutHistoryUiState.exercises.first { exerciseHistory -> exerciseHistory.exerciseId == exercise.id }
                    )
                }
            }
        }
        IconButton(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .offset((-72).dp, (-12).dp),
            onClick = { showEditWorkoutHistory = true }
        ) {
            Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = "Edit"
            )
        }
        IconButton(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .offset((-40).dp, (-12).dp),
            onClick = { showDeleteWorkoutHistory = true }
        ) {
            Icon(
                imageVector = Icons.Outlined.Delete,
                tint = Color.Red,
                contentDescription = "Delete"
            )
        }
        IconButton(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .offset((-8).dp, (-12).dp),
            onClick = { onDismiss() }
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Close"
            )
        }
    }
    if (showEditWorkoutHistory) {
        Dialog(
            onDismissRequest = { showEditWorkoutHistory = false },
            properties = DialogProperties(usePlatformDefaultWidth = false)
        ) {
            RecordWorkoutHistoryScreen(
                uiState = workoutUiState,
                onDismiss = { showEditWorkoutHistory = false },
                modifier = Modifier.fillMaxSize(),
                workoutHistory = workoutHistoryUiState,
                titleText = "Update Workout"
            )
        }
    }
    if (showDeleteWorkoutHistory) {
        val (workoutHistory, exerciseHistories) = workoutHistoryUiState.toWorkoutAndExercises()
        Dialog(
            onDismissRequest = { showDeleteWorkoutHistory = false }
        ) {
            ActionConfirmation(
                actionTitle = "Do you want to delete this workout?",
                confirmFunction = {
                    viewModel.deleteWorkoutHistory(workoutHistory, exerciseHistories)
                    onDismiss()
                },
                cancelFunction = { showDeleteWorkoutHistory = false }
            )
        }
    }
}

@Composable
fun WorkoutHistoryScreen(
    uiState: WorkoutHistoryWithExercisesUiState,
    exercises: List<ExerciseUiState>,
    modifier: Modifier = Modifier
) {
    Card(
        elevation = customCardElevation(),
        modifier = modifier
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            items(exercises.filter { exercise ->
                uiState.exercises
                    .map { exerciseHistory -> exerciseHistory.exerciseId }
                    .contains(exercise.id)
            }) { exercise ->
                WorkoutHistoryExerciseCard(
                    exercise = exercise,
                    exerciseHistory = uiState.exercises.first { exerciseHistory -> exerciseHistory.exerciseId == exercise.id }
                )
            }
        }
    }
}

@Composable
fun WorkoutHistoryExerciseCard(
    exercise: ExerciseUiState,
    exerciseHistory: WeightsExerciseHistoryUiState
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(text = exercise.name)
        HistoryDetails(
            exerciseHistory = exerciseHistory,
            exercise = exercise,
            editEnabled = false,
            deleteFunction = { }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun WorkoutHistoryScreenPreview() {
    GymTrackerTheme(darkTheme = false) {
        WorkoutHistoryScreen(
            uiState = WorkoutHistoryWithExercisesUiState(
                1, 1, LocalDate.now(), exercises = listOf(
                    WeightsExerciseHistoryUiState(1, 1, LocalDate.now(), 1.0, 1, 1, 1),
                    WeightsExerciseHistoryUiState(2, 2, LocalDate.now(), 1.0, 1, 1, 1)
                )
            ),
            exercises = listOf(
                ExerciseUiState(1, "Curls", "Biceps", "Dumbbells"),
                ExerciseUiState(2, "Dips", "Triceps", "Bars")
            )
        )
    }
}
