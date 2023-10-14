package com.example.gymtracker.ui.exercise.details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.gymtracker.ui.Calendar
import com.example.gymtracker.ui.MonthPicker
import com.example.gymtracker.ui.exercise.ExerciseDetailsUiState
import com.example.gymtracker.ui.exercise.ExerciseUiState
import com.example.gymtracker.ui.exercise.toExerciseUiState
import com.example.gymtracker.ui.history.ExerciseHistoryUiState
import com.example.gymtracker.ui.history.UpdateHistoryScreen
import com.example.gymtracker.ui.theme.GymTrackerTheme
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle


@Composable
fun ExerciseHistoryCalendar(
    uiState: ExerciseDetailsUiState,
    modifier: Modifier = Modifier
) {
    var selectedMonth by remember { mutableStateOf(YearMonth.now()) }
    var showDay: Int? by remember { mutableStateOf(null) }
    val activeDays = uiState.history!!
        .filter { history -> history.date.month == selectedMonth.month && history.date.year == selectedMonth.year }
        .map { history -> history.date.dayOfMonth }
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = modifier
    ) {
        if (showDay != null) {
            val selectedDate = LocalDate.of(
                selectedMonth.year, selectedMonth.monthValue, showDay!!
            )
            ExercisesOnDay(
                exercises = uiState.history!!.filter { history ->
                    history.date == selectedDate
                },
                date = selectedDate,
                exercise = uiState.toExerciseUiState(),
                onDismiss = { showDay = null }
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
            dayFunction = { chosenDay -> showDay = chosenDay }
        )
    }
}

@Composable
fun ExercisesOnDay(
    exercises: List<ExerciseHistoryUiState>,
    date: LocalDate,
    exercise: ExerciseUiState,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Exercises on ${date.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG))}",
                style = MaterialTheme.typography.headlineMedium
            )
            for (history in exercises) {
                HistoryDetails(
                    exerciseHistory = history,
                    exercise = exercise
                )
            }
        }
        IconButton(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .offset((-8).dp, (-12).dp),
            onClick = { onDismiss() }
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Close"
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryDetails(
    exerciseHistory: ExerciseHistoryUiState,
    exercise: ExerciseUiState,
    modifier: Modifier = Modifier
) {
    var editExercise by remember { mutableStateOf(false) }
    val customCardElevation = CardDefaults.cardElevation(
        defaultElevation = 8.dp,
        pressedElevation = 2.dp,
        focusedElevation = 4.dp
    )
    Card(
        elevation = customCardElevation,
        onClick = { editExercise = true },
        modifier = modifier
    ) {
        Row(
            modifier = Modifier.padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1F)
            ) {
                Text(text = "Sets: ${exerciseHistory.sets}")
                Text(text = "Reps: ${exerciseHistory.reps}")
            }
            Column(
                modifier = Modifier.weight(1F)
            ) {
                Text(text = "Weight: ${exerciseHistory.weight}kg")
                Text(text = "Rest time: ${exerciseHistory.rest}")
            }
        }
    }
    if (editExercise) {
        Dialog(
            onDismissRequest = { editExercise = false }
        ) {
            UpdateHistoryScreen(
                exercise = exercise,
                history = exerciseHistory,
                onDismiss = { editExercise = false }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ExercisesOnDayPreview() {
    GymTrackerTheme(darkTheme = false) {
        ExercisesOnDay(
            exercises = listOf(
                ExerciseHistoryUiState(
                    id = 1,
                    weight = 13.0,
                    sets = 1,
                    reps = 2,
                    rest = 1,
                    date = LocalDate.now().minusDays(5)
                )
            ),
            date = LocalDate.now(),
            exercise = ExerciseUiState(
                name = "Curls",
                muscleGroup = "Biceps",
                equipment = "Dumbbells"
            ),
            onDismiss = { }
        )
    }
}
