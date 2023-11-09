package com.example.gymtracker.ui.workout.details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.gymtracker.data.workout.Workout
import com.example.gymtracker.ui.ActionConfirmation
import com.example.gymtracker.ui.AppViewModelProvider
import com.example.gymtracker.ui.exercise.ExerciseCard
import com.example.gymtracker.ui.exercise.ExerciseUiState
import com.example.gymtracker.ui.navigation.NavigationArguments
import com.example.gymtracker.ui.navigation.NavigationRoute
import com.example.gymtracker.ui.navigation.TopBar
import com.example.gymtracker.ui.theme.GymTrackerTheme
import com.example.gymtracker.ui.workout.WorkoutWithExercisesUiState
import com.example.gymtracker.ui.workout.WorkoutsRoute
import com.example.gymtracker.ui.workout.create.CreateWorkoutForm
import com.example.gymtracker.ui.workout.toWorkout

object WorkoutDetailsRoute : NavigationRoute {
    val navArgument = NavigationArguments.WORKOUTS_DETAILS_NAV_ARGUMENT.route
    override val route = "${WorkoutsRoute.route}/{${navArgument}}"

    fun getRouteForNavArgument(navArgument: Int): String = "${WorkoutsRoute.route}/${navArgument}"
}

@Composable
fun WorkoutDetailsScreen(
    navController: NavHostController,
    exerciseNavigationFunction: (Int) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: WorkoutDetailsViewModel = viewModel(
        factory = AppViewModelProvider.Factory
    )
) {
    val uiState = viewModel.uiState.collectAsState().value
    WorkoutDetailsScreen(
        uiState = uiState,
        navController = navController,
        exerciseNavigationFunction = exerciseNavigationFunction,
        updateWorkoutFunction = { workout -> viewModel.updateWorkout(workout) },
        deleteWorkoutFunction = { workout -> viewModel.deleteWorkout(workout) },
        modifier = modifier,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkoutDetailsScreen(
    uiState: WorkoutWithExercisesUiState,
    navController: NavHostController,
    exerciseNavigationFunction: (Int) -> Unit,
    updateWorkoutFunction: (Workout) -> Unit,
    deleteWorkoutFunction: (Workout) -> Unit,
    modifier: Modifier = Modifier
) {
    var showAddExercise by remember { mutableStateOf(false) }
    var showUpdateWorkout by remember { mutableStateOf(false) }
    var showDeleteWorkout by remember { mutableStateOf(false) }
    Scaffold(
        modifier = modifier,
        topBar = {
            TopBar(
                text = uiState.name,
                navController = navController,
                editFunction = { showUpdateWorkout = true },
                deleteFunction = { showDeleteWorkout = true }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddExercise = true },
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier.padding(20.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    tint = Color.Black,
                    contentDescription = "Add Exercise"
                )
            }
        }
    ) { innerPadding ->
        WorkoutDetailsScreen(
            uiState = uiState,
            exerciseNavigationFunction = exerciseNavigationFunction,
            innerPadding = innerPadding,
            modifier = modifier
        )
    }
    if (showAddExercise) {
        Dialog(
            onDismissRequest = { showAddExercise = false }
        ) {
            AddWorkoutExercisesScreen(
                workout = uiState.toWorkout(),
                existingExercises = uiState.exercises,
                onDismiss = { showAddExercise = false }
            )
        }
    }
    if (showUpdateWorkout) {
        Dialog(
            onDismissRequest = { showUpdateWorkout = false }
        ) {
            CreateWorkoutForm(
                screenTitle = "Update Workout",
                workout = uiState.toWorkout(),
                saveFunction = updateWorkoutFunction,
                onDismiss = { showUpdateWorkout = false }
            )
        }
    }
    if (showDeleteWorkout) {
        Dialog(
            onDismissRequest = { showDeleteWorkout = false }
        ) {
            ActionConfirmation(
                actionTitle = "Delete ${uiState.name} Workout?",
                confirmFunction = {
                    deleteWorkoutFunction(uiState.toWorkout())
                    navController.popBackStack()
                },
                cancelFunction = { showDeleteWorkout = false })
        }
    }
}

@Composable
private fun WorkoutDetailsScreen(
    uiState: WorkoutWithExercisesUiState,
    exerciseNavigationFunction: (Int) -> Unit,
    innerPadding: PaddingValues,
    modifier: Modifier = Modifier
) {
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

@Preview(showBackground = true)
@Composable
fun WorkoutDetailsScreenPreview() {
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
            exerciseNavigationFunction = { },
            innerPadding = PaddingValues()
        )
    }
}
