package com.example.gymtracker.ui.workout.history.create

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.gymtracker.converters.WeightUnits
import com.example.gymtracker.converters.convertToKilograms
import com.example.gymtracker.converters.getWeightUnitFromShortForm
import com.example.gymtracker.data.exerciseHistory.weights.WeightsExerciseHistory
import com.example.gymtracker.data.workoutHistory.WorkoutHistory
import com.example.gymtracker.ui.AppViewModelProvider
import com.example.gymtracker.ui.DropdownBox
import com.example.gymtracker.ui.FormInformationField
import com.example.gymtracker.ui.customCardElevation
import com.example.gymtracker.ui.exercise.ExerciseUiState
import com.example.gymtracker.ui.exercise.history.ExerciseHistoryUiState
import com.example.gymtracker.ui.exercise.history.toExerciseHistory
import com.example.gymtracker.ui.theme.GymTrackerTheme
import com.example.gymtracker.ui.workout.details.WorkoutWithExercisesUiState
import com.example.gymtracker.ui.workout.history.WorkoutHistoryUiState
import com.example.gymtracker.ui.workout.history.WorkoutHistoryViewModel
import com.example.gymtracker.ui.workout.history.WorkoutHistoryWithExercisesUiState
import com.example.gymtracker.ui.workout.history.toWorkoutHistory
import com.example.gymtracker.ui.workout.history.toWorkoutUiState
import java.time.LocalDate

private const val RECORD_WORKOUT = "Record Workout"

@Composable
fun RecordWorkoutHistoryScreen(
    uiState: WorkoutWithExercisesUiState,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    workoutHistory: WorkoutHistoryWithExercisesUiState = WorkoutHistoryWithExercisesUiState(),
    titleText: String = RECORD_WORKOUT,
    viewModel: WorkoutHistoryViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    Box {
        Card(
            modifier = modifier
                .padding(vertical = 10.dp, horizontal = 10.dp),
            elevation = customCardElevation()
        ) {
            RecordWorkoutHistoryScreen(
                uiState = uiState,
                titleText = titleText,
                workoutHistory = workoutHistory,
                workoutSaveFunction = { workoutHistory, workoutExercises ->
                    viewModel.saveWorkoutHistory(
                        workoutHistory,
                        workoutExercises,
                        titleText == RECORD_WORKOUT
                    )
                },
                onDismiss = onDismiss
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

@Composable
private fun RecordWorkoutHistoryScreen(
    uiState: WorkoutWithExercisesUiState,
    titleText: String,
    workoutSaveFunction: (WorkoutHistory, List<WeightsExerciseHistory>) -> Unit,
    onDismiss: () -> Unit,
    workoutHistory: WorkoutHistoryWithExercisesUiState = WorkoutHistoryWithExercisesUiState()
) {
    val exerciseHistories: MutableList<ExerciseHistoryUiState> = remember { workoutHistory.exercises.toMutableStateList() }
    val exerciseErrors: MutableMap<Int, Boolean> = remember { mutableStateMapOf() }
    val workoutHistoryUiState = if (workoutHistory == WorkoutHistoryWithExercisesUiState()) {
        WorkoutHistoryUiState(workoutId = uiState.workoutId)
    } else {
        workoutHistory.toWorkoutUiState()
    }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(text = titleText)
        uiState.exercises.forEach { exercise ->
            RecordExerciseCard(
                exercise = exercise,
                savedExerciseHistory = workoutHistory.exercises
                    .firstOrNull { history -> history.exerciseId == exercise.id }
                    ?: ExerciseHistoryUiState(),
                selectExerciseFunction = { exerciseHistories.add(ExerciseHistoryUiState(exerciseId = exercise.id)) },
                deselectExerciseFunction = { exerciseHistories.removeIf { history -> history.exerciseId == exercise.id } },
                errorStateChange = { exerciseId, exerciseError -> exerciseErrors[exerciseId] = exerciseError },
                exerciseHistory = exerciseHistories.firstOrNull { history -> history.exerciseId == exercise.id }
            )
        }
        Button(
            onClick = {
                workoutSaveFunction(
                    workoutHistoryUiState.toWorkoutHistory(),
                    exerciseHistories.map { exerciseHistory -> exerciseHistory.toExerciseHistory() }
                )
                onDismiss()
            },
            enabled = !exerciseErrors.values.reduce { acc, error -> acc || error }
        ) {
            Text(text = "Save")
        }
    }
}

@Composable
fun RecordExerciseCard(
    exercise: ExerciseUiState,
    savedExerciseHistory: ExerciseHistoryUiState,
    selectExerciseFunction: () -> Boolean,
    deselectExerciseFunction: () -> Boolean,
    errorStateChange: (Int, Boolean) -> Unit,
    exerciseHistory: ExerciseHistoryUiState?
) {
    var expanded by remember { mutableStateOf(savedExerciseHistory != ExerciseHistoryUiState()) }
    var setsState by remember { mutableStateOf(savedExerciseHistory.sets.toString()) }
    var repsState by remember { mutableStateOf(savedExerciseHistory.reps.toString()) }
    var weightState by remember { mutableStateOf(savedExerciseHistory.weight.toString()) }
    var unitState by remember { mutableStateOf(WeightUnits.KILOGRAMS.shortForm) }
    val setsError = setsState == "" || setsState.toInt() < 1
    val repsError = repsState == "" || repsState.toInt() < 1
    val weightError = weightState == "" || weightState.toDoubleOrNull() == null
    Card {
        Column {
            Box(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = exercise.name,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .align(Alignment.Center),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Checkbox(
                    checked = expanded,
                    onCheckedChange = {
                        if (expanded) {
                            deselectExerciseFunction()
                        } else {
                            selectExerciseFunction()
                        }
                        expanded = !expanded
                    },
                    modifier = Modifier.align(Alignment.CenterEnd)
                )
            }
            if (expanded && exerciseHistory != null) {
                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 0.dp)
                ) {
                    FormInformationField(
                        label = "Sets",
                        value = setsState,
                        onChange = { entry ->
                            setsState = entry.replace("""[^0-9]""".toRegex(), "")
                            if (setsState != "") {
                                exerciseHistory.sets = setsState.toInt()
                            }
                        },
                        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                        error = setsError,
                        errorMessage = "Must be a positive number",
                        modifier = Modifier
                            .weight(1f)
                            .padding(0.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    FormInformationField(
                        label = "Reps",
                        value = repsState,
                        onChange = { entry ->
                            repsState = entry.replace("""[^0-9]""".toRegex(), "")
                            if (repsState != "") {
                                exerciseHistory.reps = repsState.toInt()
                            }
                        },
                        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                        error = repsError,
                        errorMessage = "Must be a positive number",
                        modifier = Modifier
                            .weight(1f)
                            .padding(0.dp)
                    )
                }
                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 0.dp)
                ) {
                    FormInformationField(
                        label = "Weight",
                        value = weightState,
                        onChange = { entry ->
                            weightState = entry.replace("""[^0-9-.]""".toRegex(), "")
                            if (weightState != "" && weightState.toDoubleOrNull() != null) {
                                exerciseHistory.weight = convertToKilograms(
                                    getWeightUnitFromShortForm(unitState),
                                    weightState.toDouble()
                                )
                            }
                        },
                        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Decimal),
                        error = weightError,
                        errorMessage = "Must be a number",
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
                            if (weightState != "") {
                                exerciseHistory.weight = convertToKilograms(
                                    getWeightUnitFromShortForm(unitState),
                                    weightState.toDouble()
                                )
                            }
                        },
                        modifier = Modifier
                            .weight(1f)
                            .padding(0.dp)
                            .semantics { contentDescription = "Units" }
                    )
                }
            }
        }
    }
    if (expanded) {
        errorStateChange(exercise.id, setsError || repsError || weightError)
    } else {
        errorStateChange(exercise.id, false)
    }
}

@Preview(showBackground = true)
@Composable
fun WorkoutsScreenPreview() {
    GymTrackerTheme(darkTheme = false) {
        RecordWorkoutHistoryScreen(
            uiState = WorkoutWithExercisesUiState(
                workoutId = 1,
                name = "Test",
                exercises = listOf(
                    ExerciseUiState(0, "Curls", "Biceps", "Dumbbells"),
                    ExerciseUiState(1, "Dips", "Triceps", "Dumbbells And Bars"),
                    ExerciseUiState(
                        2,
                        "Testing what happens if someone decides to have a ridiculously long exercise name",
                        "Lats",
                        "Dumbbells"
                    )
                ),
                workoutHistory = listOf(
                    WorkoutHistoryWithExercisesUiState(1, 1, LocalDate.now()),
                    WorkoutHistoryWithExercisesUiState(2, 1, LocalDate.now().minusDays(3))
                )
            ),
            titleText = RECORD_WORKOUT,
            workoutSaveFunction = { _, _ -> },
            onDismiss = {  }
        )
    }
}
