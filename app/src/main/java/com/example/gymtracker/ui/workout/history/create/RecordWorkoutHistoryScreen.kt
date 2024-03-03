package com.example.gymtracker.ui.workout.history.create

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.gymtracker.ui.AppViewModelProvider
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
                if (exerciseHistories.size > 0) {
                    workoutSaveFunction(
                        WorkoutHistoryWithExercisesUiState(
                            workoutId = workoutHistoryUiState.workoutId,
                            workoutHistoryId = workoutHistoryUiState.workoutHistoryId,
                            date = workoutHistoryUiState.date,
                            exercises = exerciseHistories
                        )
                    )
                }
                onDismiss()
            },
            enabled = !exerciseErrors.values.reduce { acc, error -> acc || error }
        ) {
            Text(text = "Save")
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
