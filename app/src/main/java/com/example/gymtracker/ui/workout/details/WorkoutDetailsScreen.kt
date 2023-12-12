package com.example.gymtracker.ui.workout.details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.gymtracker.data.workout.Workout
import com.example.gymtracker.ui.ActionConfirmation
import com.example.gymtracker.ui.AppViewModelProvider
import com.example.gymtracker.ui.exercise.ExerciseCard
import com.example.gymtracker.ui.exercise.ExerciseUiState
import com.example.gymtracker.ui.navigation.NavigationRoute
import com.example.gymtracker.ui.navigation.NavigationRoutes.WORKOUT_DETAILS_SCREEN
import com.example.gymtracker.ui.navigation.TopBar
import com.example.gymtracker.ui.theme.GymTrackerTheme
import com.example.gymtracker.ui.visualisations.Calendar
import com.example.gymtracker.ui.visualisations.MonthPicker
import com.example.gymtracker.ui.workout.WorkoutsRoute
import com.example.gymtracker.ui.workout.create.CreateWorkoutForm
import com.example.gymtracker.ui.workout.history.WorkoutHistoryScreen
import com.example.gymtracker.ui.workout.history.WorkoutHistoryWithExercisesUiState
import com.example.gymtracker.ui.workout.history.create.RecordWorkoutHistoryScreen
import java.time.LocalDate
import java.time.YearMonth

object WorkoutDetailsRoute : NavigationRoute {
    val navArgument = WORKOUT_DETAILS_SCREEN.baseRoute
    override val route = "${WorkoutsRoute.route}/{${navArgument}}"

    fun getRouteForNavArgument(navArgument: Int): String = "${WORKOUT_DETAILS_SCREEN.baseRoute}/${navArgument}"
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
    var showEditExercises by remember { mutableStateOf(false) }
    var showUpdateWorkout by remember { mutableStateOf(false) }
    var showDeleteWorkout by remember { mutableStateOf(false) }
    var showRecordWorkout by remember { mutableStateOf(false) }
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
                onClick = { showRecordWorkout = true },
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier.padding(20.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    tint = Color.Black,
                    contentDescription = "Add Workout"
                )
            }
        }
    ) { innerPadding ->
        WorkoutDetailsScreen(
            uiState = uiState,
            exerciseNavigationFunction = exerciseNavigationFunction,
            editExercises = { showEditExercises = true },
            innerPadding = innerPadding,
            modifier = modifier
        )
    }
    if (showEditExercises) {
        Dialog(onDismissRequest = { showEditExercises = false }) {
            EditWorkoutExercisesScreen(
                workout = uiState.toWorkout(),
                existingExercises = uiState.exercises,
                onDismiss = { showEditExercises = false }
            )
        }
    }
    if (showUpdateWorkout) {
        Dialog(onDismissRequest = { showUpdateWorkout = false }) {
            CreateWorkoutForm(
                screenTitle = "Update Workout",
                workout = uiState.toWorkout(),
                saveFunction = updateWorkoutFunction,
                onDismiss = { showUpdateWorkout = false }
            )
        }
    }
    if (showDeleteWorkout) {
        Dialog(onDismissRequest = { showDeleteWorkout = false }) {
            ActionConfirmation(
                actionTitle = "Delete ${uiState.name} Workout?",
                confirmFunction = {
                    deleteWorkoutFunction(uiState.toWorkout())
                    navController.popBackStack()
                },
                cancelFunction = { showDeleteWorkout = false }
            )
        }
    }
    if (showRecordWorkout) {
        Dialog(
            onDismissRequest = { showRecordWorkout = false },
            properties = DialogProperties(usePlatformDefaultWidth = false)
        ) {
            RecordWorkoutHistoryScreen(
                uiState = uiState,
                onDismiss = { showRecordWorkout = false },
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

@Composable
private fun WorkoutDetailsScreen(
    uiState: WorkoutWithExercisesUiState,
    exerciseNavigationFunction: (Int) -> Unit,
    editExercises: () -> Unit,
    innerPadding: PaddingValues,
    modifier: Modifier = Modifier
) {
    var selectedMonth by remember { mutableStateOf(YearMonth.now()) }
    var selectedWorkoutHistoryId by remember { mutableStateOf(-1) }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
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
        Button(onClick = editExercises) {
            Text(text = "Edit Exercises")
        }
        if (selectedWorkoutHistoryId != -1) {
            WorkoutHistoryScreen(
                workoutHistoryUiState = uiState.workoutHistory.first { workoutHistory -> workoutHistory.workoutHistoryId == selectedWorkoutHistoryId },
                workoutUiState = uiState,
                onDismiss = { selectedWorkoutHistoryId = -1 }
            )
        }
        MonthPicker(
            yearMonthValue = selectedMonth,
            yearMonthValueOnChange = { chosen -> selectedMonth = chosen }
        )
        Calendar(
            month = selectedMonth.monthValue,
            year = selectedMonth.year,
            activeDays = uiState.workoutHistory
                .filter {
                        history -> history.date.year == selectedMonth.year &&
                        history.date.monthValue == selectedMonth.monthValue
                }.map { history -> history.date.dayOfMonth },
            dayFunction = { chosenDay -> selectedWorkoutHistoryId = uiState.workoutHistory.first { it.date == LocalDate.of(selectedMonth.year, selectedMonth.monthValue, chosenDay) }.workoutHistoryId }
        )
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
                ),
                workoutHistory = listOf(
                    WorkoutHistoryWithExercisesUiState(1, 1, LocalDate.now()),
                    WorkoutHistoryWithExercisesUiState(2, 1, LocalDate.now().minusDays(3))
                )
            ),
            exerciseNavigationFunction = { },
            editExercises = { },
            innerPadding = PaddingValues()
        )
    }
}
