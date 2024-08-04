package com.askein.gymtracker.ui.workout.history.create

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateMap
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.askein.gymtracker.R
import com.askein.gymtracker.data.exercise.ExerciseType
import com.askein.gymtracker.enums.DistanceUnits
import com.askein.gymtracker.enums.WeightUnits
import com.askein.gymtracker.ui.AppViewModelProvider
import com.askein.gymtracker.ui.DatePickerDialog
import com.askein.gymtracker.ui.exercise.ExerciseUiState
import com.askein.gymtracker.ui.exercise.history.state.CardioExerciseHistoryUiState
import com.askein.gymtracker.ui.exercise.history.state.WeightsExerciseHistoryUiState
import com.askein.gymtracker.ui.exercise.history.state.record.RecordCardioHistoryState
import com.askein.gymtracker.ui.exercise.history.state.record.RecordWeightsHistoryState
import com.askein.gymtracker.ui.exercise.history.state.record.toRecordCardioHistoryState
import com.askein.gymtracker.ui.exercise.history.state.record.toRecordWeightsHistoryState
import com.askein.gymtracker.ui.theme.GymTrackerTheme
import com.askein.gymtracker.ui.user.LocalUserPreferences
import com.askein.gymtracker.ui.workout.details.WorkoutWithExercisesUiState
import com.askein.gymtracker.ui.workout.history.WorkoutHistoryUiState
import com.askein.gymtracker.ui.workout.history.WorkoutHistoryViewModel
import com.askein.gymtracker.ui.workout.history.WorkoutHistoryWithExercisesUiState
import com.askein.gymtracker.ui.workout.history.toWorkoutHistoryUiState
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
            elevation = CardDefaults.cardElevation(
                defaultElevation = 16.dp
            )
        ) {
            RecordWorkoutHistoryScreen(
                uiState = uiState,
                titleText = titleText,
                workoutHistory = workoutHistory,
                workoutSaveFunction = { workoutHistoryWithExercises ->
                    viewModel.saveWorkoutHistory(
                        workoutHistoryWithExercises,
                        workoutHistory,
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
                contentDescription = stringResource(id = R.string.close)
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
    val userPreferences = LocalUserPreferences.current
    val exerciseHistories =
        remember {
            workoutHistory.exerciseHistories.map { exerciseHistory ->
                when (exerciseHistory) {
                    is WeightsExerciseHistoryUiState -> {
                        exerciseHistory.exerciseId to exerciseHistory.toRecordWeightsHistoryState(
                            exerciseId = exerciseHistory.exerciseId,
                            recordWeight = exerciseHistory.weight.isNotEmpty(),
                            weightUnit = userPreferences.defaultWeightUnit
                        )
                    }

                    is CardioExerciseHistoryUiState -> {
                        exerciseHistory.exerciseId to exerciseHistory.toRecordCardioHistoryState(
                            exerciseId = exerciseHistory.exerciseId,
                            distanceUnit = userPreferences.defaultDistanceUnit
                        )
                    }
                }
            }.toMutableStateMap()
        }
    val workoutHistoryUiState = if (workoutHistory == WorkoutHistoryWithExercisesUiState()) {
        WorkoutHistoryUiState(workoutId = uiState.workoutId)
    } else {
        workoutHistory.toWorkoutHistoryUiState()
    }
    var date by remember { mutableStateOf(workoutHistory.date) }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text(text = titleText)
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .weight(weight = 1f, fill = false)
        ) {
            items(uiState.exercises) { exercise ->
                when (exercise.type) {
                    ExerciseType.WEIGHTS, ExerciseType.CALISTHENICS -> {
                        RecordWeightsExerciseCard(
                            exercise = exercise,
                            recordWeightsHistory = exerciseHistories[exercise.id] as? RecordWeightsHistoryState,
                            recordWeightsHistoryOnChange = { newState ->
                                exerciseHistories[exercise.id] = newState
                            },
                            selectExerciseFunction = {
                                exerciseHistories[exercise.id] =
                                    createEmptyRecordWeightsHistoryState(
                                        exerciseId = exercise.id,
                                        workoutHistoryId = workoutHistory.workoutHistoryId,
                                        date = workoutHistory.date,
                                        units = userPreferences.defaultWeightUnit,
                                        recordWeight = true
                                    )
                            },
                            deselectExerciseFunction = { exerciseHistories.remove(exercise.id) }
                        )
                    }

                    ExerciseType.CARDIO -> {
                        RecordCardioExerciseCard(
                            exercise = exercise,
                            recordCardioHistory = exerciseHistories[exercise.id] as? RecordCardioHistoryState,
                            recordCardioHistoryOnChange = { newState ->
                                exerciseHistories[exercise.id] = newState
                            },
                            selectExerciseFunction = {
                                exerciseHistories[exercise.id] =
                                    createEmptyRecordCardioHistoryState(
                                        exerciseId = exercise.id,
                                        workoutHistoryId = workoutHistory.workoutHistoryId,
                                        date = workoutHistory.date,
                                        units = userPreferences.defaultDistanceUnit,
                                    )

                            },
                            deselectExerciseFunction = { exerciseHistories.remove(exercise.id) }
                        )
                    }
                }
            }
        }
        DatePickerDialog(
            date = date,
            onDateChange = { newDate -> date = newDate }
        )
        Button(
            onClick = {
                if (exerciseHistories.size > 0) {
                    val historyUiStates =
                        exerciseHistories.values.map { exercise -> exercise.toHistoryUiState(date) }
                    workoutSaveFunction(
                        WorkoutHistoryWithExercisesUiState(
                            workoutId = workoutHistoryUiState.workoutId,
                            workoutHistoryId = workoutHistoryUiState.workoutHistoryId,
                            date = date,
                            exerciseHistories = historyUiStates
                        )
                    )
                }
                onDismiss()
            },
            enabled = exerciseHistories.values
                .map { recordHistoryState -> recordHistoryState.isValid() }
                .none { valid -> !valid }
        ) {
            Text(text = stringResource(id = R.string.save))
        }
    }
}

fun createEmptyRecordWeightsHistoryState(
    exerciseId: Int,
    workoutHistoryId: Int,
    date: LocalDate,
    units: WeightUnits,
    recordWeight: Boolean
) = RecordWeightsHistoryState(
    historyId = 0,
    exerciseId = exerciseId,
    workoutHistoryId = workoutHistoryId,
    dateState = date,
    rest = null,
    setsState = "",
    repsState = mutableStateListOf(),
    minutesState = mutableStateListOf(),
    secondsState = mutableStateListOf(),
    weightsState = mutableStateListOf(),
    unitState = units,
    recordReps = true,
    recordWeight = recordWeight
)

fun createEmptyRecordCardioHistoryState(
    exerciseId: Int,
    workoutHistoryId: Int,
    date: LocalDate,
    units: DistanceUnits
) = RecordCardioHistoryState(
    historyId = 0,
    exerciseId = exerciseId,
    workoutHistoryId = workoutHistoryId,
    dateState = date,
    minutesState = "",
    secondsState = "",
    caloriesState = "",
    distanceState = "",
    unitState = units
)

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
