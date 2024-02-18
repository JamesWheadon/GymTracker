package com.example.gymtracker.ui.workout.history.create

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.material3.MaterialTheme
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
import com.example.gymtracker.converters.DistanceUnits
import com.example.gymtracker.converters.WeightUnits
import com.example.gymtracker.converters.convertToKilograms
import com.example.gymtracker.converters.convertToKilometers
import com.example.gymtracker.converters.getDistanceUnitFromShortForm
import com.example.gymtracker.converters.getWeightUnitFromShortForm
import com.example.gymtracker.ui.AppViewModelProvider
import com.example.gymtracker.ui.DropdownBox
import com.example.gymtracker.ui.FormInformationField
import com.example.gymtracker.ui.FormTimeField
import com.example.gymtracker.ui.customCardElevation
import com.example.gymtracker.ui.exercise.ExerciseUiState
import com.example.gymtracker.ui.exercise.history.state.CardioExerciseHistoryUiState
import com.example.gymtracker.ui.exercise.history.state.ExerciseHistoryUiState
import com.example.gymtracker.ui.exercise.history.state.WeightsExerciseHistoryUiState
import com.example.gymtracker.ui.theme.GymTrackerTheme
import com.example.gymtracker.ui.workout.details.WorkoutWithExercisesUiState
import com.example.gymtracker.ui.workout.history.WorkoutHistoryUiState
import com.example.gymtracker.ui.workout.history.WorkoutHistoryViewModel
import com.example.gymtracker.ui.workout.history.WorkoutHistoryWithExercisesUiState
import com.example.gymtracker.ui.workout.history.toWorkoutHistoryUiState
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
                workoutSaveFunction = { workoutHistoryWithExercises ->
                    viewModel.saveWorkoutHistory(
                        workoutHistoryWithExercises,
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
fun RecordWorkoutHistoryScreen(
    uiState: WorkoutWithExercisesUiState,
    titleText: String,
    workoutSaveFunction: (WorkoutHistoryWithExercisesUiState) -> Unit,
    onDismiss: () -> Unit,
    workoutHistory: WorkoutHistoryWithExercisesUiState = WorkoutHistoryWithExercisesUiState()
) {
    val exerciseHistories: MutableList<ExerciseHistoryUiState> =
        remember { workoutHistory.exercises.toMutableStateList() }
    val exerciseErrors: MutableMap<Int, Boolean> = remember { mutableStateMapOf() }
    val workoutHistoryUiState = if (workoutHistory == WorkoutHistoryWithExercisesUiState()) {
        WorkoutHistoryUiState(workoutId = uiState.workoutId)
    } else {
        workoutHistory.toWorkoutHistoryUiState()
    }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(text = titleText)
        uiState.exercises.forEach { exercise ->
            if (exercise.equipment != "") {
                RecordWeightsExerciseCard(
                    exercise = exercise,
                    selectExerciseFunction = {
                        exerciseHistories.add(
                            WeightsExerciseHistoryUiState(
                                exerciseId = exercise.id
                            )
                        )
                    },
                    deselectExerciseFunction = { exerciseHistories.removeIf { history -> history.exerciseId == exercise.id } },
                    errorStateChange = { exerciseId, exerciseError ->
                        exerciseErrors[exerciseId] = exerciseError
                    },
                    exerciseHistory = exerciseHistories.firstOrNull { history -> history.exerciseId == exercise.id } as? WeightsExerciseHistoryUiState
                )
            } else {
                RecordCardioExerciseCard(
                    exercise = exercise,
                    selectExerciseFunction = {
                        exerciseHistories.add(
                            CardioExerciseHistoryUiState(
                                exerciseId = exercise.id
                            )
                        )
                    },
                    deselectExerciseFunction = { exerciseHistories.removeIf { history -> history.exerciseId == exercise.id } },
                    errorStateChange = { exerciseId, exerciseError ->
                        exerciseErrors[exerciseId] = exerciseError
                    },
                    exerciseHistory = exerciseHistories.firstOrNull { history -> history.exerciseId == exercise.id } as? CardioExerciseHistoryUiState
                )
            }
        }
        Button(
            onClick = {
                workoutSaveFunction(
                    WorkoutHistoryWithExercisesUiState(
                        workoutId = workoutHistoryUiState.workoutId,
                        workoutHistoryId = workoutHistoryUiState.workoutHistoryId,
                        date = workoutHistoryUiState.date,
                        exercises = exerciseHistories
                    )
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
fun RecordWeightsExerciseCard(
    exercise: ExerciseUiState,
    exerciseHistory: WeightsExerciseHistoryUiState?,
    selectExerciseFunction: () -> Unit,
    deselectExerciseFunction: () -> Unit,
    errorStateChange: (Int, Boolean) -> Unit
) {
    var expanded by remember { mutableStateOf(exerciseHistory != null) }
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
            if (exerciseHistory != null) {
                var setsState by remember { mutableStateOf(exerciseHistory.sets.toString()) }
                var repsState by remember { mutableStateOf(exerciseHistory.reps.toString()) }
                var weightState by remember { mutableStateOf(exerciseHistory.weight.toString()) }
                var unitState by remember { mutableStateOf(WeightUnits.KILOGRAMS.shortForm) }
                val setsError = setsState == "" || setsState.toInt() < 1
                val repsError = repsState == "" || repsState.toInt() < 1
                val weightError = weightState == "" || weightState.toDoubleOrNull() == null
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
                errorStateChange(exercise.id, setsError || repsError || weightError)
            } else {
                errorStateChange(exercise.id, false)
            }
        }
    }
}

@Composable
fun RecordCardioExerciseCard(
    exercise: ExerciseUiState,
    exerciseHistory: CardioExerciseHistoryUiState?,
    selectExerciseFunction: () -> Unit,
    deselectExerciseFunction: () -> Unit,
    errorStateChange: (Int, Boolean) -> Unit
) {
    var expanded by remember { mutableStateOf(exerciseHistory != null) }
    Card {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(vertical = 8.dp)
        ) {
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
            if (exerciseHistory != null) {
                var minutesState by remember {
                    mutableStateOf(
                        exerciseHistory.minutes?.toString() ?: ""
                    )
                }
                var secondsState by remember {
                    mutableStateOf(
                        exerciseHistory.seconds?.toString() ?: ""
                    )
                }
                var distanceState by remember {
                    mutableStateOf(
                        exerciseHistory.distance?.toString() ?: ""
                    )
                }
                var caloriesState by remember {
                    mutableStateOf(
                        exerciseHistory.calories?.toString() ?: ""
                    )
                }
                var unitState by remember { mutableStateOf(DistanceUnits.KILOMETERS.shortForm) }
                val error = !((minutesState != "" && secondsState != "")
                        || distanceState != ""
                        || caloriesState != "")
                FormTimeField(
                    minutes = minutesState,
                    seconds = secondsState,
                    minutesOnChange = { minutes ->
                        minutesState = Regex("[^0-9]").replace(minutes, "")
                        if (minutesState != "") {
                            exerciseHistory.minutes = minutesState.toInt()
                        }
                    },
                    secondsOnChange = { seconds ->
                        secondsState = Regex("[^0-9]").replace(seconds, "")
                        if (secondsState != "") {
                            exerciseHistory.seconds = secondsState.toInt()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 0.dp)
                )
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.Top,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 0.dp)
                ) {
                    FormInformationField(
                        label = "Distance",
                        value = distanceState,
                        onChange = { entry ->
                            distanceState = Regex("[^0-9.]").replace(entry, "")
                            if (distanceState != "" && distanceState.toDoubleOrNull() != null) {
                                exerciseHistory.distance = convertToKilometers(
                                    getDistanceUnitFromShortForm(unitState),
                                    distanceState.toDouble()
                                )
                            }
                        },
                        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Decimal),
                        modifier = Modifier
                            .weight(1f)
                            .height(intrinsicSize = IntrinsicSize.Max)
                            .padding(0.dp)
                    )
                    DropdownBox(
                        options = listOf(
                            DistanceUnits.METERS.shortForm,
                            DistanceUnits.KILOMETERS.shortForm,
                            DistanceUnits.MILES.shortForm
                        ),
                        onChange = { value ->
                            unitState = value
                        },
                        modifier = Modifier
                            .weight(1f)
                            .padding(0.dp)
                            .height(intrinsicSize = IntrinsicSize.Max)
                            .semantics { contentDescription = "Units" }
                    )
                }
                FormInformationField(
                    label = "Calories",
                    value = caloriesState,
                    onChange = { entry ->
                        caloriesState = Regex("[^0-9]").replace(entry, "")
                        if (caloriesState != "") {
                            exerciseHistory.calories = caloriesState.toInt()
                        }
                    },
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 0.dp)
                )
                if (error) {
                    Text(
                        text = "Must have a time, distance or calories entered",
                        color = MaterialTheme.colorScheme.error
                    )
                }
                errorStateChange(exercise.id, error)
            } else {
                errorStateChange(exercise.id, false)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RecordWorkoutHistoryScreenPreview() {
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
                        "Testing what happens if someone decides to have a ridiculously long exercise name"
                    )
                ),
                workoutHistory = listOf(
                    WorkoutHistoryWithExercisesUiState(1, 1, LocalDate.now()),
                    WorkoutHistoryWithExercisesUiState(2, 1, LocalDate.now().minusDays(3))
                )
            ),
            titleText = RECORD_WORKOUT,
            workoutSaveFunction = { },
            onDismiss = { }
        )
    }
}
