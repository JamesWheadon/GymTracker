package com.example.gymtracker.ui.history

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.gymtracker.converters.WeightUnits
import com.example.gymtracker.converters.convertToKilograms
import com.example.gymtracker.converters.getWeightUnitFromShortForm
import com.example.gymtracker.data.history.ExerciseHistory
import com.example.gymtracker.ui.AppViewModelProvider
import com.example.gymtracker.ui.DropdownBox
import com.example.gymtracker.ui.ExerciseInformationField
import com.example.gymtracker.ui.exercise.ExerciseUiState
import com.example.gymtracker.ui.theme.GymTrackerTheme
import java.time.LocalDate


@Composable
fun RecordHistoryScreen(
    exercise: ExerciseUiState,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: RecordHistoryViewModel = viewModel(
        factory = AppViewModelProvider.Factory
    )
) {
    RecordHistoryScreen(
        exercise = exercise,
        saveFunction = { history ->
            viewModel.saveHistory(history)
        },
        onDismiss = onDismiss,
        modifier = modifier
    )
}

@Composable
fun UpdateHistoryScreen(
    exercise: ExerciseUiState,
    history: ExerciseHistoryUiState,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: RecordHistoryViewModel = viewModel(
        factory = AppViewModelProvider.Factory
    )
) {
    EditHistoryScreen(
        history = history,
        exercise = exercise,
        updateFunction = { existingHistory ->
            viewModel.updateHistory(existingHistory)
        },
        onDismiss = onDismiss,
        modifier = modifier
    )
}

@Composable
fun RecordHistoryScreen(
    exercise: ExerciseUiState,
    saveFunction: (ExerciseHistory) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box {
        RecordHistoryCard(
            exerciseId = exercise.id,
            cardTitle = "New ${exercise.name} Workout",
            saveFunction = saveFunction,
            onDismiss = onDismiss,
            modifier = modifier
        )
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
fun EditHistoryScreen(
    history: ExerciseHistoryUiState,
    exercise: ExerciseUiState,
    updateFunction: (ExerciseHistory) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box {
        RecordHistoryCard(
            exerciseId = exercise.id,
            cardTitle = "Update ${exercise.name} Workout",
            saveFunction = updateFunction,
            onDismiss = onDismiss,
            modifier = modifier,
            savedHistory = history
        )
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
private fun RecordHistoryCard(
    exerciseId: Int,
    cardTitle: String,
    saveFunction: (ExerciseHistory) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    savedHistory: ExerciseHistoryUiState = ExerciseHistoryUiState()
) {
    val customCardElevation = CardDefaults.cardElevation(
        defaultElevation = 16.dp
    )
    var setsState by remember { mutableStateOf(if (savedHistory == ExerciseHistoryUiState()) "" else savedHistory.sets.toString()) }
    var repsState by remember { mutableStateOf(if (savedHistory == ExerciseHistoryUiState()) "" else savedHistory.reps.toString()) }
    var weightState by remember { mutableStateOf(if (savedHistory == ExerciseHistoryUiState()) "" else savedHistory.weight.toString()) }
    var unitState by remember { mutableStateOf(WeightUnits.KILOGRAMS.shortForm) }
    Card(
        modifier = modifier
            .padding(vertical = 10.dp, horizontal = 10.dp),
        elevation = customCardElevation
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth().padding(0.dp, 12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = cardTitle
            )
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically,
                modifier = modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 0.dp)
            ) {
                ExerciseInformationField(
                    label = "Sets",
                    value = setsState,
                    onChange = { entry ->
                        setsState = entry
                    },
                    modifier = Modifier
                        .weight(1f)
                        .padding(0.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                ExerciseInformationField(
                    label = "Reps",
                    value = repsState,
                    onChange = { entry ->
                        repsState = entry
                    },
                    modifier = Modifier
                        .weight(1f)
                        .padding(0.dp)
                )
            }
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically,
                modifier = modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 0.dp)
            ) {
                ExerciseInformationField(
                    label = "Weight",
                    value = weightState,
                    onChange = { entry ->
                        weightState = entry
                    },
                    modifier = Modifier
                        .weight(1f)
                        .padding(0.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                DropdownBox(
                    options = listOf(
                        WeightUnits.KILOGRAMS.shortForm,
                        WeightUnits.POUNDS.shortForm
                    ),
                    onChange = { value ->
                        unitState = value
                    },
                    modifier = Modifier
                        .weight(1f)
                        .padding(0.dp)
                        .semantics { contentDescription = "Units" }
                )
            }
            SaveHistoryButton(
                setsState = setsState,
                repsState = repsState,
                weightState = weightState,
                unitState = unitState,
                exerciseId = exerciseId,
                savedHistory = savedHistory,
                saveFunction = saveFunction,
                onDismiss = onDismiss
            )
        }
    }
}

@Composable
private fun SaveHistoryButton(
    setsState: String,
    repsState: String,
    weightState: String,
    unitState: String,
    exerciseId: Int,
    savedHistory: ExerciseHistoryUiState,
    saveFunction: (ExerciseHistory) -> Unit,
    onDismiss: () -> Unit
) {
    if (setsState != "" && repsState != "" && weightState != "" && unitState != "") {
        val weight = weightState.toDouble()
        val unit = getWeightUnitFromShortForm(unitState)
        val history = if (savedHistory == ExerciseHistoryUiState()) {
            ExerciseHistory(
                exerciseId = exerciseId,
                weight = convertToKilograms(unit, weight),
                sets = setsState.toInt(),
                reps = repsState.toInt(),
                date = LocalDate.now()
            )
        } else {
            savedHistory.sets = setsState.toInt()
            savedHistory.reps = repsState.toInt()
            savedHistory.weight = convertToKilograms(unit, weight)
            savedHistory.toExerciseHistory(exerciseId)
        }
        Button(onClick = {
            saveFunction(history)
            onDismiss()
        }) {
            Text("Save")
        }
    } else {
        Button(
            onClick = { },
            enabled = false
        ) {
            Text("Save")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ExerciseScreenPreview() {
    GymTrackerTheme(darkTheme = false) {
        RecordHistoryScreen(
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
