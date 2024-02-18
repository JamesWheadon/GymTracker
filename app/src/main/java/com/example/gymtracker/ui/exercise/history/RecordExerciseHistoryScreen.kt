package com.example.gymtracker.ui.exercise.history

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.gymtracker.ui.AppViewModelProvider
import com.example.gymtracker.ui.exercise.ExerciseUiState
import com.example.gymtracker.ui.exercise.history.state.CardioExerciseHistoryUiState
import com.example.gymtracker.ui.exercise.history.state.ExerciseHistoryUiState
import com.example.gymtracker.ui.exercise.history.state.WeightsExerciseHistoryUiState
import com.example.gymtracker.ui.theme.GymTrackerTheme


@Composable
fun RecordExerciseHistoryScreen(
    exercise: ExerciseUiState,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: RecordExerciseHistoryViewModel = viewModel(
        factory = AppViewModelProvider.Factory
    )
) {
    RecordExerciseHistoryScreen(
        exercise = exercise,
        saveFunction = { history ->
            viewModel.saveHistory(history)
        },
        onDismiss = onDismiss,
        modifier = modifier
    )
}

@Composable
fun UpdateExerciseHistoryScreen(
    exercise: ExerciseUiState,
    history: ExerciseHistoryUiState,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: RecordExerciseHistoryViewModel = viewModel(
        factory = AppViewModelProvider.Factory
    )
) {
    RecordExerciseHistoryScreen(
        exercise = exercise,
        saveFunction = { existingHistory ->
            viewModel.updateHistory(existingHistory)
        },
        onDismiss = onDismiss,
        modifier = modifier,
        history = history
    )
}

@Composable
fun RecordExerciseHistoryScreen(
    exercise: ExerciseUiState,
    saveFunction: (ExerciseHistoryUiState) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    history: ExerciseHistoryUiState? = null
) {
    val titleStart = history.let { if (it == null) "New" else "Update" }
    Box {
        if (exercise.equipment != "") {
            val savedHistory = history?: WeightsExerciseHistoryUiState()
            RecordWeightsExerciseHistoryCard(
                exerciseId = exercise.id,
                cardTitle = "$titleStart ${exercise.name} Workout",
                saveFunction = saveFunction,
                onDismiss = onDismiss,
                modifier = modifier,
                savedHistory = savedHistory as WeightsExerciseHistoryUiState
            )
        } else {
            val savedHistory = history?: CardioExerciseHistoryUiState()
            RecordCardioExerciseHistoryCard(
                exerciseId = exercise.id,
                cardTitle = "$titleStart ${exercise.name} Workout",
                saveFunction = saveFunction,
                onDismiss = onDismiss,
                modifier = modifier,
                savedHistory = savedHistory as CardioExerciseHistoryUiState
            )
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

@Preview(showBackground = true)
@Composable
fun RecordExerciseHistoryScreenPreview() {
    GymTrackerTheme(darkTheme = false) {
        RecordExerciseHistoryScreen(
            exercise = ExerciseUiState(
                name = "Curls",
                muscleGroup = "Biceps",
                equipment = "Dumbbells"
            ),
            saveFunction = {},
            onDismiss = {}
        )
    }
}
