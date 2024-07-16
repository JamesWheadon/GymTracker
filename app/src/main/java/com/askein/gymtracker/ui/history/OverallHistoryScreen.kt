package com.askein.gymtracker.ui.history

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
import com.askein.gymtracker.R
import com.askein.gymtracker.ui.AppViewModelProvider
import com.askein.gymtracker.ui.customCardElevation
import com.askein.gymtracker.ui.exercise.ExerciseCard
import com.askein.gymtracker.ui.exercise.ExerciseUiState
import com.askein.gymtracker.ui.theme.GymTrackerTheme
import com.askein.gymtracker.ui.visualisations.Calendar
import com.askein.gymtracker.ui.visualisations.MonthPicker
import com.askein.gymtracker.ui.workout.WorkoutCard
import com.askein.gymtracker.ui.workout.WorkoutUiState
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

@Composable
fun OverallHistoryScreen(
    exerciseNavigationFunction: (Int, LocalDate?) -> Unit,
    workoutNavigationFunction: (Int, LocalDate?) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: OverallHistoryViewModel = viewModel(
        factory = AppViewModelProvider.Factory
    )
) {
    val datesUiState = viewModel.datesUiState.collectAsState().value
    val historyUiState = viewModel.historyUiState.collectAsState().value
    OverallHistoryScreen(
        exerciseNavigationFunction = exerciseNavigationFunction,
        workoutNavigationFunction = workoutNavigationFunction,
        datesUiState = datesUiState,
        historyUiState = historyUiState,
        dateSelector = { date -> viewModel.selectDate(date) },
        modifier = modifier
    )
}

@Composable
fun OverallHistoryScreen(
    exerciseNavigationFunction: (Int, LocalDate?) -> Unit,
    workoutNavigationFunction: (Int, LocalDate?) -> Unit,
    datesUiState: List<LocalDate>,
    historyUiState: HistoryUiState,
    dateSelector: (LocalDate) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedMonth by remember { mutableStateOf(YearMonth.now()) }
    var showHistory by remember { mutableStateOf(false) }
    val activeDays =
        datesUiState.filter { date -> date.month == selectedMonth.month && date.year == selectedMonth.year }
            .map { date -> date.dayOfMonth }
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = modifier.padding(vertical = 16.dp, horizontal = 16.dp)
    ) {
        if (showHistory) {
            HistoryOnDay(
                date = historyUiState.date!!,
                exerciseNavigationFunction = exerciseNavigationFunction,
                workoutNavigationFunction = workoutNavigationFunction,
                workoutsOnDateUiState = historyUiState.workouts,
                exercisesOnDateUiState = historyUiState.exercises,
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
                showHistory = true
            }
        )
    }
}

@Composable
fun HistoryOnDay(
    date: LocalDate,
    exerciseNavigationFunction: (Int, LocalDate?) -> Unit,
    workoutNavigationFunction: (Int, LocalDate?) -> Unit,
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
                                navigationFunction = workoutNavigationFunction,
                                chosenDate = date
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
                                navigationFunction = exerciseNavigationFunction,
                                chosenDate = date
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
            workoutNavigationFunction = { _, _ -> (Unit) },
            exerciseNavigationFunction = { _, _ -> (Unit) },
            datesUiState = listOf(LocalDate.now(), LocalDate.now().minusDays(2)),
            historyUiState = HistoryUiState(
                workouts = listOf(WorkoutUiState(name = "Arms"), WorkoutUiState(name = "Legs")),
                exercises = listOf(
                    ExerciseUiState(name = "Treadmill"),
                    ExerciseUiState(name = "Bench", muscleGroup = "Chest", equipment = "Bench")
                )
            ),
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
            workoutNavigationFunction = { _, _ -> (Unit) },
            exerciseNavigationFunction = { _, _ -> (Unit) },
            workoutsOnDateUiState = listOf(
                WorkoutUiState(name = "Arms"),
                WorkoutUiState(name = "Legs")
            ),
            exercisesOnDateUiState = listOf(
                ExerciseUiState(name = "Treadmill"),
                ExerciseUiState(name = "Bench", muscleGroup = "Chest", equipment = "Bench")
            ),
            onDismiss = { }
        )
    }
}
