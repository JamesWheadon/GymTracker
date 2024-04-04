package com.askein.gymtracker.ui.exercise

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.askein.gymtracker.ui.AppViewModelProvider
import com.askein.gymtracker.ui.exercise.create.CreateExerciseScreen
import com.askein.gymtracker.ui.theme.GymTrackerTheme

@Composable
fun ExercisesScreen(
    exerciseNavigationFunction: (Int) -> Unit,
    showCreateExercise: Boolean,
    dismissCreateExercise: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ExercisesScreenViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val exerciseListUiState by viewModel.exerciseListUiState.collectAsState()
    ExercisesScreen(
        exerciseListUiState = exerciseListUiState,
        showCreateExercise = showCreateExercise,
        dismissCreateExercise = dismissCreateExercise,
        exerciseNavigationFunction = exerciseNavigationFunction,
        modifier = modifier
    )
}

@Composable
fun ExercisesScreen(
    exerciseListUiState: ExerciseListUiState,
    showCreateExercise: Boolean,
    dismissCreateExercise: () -> Unit,
    exerciseNavigationFunction: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    ExercisesScreen(
        exerciseListUiState = exerciseListUiState,
        exerciseNavigationFunction = exerciseNavigationFunction,
        modifier = modifier
    )
    if (showCreateExercise) {
        Dialog(
            onDismissRequest = dismissCreateExercise
        ) {
            CreateExerciseScreen(
                onDismiss = dismissCreateExercise
            )
        }
    }
}

@Composable
fun ExercisesScreen(
    exerciseListUiState: ExerciseListUiState,
    exerciseNavigationFunction: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.padding(vertical = 16.dp, horizontal = 16.dp)
    ) {
        LazyColumn(
            modifier = modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(0.dp)
        ) {
            items(exerciseListUiState.exerciseList.sortedBy { it.name }) { exercise ->
                ExerciseCard(
                    exercise = exercise,
                    navigationFunction = exerciseNavigationFunction
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ExerciseScreenPreview() {
    GymTrackerTheme(darkTheme = false) {
        ExercisesScreen(
            exerciseListUiState = ExerciseListUiState(
                listOf(
                    ExerciseUiState(0, "Curls", "Biceps", "Dumbbells"),
                    ExerciseUiState(1, "Dips", "Triceps", "Dumbbells And Bars"),
                    ExerciseUiState(
                        2,
                        "Testing what happens if someone decides to have a ridiculously long exercise name",
                        "Lats",
                        "Dumbbells"
                    ),
                )
            ),
            exerciseNavigationFunction = { }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ExerciseScreenCardPreview() {
    GymTrackerTheme(darkTheme = false) {
        ExercisesScreen(
            exerciseListUiState = ExerciseListUiState(
                listOf(
                    ExerciseUiState(0, "Curls", "Biceps", "Dumbbells"),
                    ExerciseUiState(1, "Dips", "Triceps", "Dumbbells And Bars"),
                    ExerciseUiState(
                        2,
                        "Testing what happens if someone decides to have a ridiculously long exercise name",
                        "Lats",
                        "Dumbbells"
                    ),
                )
            ),
            exerciseNavigationFunction = { }
        )
    }
}
