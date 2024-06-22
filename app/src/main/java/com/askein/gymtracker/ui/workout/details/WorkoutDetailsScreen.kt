package com.askein.gymtracker.ui.workout.details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import com.askein.gymtracker.ui.exercise.history.state.WeightsExerciseHistoryUiState
import com.askein.gymtracker.ui.navigation.NavigationRoute
import com.askein.gymtracker.ui.navigation.NavigationRoutes.WORKOUT_DETAILS_SCREEN
import com.askein.gymtracker.ui.navigation.TopBar
import com.askein.gymtracker.ui.theme.GymTrackerTheme
import com.askein.gymtracker.ui.user.LocalUserPreferences
import com.askein.gymtracker.ui.user.UserPreferencesUiState
import com.askein.gymtracker.ui.visualisations.Calendar
import com.askein.gymtracker.ui.visualisations.MonthPicker
import com.askein.gymtracker.ui.workout.WorkoutUiState
import com.askein.gymtracker.ui.workout.create.CreateWorkoutForm
import com.askein.gymtracker.ui.workout.history.WorkoutHistoryScreen
import com.askein.gymtracker.ui.workout.history.WorkoutHistoryWithExercisesUiState
import com.askein.gymtracker.ui.workout.history.create.RecordWorkoutHistoryScreen
import com.askein.gymtracker.util.convertLocalDateToString
import com.askein.gymtracker.util.convertStringToLocalDate
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

object WorkoutDetailsRoute : NavigationRoute {
    override val route =
        "${WORKOUT_DETAILS_SCREEN.baseRoute}/{${WORKOUT_DETAILS_SCREEN.idArgument}}/{${WORKOUT_DETAILS_SCREEN.dateArgument}}"

    fun getRouteForNavArguments(idArgument: Int, dateArgument: LocalDate?): String =
        "${WORKOUT_DETAILS_SCREEN.baseRoute}/${idArgument}/${convertLocalDateToString(dateArgument)}"
}

@Composable
fun WorkoutDetailsScreen(
    navController: NavHostController,
    exerciseNavigationFunction: (Int, LocalDate?) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: WorkoutDetailsViewModel = viewModel(
        factory = AppViewModelProvider.Factory
    )
) {
    val uiState = viewModel.uiState.collectAsState().value
    val chosenDate = convertStringToLocalDate(viewModel.chosenDate!!)
    WorkoutDetailsScreen(
        uiState = uiState,
        navController = navController,
        exerciseNavigationFunction = exerciseNavigationFunction,
        updateWorkoutFunction = { workout -> viewModel.updateWorkout(workout) },
        deleteWorkoutFunction = { workout -> viewModel.deleteWorkout(workout) },
        chosenDate = chosenDate,
        modifier = modifier,
    )
}

@Composable
fun WorkoutDetailsScreen(
    uiState: WorkoutWithExercisesUiState,
    navController: NavHostController,
    exerciseNavigationFunction: (Int, LocalDate?) -> Unit,
    updateWorkoutFunction: (WorkoutUiState) -> Unit,
    deleteWorkoutFunction: (WorkoutUiState) -> Unit,
    chosenDate: LocalDate?,
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
            chosenDate = chosenDate,
            modifier = modifier
        )
    }
    if (showEditExercises) {
        EditWorkoutExercisesScreen(
            uiState = uiState,
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
    exerciseNavigationFunction: (Int, LocalDate?) -> Unit,
    editExercises: () -> Unit,
    innerPadding: PaddingValues,
    chosenDate: LocalDate?,
    modifier: Modifier = Modifier
) {
    val currentMonth = if (chosenDate == null) YearMonth.now() else YearMonth.of(chosenDate.year, chosenDate.month)
    var selectedMonth by remember { mutableStateOf(currentMonth) }
    var showDate: LocalDate? by remember { mutableStateOf(chosenDate) }
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
        if (showDate != null) {
            Box {
                Column(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = stringResource(
                            id = R.string.workouts_on,
                            showDate!!.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG))
                        ),
                        style = MaterialTheme.typography.headlineMedium
                    )
                    uiState.workoutHistory.filter { it.date == showDate }.forEach { workoutHistory ->
                        WorkoutHistoryScreen(
                            workoutHistoryUiState = workoutHistory,
                            workoutUiState = uiState,
                            chosenDate = showDate!!,
                            exerciseNavigationFunction = exerciseNavigationFunction
                        )
                    }
                }
                IconButton(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .offset((-8).dp, (-12).dp),
                    onClick = { showDate = null }
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = stringResource(id = R.string.close)
                    )
                }
            }
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
            dayFunction = { day ->
                showDate = LocalDate.of(selectedMonth.year, selectedMonth.monthValue, day)
            }
        )
        Spacer(modifier = Modifier.height(72.dp))
    }
}

@Preview(showBackground = true)
@Composable
fun WorkoutDetailsScreenPreview() {
    val userPreferencesUiState = UserPreferencesUiState()
    CompositionLocalProvider(LocalUserPreferences provides userPreferencesUiState) {
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
                        WorkoutHistoryWithExercisesUiState(
                            1,
                            1,
                            LocalDate.now(),
                            exercises = listOf(WeightsExerciseHistoryUiState(exerciseId = 0))
                        ),
                        WorkoutHistoryWithExercisesUiState(
                            2,
                            1,
                            LocalDate.now(),
                            exercises = listOf(WeightsExerciseHistoryUiState(exerciseId = 1))
                        ),
                        WorkoutHistoryWithExercisesUiState(2, 1, LocalDate.now().minusDays(3))
                    )
                ),
                exerciseNavigationFunction = { _, _ -> (Unit) },
                editExercises = { },
                chosenDate = null,
                innerPadding = PaddingValues()
            )
        }
    }
}
