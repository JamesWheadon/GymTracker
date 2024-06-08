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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.askein.gymtracker.R
import com.askein.gymtracker.data.exercise.ExerciseType
import com.askein.gymtracker.ui.AppViewModelProvider
import com.askein.gymtracker.ui.DatePickerDialog
import com.askein.gymtracker.ui.customCardElevation
import com.askein.gymtracker.ui.exercise.ExerciseUiState
import com.askein.gymtracker.ui.exercise.history.state.CardioExerciseHistoryUiState
import com.askein.gymtracker.ui.exercise.history.state.ExerciseHistoryUiState
import com.askein.gymtracker.ui.exercise.history.state.WeightsExerciseHistoryUiState
import com.askein.gymtracker.ui.theme.GymTrackerTheme
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
            elevation = customCardElevation()
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
    val exerciseHistories: MutableList<ExerciseHistoryUiState> =
        remember { workoutHistory.exercises.toMutableStateList() }
    val exerciseErrors: MutableMap<Int, Boolean> = remember { mutableStateMapOf() }
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
                    ExerciseType.WEIGHTS -> {
                        RecordWeightsExerciseCard(
                            exercise = exercise,
                            exerciseHistory = exerciseHistories.firstOrNull { history -> history.exerciseId == exercise.id } as? WeightsExerciseHistoryUiState,
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
                        )
                    }

                    ExerciseType.CARDIO -> {
                        RecordCardioExerciseCard(
                            exercise = exercise,
                            exerciseHistory = exerciseHistories.firstOrNull { history -> history.exerciseId == exercise.id } as? CardioExerciseHistoryUiState,
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
                            }
                        )
                    }

                    ExerciseType.CALISTHENICS -> {
                        RecordWeightsExerciseCard(
                            exercise = exercise,
                            exerciseHistory = exerciseHistories.firstOrNull { history -> history.exerciseId == exercise.id } as? WeightsExerciseHistoryUiState,
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
                            recordWeight = false
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
                    exerciseHistories.map { exercise -> exercise.date = date }
                    workoutSaveFunction(
                        WorkoutHistoryWithExercisesUiState(
                            workoutId = workoutHistoryUiState.workoutId,
                            workoutHistoryId = workoutHistoryUiState.workoutHistoryId,
                            date = date,
                            exercises = exerciseHistories
                        )
                    )
                }
                onDismiss()
            },
            enabled = !(exerciseErrors.values.reduceOrNull { acc, error -> acc || error } ?: true)
        ) {
            Text(text = stringResource(id = R.string.save))
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
