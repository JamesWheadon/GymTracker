package com.example.gymtracker.ui.workout.details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.gymtracker.ui.AppViewModelProvider
import com.example.gymtracker.ui.exercise.ExerciseCard
import com.example.gymtracker.ui.exercise.ExerciseUiState
import com.example.gymtracker.ui.navigation.NavigationArguments
import com.example.gymtracker.ui.navigation.NavigationRoute
import com.example.gymtracker.ui.navigation.TopBar
import com.example.gymtracker.ui.theme.GymTrackerTheme
import com.example.gymtracker.ui.workout.WorkoutWithExercisesUiState
import com.example.gymtracker.ui.workout.WorkoutsRoute

object WorkoutDetailsRoute : NavigationRoute {
    val navArgument = NavigationArguments.WORKOUTS_DETAILS_NAV_ARGUMENT.routeName
    override val route = "${WorkoutsRoute.route}/{${navArgument}}"

    fun getRouteForNavArgument(navArgument: Int): String = "${WorkoutsRoute.route}/${navArgument}"
}

@Composable
fun WorkoutDetailsScreen(
    modifier: Modifier = Modifier,
    exerciseNavigationFunction: (Int) -> Unit,
    viewModel: WorkoutDetailsViewModel = viewModel(
        factory = AppViewModelProvider.Factory
    )
) {
    val uiState = viewModel.uiState.collectAsState().value
    WorkoutDetailsScreen(
        uiState = uiState,
        exerciseNavigationFunction = exerciseNavigationFunction,
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkoutDetailsScreen(
    uiState: WorkoutWithExercisesUiState,
    exerciseNavigationFunction: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopBar(
                text = uiState.name,
                backEnabled = false,
                editEnabled = false,
                deleteEnabled = false
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = modifier
                .fillMaxWidth(),
            contentPadding = innerPadding,
            verticalArrangement = Arrangement.spacedBy(0.dp)
        ) {
            items(uiState.exercises) { exercise ->
                ExerciseCard(exercise = exercise, navigationFunction = exerciseNavigationFunction)
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun ExerciseScreenPreview() {
    GymTrackerTheme(darkTheme = false) {
        WorkoutDetailsScreen(
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
                )
            ),
            exerciseNavigationFunction = { }
        )
    }
}