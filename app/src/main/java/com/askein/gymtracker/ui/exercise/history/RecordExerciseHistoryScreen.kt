package com.askein.gymtracker.ui.exercise.history

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.askein.gymtracker.R
import com.askein.gymtracker.ui.AppViewModelProvider
import com.askein.gymtracker.ui.exercise.ExerciseUiState
import com.askein.gymtracker.ui.exercise.history.state.CardioExerciseHistoryUiState
import com.askein.gymtracker.ui.exercise.history.state.ExerciseHistoryUiState
import com.askein.gymtracker.ui.exercise.history.state.WeightsExerciseHistoryUiState

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
    val title = history.let { if (it == null) R.string.new_workout else R.string.update_workout }
    Box {
        if (exercise.equipment != "") {
            val savedHistory = history?: WeightsExerciseHistoryUiState()
            RecordWeightsExerciseHistoryCard(
                exerciseId = exercise.id,
                cardTitle = stringResource(id = title, exercise.name),
                saveFunction = saveFunction,
                onDismiss = onDismiss,
                modifier = modifier,
                savedHistory = savedHistory as WeightsExerciseHistoryUiState
            )
        } else {
            val savedHistory = history?: CardioExerciseHistoryUiState()
            RecordCardioExerciseHistoryCard(
                exerciseId = exercise.id,
                cardTitle = stringResource(id = title, exercise.name),
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
                contentDescription = stringResource(id = R.string.close)
            )
        }
    }
}
