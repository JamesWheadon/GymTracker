package com.askein.gymtracker.ui.workout.history.create.live

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.askein.gymtracker.R
import com.askein.gymtracker.ui.ActionConfirmation
import com.askein.gymtracker.ui.AppViewModelProvider
import com.askein.gymtracker.ui.exercise.ExerciseUiState
import com.askein.gymtracker.ui.exercise.history.state.ExerciseHistoryUiState
import com.askein.gymtracker.ui.home.HomeScreenRoute
import com.askein.gymtracker.ui.navigation.NavigationRoute
import com.askein.gymtracker.ui.navigation.NavigationRoutes.LIVE_RECORD_WORKOUT_SCREEN
import com.askein.gymtracker.ui.navigation.TopBar
import com.askein.gymtracker.ui.theme.GymTrackerTheme
import com.askein.gymtracker.ui.workout.details.WorkoutDetailsRoute
import com.askein.gymtracker.ui.workout.details.WorkoutDetailsViewModel
import com.askein.gymtracker.ui.workout.details.WorkoutWithExercisesUiState
import com.askein.gymtracker.ui.workout.history.WorkoutHistoryUiState
import com.askein.gymtracker.ui.workout.history.WorkoutHistoryViewModel
import com.askein.gymtracker.ui.workout.history.WorkoutHistoryWithExercisesUiState
import java.time.LocalDate

object LiveRecordWorkoutRoute : NavigationRoute {
    override val route =
        "${LIVE_RECORD_WORKOUT_SCREEN.baseRoute}/{${LIVE_RECORD_WORKOUT_SCREEN.navigationArgument}}"

    fun getRouteForNavArgument(navArgument: Int): String =
        "${LIVE_RECORD_WORKOUT_SCREEN.baseRoute}/${navArgument}"
}

@Composable
fun LiveRecordWorkout(
    navController: NavHostController,
    detailsViewModel: WorkoutDetailsViewModel = viewModel(factory = AppViewModelProvider.Factory),
    historyViewModel: WorkoutHistoryViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val uiState = detailsViewModel.uiState.collectAsState().value
    LaunchedEffect(uiState.workoutId != 0) {
        historyViewModel.liveSaveWorkoutHistory(WorkoutHistoryUiState(workoutId = uiState.workoutId))
    }
    Scaffold(
        topBar = {
            TopBar(
                text = uiState.name,
                navController = navController
            )
        }
    ) { innerPadding ->
        LiveRecordWorkout(
            uiState = uiState,
            saveFunction = { exercise ->
                historyViewModel.liveSaveWorkoutExerciseHistory(exercise)
            },
            finishFunction = {
                navController.popBackStack()
                navController.navigate(WorkoutDetailsRoute.getRouteForNavArgument(uiState.workoutId))
            },
            cancelFunction = {
                historyViewModel.liveDeleteWorkoutHistory()
                navController.popBackStack()
                navController.navigate(HomeScreenRoute.route)
            },
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxWidth()
        )
    }
}

@Composable
fun LiveRecordWorkout(
    uiState: WorkoutWithExercisesUiState,
    saveFunction: (ExerciseHistoryUiState) -> Unit,
    finishFunction: () -> Unit,
    cancelFunction: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showDeleteWorkout by remember { mutableStateOf(false) }
    val completedExercises = rememberSaveable(saver = IntListSaver) { mutableStateListOf() }
    var currentExercise by rememberSaveable { mutableIntStateOf(-1) }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier.verticalScroll(rememberScrollState()).padding(end = 72.dp)
    ) {
        uiState.exercises.forEach { exercise ->
            if (exercise.id == currentExercise) {
                if (exercise.equipment != "") {
                    LiveRecordWeightsExercise(
                        uiState = exercise,
                        exerciseComplete = { exerciseHistory ->
                            saveFunction(exerciseHistory)
                            completedExercises.add(exerciseHistory.exerciseId)
                            currentExercise = -1
                        },
                        exerciseCancel = {
                            currentExercise = -1
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                } else {
                    LiveRecordCardioExercise(
                        uiState = exercise,
                        exerciseComplete = { exerciseHistory ->
                            saveFunction(exerciseHistory)
                            completedExercises.add(exerciseHistory.exerciseId)
                            currentExercise = -1
                        },
                        exerciseCancel = {
                            currentExercise = -1
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            } else if (completedExercises.contains(exercise.id)) {
                LiveRecordWorkoutExerciseCard(
                    exercise = exercise,
                    completed = true
                )
            } else {
                LiveRecordWorkoutExerciseCard(
                    exercise = exercise,
                    completed = false,
                    recording = currentExercise != -1,
                    startFunction = { currentExercise = exercise.id }
                )
            }
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround,
            modifier = Modifier.fillMaxWidth()
        ) {
            if (completedExercises.size > 0) {
                Button(enabled = currentExercise == -1, onClick = { finishFunction() }) {
                    Text(text = stringResource(id = R.string.finish_workout))
                }
            }
            Button(
                onClick = { showDeleteWorkout = true },
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
            ) {
                Text(text = stringResource(id = R.string.cancel))
            }
        }
    }
    if (showDeleteWorkout) {
        Dialog(onDismissRequest = { showDeleteWorkout = false }) {
            ActionConfirmation(
                actionTitle = stringResource(id = R.string.live_record_cancel),
                confirmFunction = { cancelFunction() },
                cancelFunction = { showDeleteWorkout = false }
            )
        }
    }
}

@Composable
fun LiveRecordWorkoutExerciseCard(
    exercise: ExerciseUiState,
    completed: Boolean,
    modifier: Modifier = Modifier,
    recording: Boolean = false,
    startFunction: () -> Unit = { }
) {
    Card(
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = exercise.name,
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.fillMaxWidth(0.55f),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            if (completed) {
                Text(text = stringResource(id = R.string.completed))
            } else {
                Button(
                    onClick = startFunction,
                    enabled = !recording
                ) {
                    Text(text = stringResource(id = R.string.start))
                }
            }
        }
    }
}

val IntListSaver = listSaver(
    save = { it.toList() },
    restore = { it.map { elem -> elem.toString().toInt() }.toMutableList() }
)

@Preview(showBackground = true)
@Composable
fun LiveRecordWorkoutExerciseCardCompletedPreview() {
    GymTrackerTheme(darkTheme = false) {
        LiveRecordWorkoutExerciseCard(
            exercise = ExerciseUiState(0, "Curls", "Biceps", "Dumbbells"),
            completed = true,
            recording = false,
            startFunction = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun LiveRecordWorkoutExerciseCardPreview() {
    GymTrackerTheme(darkTheme = false) {
        LiveRecordWorkoutExerciseCard(
            exercise = ExerciseUiState(0, "Curls", "Biceps", "Dumbbells"),
            completed = false,
            recording = false,
            startFunction = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun LiveRecordWorkoutPreview() {
    GymTrackerTheme(darkTheme = false) {
        LiveRecordWorkout(
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
            saveFunction = { },
            finishFunction = { },
            cancelFunction = { }
        )
    }
}
