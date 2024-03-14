package com.example.gymtracker.ui.history

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.gymtracker.R
import com.example.gymtracker.ui.AppViewModelProvider
import com.example.gymtracker.ui.customCardElevation
import com.example.gymtracker.ui.exercise.ExerciseCard
import com.example.gymtracker.ui.exercise.ExerciseUiState
import com.example.gymtracker.ui.navigation.HomeNavigationInformation
import com.example.gymtracker.ui.navigation.HomeScreenCardWrapper
import com.example.gymtracker.ui.navigation.NavigationRoute
import com.example.gymtracker.ui.navigation.NavigationRoutes
import com.example.gymtracker.ui.theme.GymTrackerTheme
import com.example.gymtracker.ui.visualisations.Calendar
import com.example.gymtracker.ui.visualisations.MonthPicker
import com.example.gymtracker.ui.workout.WorkoutCard
import com.example.gymtracker.ui.workout.WorkoutUiState
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

object OverallHistoryRoute : NavigationRoute {
    override val route = NavigationRoutes.OVERALL_HISTORY_SCREEN.baseRoute
}

@Composable
fun OverallHistoryScreen(
    navController: NavHostController,
    exerciseNavigationFunction: (Int) -> Unit,
    workoutNavigationFunction: (Int) -> Unit,
    homeNavigationOptions: Map<HomeNavigationInformation, Boolean>,
    modifier: Modifier = Modifier,
    viewModel: OverallHistoryViewModel = viewModel(
        factory = AppViewModelProvider.Factory
    )
) {
    val datesUiState = viewModel.datesUiState.collectAsState().value
    val workoutsOnDateUiState = viewModel.workoutsOnDateUiState.collectAsState().value
    val exercisesOnDateUiState = viewModel.exercisesOnDateUiState.collectAsState().value
    OverallHistoryScreen(
        navController = navController,
        exerciseNavigationFunction = exerciseNavigationFunction,
        workoutNavigationFunction = workoutNavigationFunction,
        homeNavigationOptions = homeNavigationOptions,
        datesUiState = datesUiState,
        workoutsOnDateUiState = workoutsOnDateUiState,
        exercisesOnDateUiState = exercisesOnDateUiState,
        dateSelector = { date -> viewModel.selectDate(date) },
        modifier = modifier
    )
}

@Composable
fun OverallHistoryScreen(
    navController: NavHostController,
    exerciseNavigationFunction: (Int) -> Unit,
    workoutNavigationFunction: (Int) -> Unit,
    homeNavigationOptions: Map<HomeNavigationInformation, Boolean>,
    datesUiState: List<LocalDate>,
    workoutsOnDateUiState: List<WorkoutUiState>,
    exercisesOnDateUiState: List<ExerciseUiState>,
    dateSelector: (LocalDate) -> Unit,
    modifier: Modifier = Modifier
) {
    HomeScreenCardWrapper(
        title = stringResource(id = R.string.history),
        navController = navController,
        homeNavigationOptions = homeNavigationOptions
    ) {
        OverallHistoryScreen(
            exerciseNavigationFunction = exerciseNavigationFunction,
            workoutNavigationFunction = workoutNavigationFunction,
            datesUiState = datesUiState,
            workoutsOnDateUiState = workoutsOnDateUiState,
            exercisesOnDateUiState = exercisesOnDateUiState,
            dateSelector = dateSelector,
            modifier = modifier
        )
    }
}

@Composable
fun OverallHistoryScreen(
    exerciseNavigationFunction: (Int) -> Unit,
    workoutNavigationFunction: (Int) -> Unit,
    datesUiState: List<LocalDate>,
    workoutsOnDateUiState: List<WorkoutUiState>,
    exercisesOnDateUiState: List<ExerciseUiState>,
    dateSelector: (LocalDate) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedMonth by remember { mutableStateOf(YearMonth.now()) }
    var showHistory by remember { mutableStateOf(false) }
    var selectedDate: LocalDate? by remember { mutableStateOf(null) }
    val activeDays =
        datesUiState.filter { date -> date.month == selectedMonth.month && date.year == selectedMonth.year }
            .map { date -> date.dayOfMonth }
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = modifier.padding(horizontal = 8.dp)
    ) {
        if (showHistory) {
            HistoryOnDay(
                date = selectedDate!!,
                exerciseNavigationFunction = exerciseNavigationFunction,
                workoutNavigationFunction = workoutNavigationFunction,
                workoutsOnDateUiState = workoutsOnDateUiState,
                exercisesOnDateUiState = exercisesOnDateUiState,
                onDismiss = { showHistory = false }
            )
        }
        MonthPicker(
            yearMonthValue = selectedMonth,
            yearMonthValueOnChange = { chosen -> selectedMonth = chosen }
        )
        Calendar(
            month = selectedMonth.monthValue,
            year = selectedMonth.year,
            activeDays = activeDays,
            dayFunction = { chosenDay ->
                val chosenDate = LocalDate.of(selectedMonth.year, selectedMonth.month, chosenDay)
                dateSelector(chosenDate)
                selectedDate = chosenDate
                showHistory = true
            }
        )
    }
}

@Composable
fun HistoryOnDay(
    date: LocalDate,
    exerciseNavigationFunction: (Int) -> Unit,
    workoutNavigationFunction: (Int) -> Unit,
    workoutsOnDateUiState: List<WorkoutUiState>,
    exercisesOnDateUiState: List<ExerciseUiState>,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box {
        Card(
            elevation = customCardElevation(),
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = date.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG)),
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
                if (workoutsOnDateUiState.isNotEmpty()) {
                    Text(
                        text = stringResource(id = R.string.workouts),
                        style = MaterialTheme.typography.headlineMedium,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                    Card(
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        workoutsOnDateUiState.forEach { workout ->
                            WorkoutCard(
                                workout = workout,
                                navigationFunction = workoutNavigationFunction
                            )
                        }
                    }
                }
                if (exercisesOnDateUiState.isNotEmpty()) {
                    Text(
                        text = stringResource(id = R.string.exercises),
                        style = MaterialTheme.typography.headlineMedium,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                    Card(
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        exercisesOnDateUiState.forEach { exercise ->
                            ExerciseCard(
                                exercise = exercise,
                                navigationFunction = exerciseNavigationFunction
                            )
                        }
                    }
                }
            }
        }
        IconButton(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .offset((-8).dp, (12).dp),
            onClick = { onDismiss() }
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = stringResource(id = R.string.close)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun OverallHistoryScreenPreview() {
    GymTrackerTheme(darkTheme = false) {
        OverallHistoryScreen(
            workoutNavigationFunction = { },
            exerciseNavigationFunction = { },
            datesUiState = listOf(LocalDate.now(), LocalDate.now().minusDays(2)),
            workoutsOnDateUiState = listOf(WorkoutUiState(name = "Arms"), WorkoutUiState(name = "Legs")),
            exercisesOnDateUiState = listOf(ExerciseUiState(name = "Treadmill"), ExerciseUiState(name = "Bench", muscleGroup = "Chest", equipment = "Bench")),
            dateSelector = { }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun HistoryOnDayPreview() {
    GymTrackerTheme(darkTheme = false) {
        HistoryOnDay(
            date = LocalDate.now(),
            workoutNavigationFunction = { },
            exerciseNavigationFunction = { },
            workoutsOnDateUiState = listOf(WorkoutUiState(name = "Arms"), WorkoutUiState(name = "Legs")),
            exercisesOnDateUiState = listOf(ExerciseUiState(name = "Treadmill"), ExerciseUiState(name = "Bench", muscleGroup = "Chest", equipment = "Bench")),
            onDismiss = { }
        )
    }
}
