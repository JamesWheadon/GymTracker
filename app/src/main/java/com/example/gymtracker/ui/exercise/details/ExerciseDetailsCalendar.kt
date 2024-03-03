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
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.Card
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.gymtracker.converters.DistanceUnits
import com.example.gymtracker.converters.WeightUnits
import com.example.gymtracker.converters.convertToDistanceUnit
import com.example.gymtracker.converters.convertToWeightUnit
import com.example.gymtracker.ui.ActionConfirmation
import com.example.gymtracker.ui.AppViewModelProvider
import com.example.gymtracker.ui.customCardElevation
import com.example.gymtracker.ui.exercise.ExerciseUiState
import com.example.gymtracker.ui.exercise.history.RecordExerciseHistoryViewModel
import com.example.gymtracker.ui.exercise.history.UpdateExerciseHistoryScreen
import com.example.gymtracker.ui.exercise.history.state.CardioExerciseHistoryUiState
import com.example.gymtracker.ui.exercise.history.state.ExerciseHistoryUiState
import com.example.gymtracker.ui.exercise.history.state.WeightsExerciseHistoryUiState
import com.example.gymtracker.ui.noCardElevation
import com.example.gymtracker.ui.theme.GymTrackerTheme
import com.example.gymtracker.ui.user.LocalUserPreferences
import com.example.gymtracker.ui.visualisations.Calendar
import com.example.gymtracker.ui.visualisations.MonthPicker
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
    val activeDays = listOf(uiState.weightsHistory, uiState.cardioHistory).flatten()
        .filter { history -> history.date.month == selectedMonth.month && history.date.year == selectedMonth.year }
        .map { history -> history.date.dayOfMonth }
    val exerciseHistory =
        if (uiState.exercise.equipment == "") uiState.cardioHistory else uiState.weightsHistory
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = modifier
    ) {
        if (showDay != null) {
            val selectedDate = LocalDate.of(
                selectedMonth.year, selectedMonth.monthValue, showDay!!
            )
            ExercisesOnDay(
                exercises = exerciseHistory.filter { history ->
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
    modifier: Modifier = Modifier,
    viewModel: RecordExerciseHistoryViewModel = viewModel(factory = AppViewModelProvider.Factory)
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
                ExerciseHistoryDetails(
                    exerciseHistory = history,
                    exercise = exercise,
                    deleteFunction = { deleteHistory -> viewModel.deleteHistory(deleteHistory) },
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

@Composable
fun ExerciseHistoryDetails(
    exerciseHistory: ExerciseHistoryUiState,
    exercise: ExerciseUiState,
    deleteFunction: (ExerciseHistoryUiState) -> Unit,
    modifier: Modifier = Modifier,
    editEnabled: Boolean = true,
    elevation: Boolean = true
) {
    var editExercise by remember { mutableStateOf(false) }
    var deleteExercise by remember { mutableStateOf(false) }
    Card(
        elevation = if (elevation) customCardElevation() else noCardElevation(),
        onClick = { editExercise = true && editEnabled },
        modifier = modifier
    ) {
        when (exerciseHistory) {
            is WeightsExerciseHistoryUiState -> {
                WeightsExerciseHistoryDetails(
                    exerciseHistory = exerciseHistory,
                    deleteFunction = { deleteExercise = true },
                    editEnabled = editEnabled
                )
            }

            is CardioExerciseHistoryUiState -> {
                CardioExerciseHistoryDetails(
                    exerciseHistory = exerciseHistory,
                    deleteFunction = { deleteExercise = true },
                    editEnabled = editEnabled
                )
            }
        }
    }
    if (editExercise) {
        Dialog(
            onDismissRequest = { editExercise = false }
        ) {
            UpdateExerciseHistoryScreen(
                exercise = exercise,
                history = exerciseHistory,
                onDismiss = { editExercise = false }
            )
        }
    }
    if (deleteExercise) {
        Dialog(
            onDismissRequest = { deleteExercise = false }
        ) {
            ActionConfirmation(
                actionTitle = "Do you want to delete this exercise?",
                confirmFunction = {
                    deleteFunction(
                        exerciseHistory
                    )
                },
                cancelFunction = { deleteExercise = false }
            )
        }
    }
}

@Composable
fun WeightsExerciseHistoryDetails(
    exerciseHistory: WeightsExerciseHistoryUiState,
    deleteFunction: () -> Unit,
    modifier: Modifier = Modifier,
    editEnabled: Boolean
) {
    val userPreferencesUiState = LocalUserPreferences.current
    val weight = if (userPreferencesUiState.defaultWeightUnit == WeightUnits.KILOGRAMS) {
        exerciseHistory.weight
    } else {
        convertToWeightUnit(userPreferencesUiState.defaultWeightUnit, exerciseHistory.weight)
    }
    Row(
        modifier = modifier.padding(8.dp),
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
            Text(text = "Weight: $weight${userPreferencesUiState.defaultWeightUnit.shortForm}")
            if (exerciseHistory.rest != null) {
                Text(text = "Rest time: ${exerciseHistory.rest}")
            }
        }
        if (editEnabled) {
            IconButton(onClick = deleteFunction) {
                Icon(
                    imageVector = Icons.Outlined.Delete,
                    tint = Color.Red,
                    contentDescription = "Delete history"
                )
            }
        }
    }
}

@Composable
fun CardioExerciseHistoryDetails(
    exerciseHistory: CardioExerciseHistoryUiState,
    deleteFunction: () -> Unit,
    modifier: Modifier = Modifier,
    editEnabled: Boolean = true
) {
    val seconds = (exerciseHistory.minutes ?: 0) * 60 + (exerciseHistory.seconds ?: 0)
    val time = if (seconds >= 3600) {
        "${seconds / 3600}:${
            String.format(
                "%02d",
                (seconds % 3600) / 60
            )
        }:${String.format("%02d", seconds % 60)}"
    } else if (seconds >= 60) {
        "${String.format("%02d", seconds / 60)}:${String.format("%02d", seconds % 60)}"
    } else {
        "${String.format("%02d", seconds % 60)} s"
    }
    Row(
        modifier = modifier.padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (seconds != 0 && exerciseHistory.distance != null && exerciseHistory.calories != null) {
            Column(
                modifier = Modifier.weight(1F)
            ) {
                Text(text = "Time: $time")
            }
            val userPreferencesUiState = LocalUserPreferences.current
            val distance =
                if (userPreferencesUiState.defaultDistanceUnit == DistanceUnits.KILOMETERS) {
                    exerciseHistory.distance
                } else {
                    convertToDistanceUnit(
                        userPreferencesUiState.defaultDistanceUnit,
                        exerciseHistory.distance!!
                    )
                }
            Column(
                modifier = Modifier.weight(1F)
            ) {
                Text(text = "Distance: $distance${userPreferencesUiState.defaultDistanceUnit.shortForm}")
                Text(text = "Calories: ${exerciseHistory.calories}kcal")
            }
        } else {
            Column(
                modifier = Modifier.weight(1F)
            ) {
                if (seconds != 0) {
                    Text(text = "Time: $time")
                }
                if (exerciseHistory.distance != null) {
                    val userPreferencesUiState = LocalUserPreferences.current
                    val distance =
                        if (userPreferencesUiState.defaultDistanceUnit == DistanceUnits.KILOMETERS) {
                            exerciseHistory.distance
                        } else {
                            convertToDistanceUnit(
                                userPreferencesUiState.defaultDistanceUnit,
                                exerciseHistory.distance!!
                            )
                        }
                    Text(text = "Distance: $distance${userPreferencesUiState.defaultDistanceUnit.shortForm}")
                }
                if (exerciseHistory.calories != null) {
                    Text(text = "Calories: ${exerciseHistory.calories}kcal")
                }
            }
        }
        if (editEnabled) {
            IconButton(onClick = deleteFunction) {
                Icon(
                    imageVector = Icons.Outlined.Delete,
                    tint = Color.Red,
                    contentDescription = "Delete history"
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun WeightsHistoryDetailsPreview() {
    GymTrackerTheme(darkTheme = false) {
        WeightsExerciseHistoryDetails(
            exerciseHistory = WeightsExerciseHistoryUiState(
                id = 1,
                weight = 13.0,
                sets = 1,
                reps = 2,
                rest = 1,
                date = LocalDate.now().minusDays(5)

            ),
            deleteFunction = { },
            editEnabled = true
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CardioHistoryDetailsPreview() {
    GymTrackerTheme(darkTheme = false) {
        CardioExerciseHistoryDetails(
            exerciseHistory = CardioExerciseHistoryUiState(
                id = 1,
                distance = 30.0,
                minutes = 90,
                seconds = 0,
                calories = 800,
                date = LocalDate.now().minusDays(5)

            ),
            deleteFunction = { },
            editEnabled = true
        )
    }
}
