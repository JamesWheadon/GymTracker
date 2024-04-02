package com.askein.gymtracker.ui.workout.details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.askein.gymtracker.R
import com.askein.gymtracker.ui.ActionConfirmation
import com.askein.gymtracker.ui.AppViewModelProvider
import com.askein.gymtracker.ui.exercise.ExerciseCard
import com.askein.gymtracker.ui.exercise.ExerciseUiState
import com.askein.gymtracker.ui.navigation.NavigationRoute
import com.askein.gymtracker.ui.navigation.NavigationRoutes.WORKOUT_DETAILS_SCREEN
import com.askein.gymtracker.ui.navigation.TopBar
import com.askein.gymtracker.ui.theme.GymTrackerTheme
import com.askein.gymtracker.ui.visualisations.Calendar
import com.askein.gymtracker.ui.visualisations.MonthPicker
import com.askein.gymtracker.ui.workout.WorkoutUiState
import com.askein.gymtracker.ui.workout.create.CreateWorkoutForm
import com.askein.gymtracker.ui.workout.history.WorkoutHistoryScreen
import com.askein.gymtracker.ui.workout.history.WorkoutHistoryWithExercisesUiState
import com.askein.gymtracker.ui.workout.history.create.RecordWorkoutHistoryScreen
import java.time.LocalDate
import java.time.YearMonth

object WorkoutDetailsRoute : NavigationRoute {
    override val route =
        "${WORKOUT_DETAILS_SCREEN.baseRoute}/{${WORKOUT_DETAILS_SCREEN.navigationArgument}}"

    fun getRouteForNavArgument(navArgument: Int): String =
        "${WORKOUT_DETAILS_SCREEN.baseRoute}/${navArgument}"
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

@Composable
fun WorkoutDetailsScreen(
    uiState: WorkoutWithExercisesUiState,
    navController: NavHostController,
    exerciseNavigationFunction: (Int) -> Unit,
    updateWorkoutFunction: (WorkoutUiState) -> Unit,
    deleteWorkoutFunction: (WorkoutUiState) -> Unit,
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
                    contentDescription = stringResource(id = R.string.add_workout)
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
        EditWorkoutExercisesScreen(
            workout = uiState.toWorkoutUiState(),
            existingExercises = uiState.exercises,
            onDismiss = { showEditExercises = false }
        )
    }
    if (showUpdateWorkout) {
        Dialog(onDismissRequest = { showUpdateWorkout = false }) {
            CreateWorkoutForm(
                screenTitle = stringResource(id = R.string.update_workout, uiState.name),
                workout = uiState.toWorkoutUiState(),
                saveFunction = updateWorkoutFunction,
                onDismiss = { showUpdateWorkout = false }
            )
        }
    }
    if (showDeleteWorkout) {
        Dialog(onDismissRequest = { showDeleteWorkout = false }) {
            ActionConfirmation(
                actionTitle = stringResource(id = R.string.delete_workout, uiState.name),
                confirmFunction = {
                    deleteWorkoutFunction(uiState.toWorkoutUiState())
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
    var selectedWorkoutHistoryId by remember { mutableIntStateOf(-1) }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(vertical = 16.dp, horizontal = 16.dp)
    ) {
        Card(
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.padding(innerPadding)
        ) {
            Column {
                uiState.exercises.forEach { exercise ->
                    ExerciseCard(
                        exercise = exercise,
                        navigationFunction = exerciseNavigationFunction
                    )
                }
            }
        }
        Button(onClick = editExercises) {
            Text(text = stringResource(id = R.string.edit_exercises))
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
                .filter { history ->
                    history.date.year == selectedMonth.year &&
                            history.date.monthValue == selectedMonth.monthValue
                }.map { history -> history.date.dayOfMonth },
            dayFunction = { chosenDay ->
                selectedWorkoutHistoryId = uiState.workoutHistory.first {
                    it.date == LocalDate.of(
                        selectedMonth.year,
                        selectedMonth.monthValue,
                        chosenDay
                    )
                }.workoutHistoryId
            }
        )
        Spacer(modifier = Modifier.height(72.dp))
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
