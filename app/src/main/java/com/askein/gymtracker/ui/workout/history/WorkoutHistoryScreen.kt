package com.askein.gymtracker.ui.workout.history

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.outlined.Delete
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import com.askein.gymtracker.R
import com.askein.gymtracker.ui.ActionConfirmation
import com.askein.gymtracker.ui.AppViewModelProvider
import com.askein.gymtracker.ui.exercise.ExerciseUiState
import com.askein.gymtracker.ui.exercise.details.ExerciseHistoryDetails
import com.askein.gymtracker.ui.exercise.history.state.ExerciseHistoryUiState
import com.askein.gymtracker.ui.workout.details.WorkoutWithExercisesUiState
import com.askein.gymtracker.ui.workout.history.create.RecordWorkoutHistoryScreen
import java.time.LocalDate

@Composable
fun WorkoutHistoryScreen(
    workoutHistoryUiState: WorkoutHistoryWithExercisesUiState,
    workoutUiState: WorkoutWithExercisesUiState,
    exerciseNavigationFunction: (Int, LocalDate?) -> Unit,
    chosenDate: LocalDate,
    modifier: Modifier = Modifier,
    viewModel: WorkoutHistoryViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    var showEditWorkoutHistory by remember { mutableStateOf(false) }
    var showDeleteWorkoutHistory by remember { mutableStateOf(false) }
    val historyExerciseIds = workoutHistoryUiState.exerciseHistories
        .map { exerciseHistory -> exerciseHistory.exerciseId }
    Box {
        Card(
            elevation = CardDefaults.cardElevation(
                defaultElevation = 16.dp
            ),
            modifier = modifier.defaultMinSize(minHeight = 30.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                workoutUiState.exercises.filter { exercise ->
                    historyExerciseIds.contains(exercise.id)
                }.forEach { exercise ->
                    WorkoutHistoryExerciseCard(
                        exercise = exercise,
                        exerciseHistory = workoutHistoryUiState.exerciseHistories.first { exerciseHistory -> exerciseHistory.exerciseId == exercise.id },
                        chosenDate = chosenDate,
                        exerciseNavigationFunction = exerciseNavigationFunction
                    )
                }
            }
        }
        IconButton(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .offset((-40).dp, (-12).dp),
            onClick = { showEditWorkoutHistory = true }
        ) {
            Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = stringResource(id = R.string.edit)
            )
        }
        IconButton(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .offset((-8).dp, (-12).dp),
            onClick = { showDeleteWorkoutHistory = true }
        ) {
            Icon(
                imageVector = Icons.Outlined.Delete,
                tint = Color.Red,
                contentDescription = stringResource(id = R.string.delete)
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
                titleText = stringResource(id = R.string.update_workout, workoutUiState.name)
            )
        }
    }
    if (showDeleteWorkoutHistory) {
        Dialog(
            onDismissRequest = { showDeleteWorkoutHistory = false }
        ) {
            ActionConfirmation(
                actionTitle = stringResource(id = R.string.delete_workout_confirm),
                confirmFunction = {
                    viewModel.deleteWorkoutHistory(workoutHistoryUiState)
                    showDeleteWorkoutHistory = false
                },
                cancelFunction = { showDeleteWorkoutHistory = false }
            )
        }
    }
}

@Composable
fun WorkoutHistoryExerciseCard(
    exercise: ExerciseUiState,
    exerciseHistory: ExerciseHistoryUiState,
    chosenDate: LocalDate,
    exerciseNavigationFunction: (Int, LocalDate?) -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { exerciseNavigationFunction(exercise.id, chosenDate) }
    ) {
        Text(
            text = exercise.name,
            style = MaterialTheme.typography.headlineSmall
        )
        ExerciseHistoryDetails(
            exerciseHistory = exerciseHistory,
            exerciseType = exercise.type
        )
    }
}
