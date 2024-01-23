package com.example.gymtracker.ui.workout.history.create.live

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.gymtracker.data.exerciseHistory.ExerciseHistory
import com.example.gymtracker.ui.AppViewModelProvider
import com.example.gymtracker.ui.exercise.ExerciseUiState
import com.example.gymtracker.ui.exercise.history.toExerciseHistory
import com.example.gymtracker.ui.navigation.NavigationRoute
import com.example.gymtracker.ui.navigation.NavigationRoutes.LIVE_RECORD_WORKOUT_SCREEN
import com.example.gymtracker.ui.navigation.TopBar
import com.example.gymtracker.ui.theme.GymTrackerTheme
import com.example.gymtracker.ui.workout.details.WorkoutDetailsRoute
import com.example.gymtracker.ui.workout.details.WorkoutDetailsViewModel
import com.example.gymtracker.ui.workout.details.WorkoutWithExercisesUiState
import com.example.gymtracker.ui.workout.history.WorkoutHistoryUiState
import com.example.gymtracker.ui.workout.history.WorkoutHistoryViewModel
import com.example.gymtracker.ui.workout.history.WorkoutHistoryWithExercisesUiState
import com.example.gymtracker.ui.workout.history.toWorkoutHistory
import java.time.LocalDate

object LiveRecordWorkoutRoute : NavigationRoute {
    override val route =
        "${LIVE_RECORD_WORKOUT_SCREEN.baseRoute}/{${LIVE_RECORD_WORKOUT_SCREEN.navigationArgument}}"

    fun getRouteForNavArgument(navArgument: Int): String =
        "${LIVE_RECORD_WORKOUT_SCREEN.baseRoute}/${navArgument}"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LiveRecordWorkout(
    navController: NavHostController,
    detailsViewModel: WorkoutDetailsViewModel = viewModel(factory = AppViewModelProvider.Factory),
    historyViewModel: WorkoutHistoryViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val uiState = detailsViewModel.uiState.collectAsState().value
    LaunchedEffect(uiState.workoutId != 0) {
        historyViewModel.saveWorkoutHistory(
            WorkoutHistoryUiState(workoutId = uiState.workoutId).toWorkoutHistory()
        )
    }
    Scaffold(
        topBar = {
            TopBar(
                text = "Record ${uiState.name} Workout",
                navController = navController
            )
        }
    ) { innerPadding ->
        LiveRecordWorkout(
            uiState = uiState,
            saveFunction = { exercise ->
                historyViewModel.saveWorkoutExercise(exercise)
            },
            finishFunction = {
                navController.popBackStack()
                navController.navigate(WorkoutDetailsRoute.getRouteForNavArgument(uiState.workoutId))
            },
            modifier = Modifier.padding(innerPadding)
        )
    }
}

@Composable
fun LiveRecordWorkout(
    uiState: WorkoutWithExercisesUiState,
    saveFunction: (ExerciseHistory) -> Unit,
    finishFunction: () -> Unit,
    modifier: Modifier = Modifier
) {
    val completedExercises = rememberSaveable(saver = IntListSaver) { mutableStateListOf() }
    var currentExercise by rememberSaveable { mutableStateOf(-1) }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
    ) {
        uiState.exercises.forEach { exercise ->
            if (exercise.id == currentExercise) {
                LiveRecordExercise(
                    uiState = exercise,
                    exerciseComplete = { exerciseHistory ->
                        saveFunction(exerciseHistory.toExerciseHistory())
                        completedExercises.add(exerciseHistory.exerciseId)
                        currentExercise = -1
                    },
                    exerciseCancel = {
                        currentExercise = -1
                    }
                )
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
        Button(onClick = { finishFunction() }) {
            Text(text = "Finish Workout")
        }
    }
}

@Composable
fun LiveRecordWorkoutExerciseCard(
    exercise: ExerciseUiState,
    completed: Boolean,
    recording: Boolean = false,
    startFunction: () -> Unit = { }
) {
    Card {
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
                Text(text = "Completed")
            } else {
                Button(
                    onClick = startFunction,
                    enabled = !recording
                ) {
                    Text(text = "Start")
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
            finishFunction = { }
        )
    }
}
