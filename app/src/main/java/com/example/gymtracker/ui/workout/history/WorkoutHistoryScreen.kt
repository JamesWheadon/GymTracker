package com.example.gymtracker.ui.workout.history

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.gymtracker.ui.customCardElevation
import com.example.gymtracker.ui.exercise.ExerciseUiState
import com.example.gymtracker.ui.exercise.details.HistoryDetails
import com.example.gymtracker.ui.exercise.history.ExerciseHistoryUiState
import com.example.gymtracker.ui.theme.GymTrackerTheme
import java.time.LocalDate

@Composable
fun WorkoutHistoryScreen(
    uiState: WorkoutHistoryWithExercisesUiState,
    exercises: List<ExerciseUiState>,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box {
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
}

@Composable
fun WorkoutHistoryExerciseCard(
    exercise: ExerciseUiState,
    exerciseHistory: ExerciseHistoryUiState
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(text = exercise.name)
        HistoryDetails(
            exerciseHistory = exerciseHistory,
            exercise = exercise,
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
                    ExerciseHistoryUiState(1, 1, 1.0, 1, 1, 1, LocalDate.now()),
                    ExerciseHistoryUiState(2, 2, 1.0, 1, 1, 1, LocalDate.now())
                )
            ),
            exercises = listOf(
                ExerciseUiState(1, "Curls", "Biceps", "Dumbbells"),
                ExerciseUiState(2, "Dips", "Triceps", "Bars")
            ),
            onDismiss = { }
        )
    }
}
