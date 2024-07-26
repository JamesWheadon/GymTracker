package com.askein.gymtracker.ui.exercise.details

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
import androidx.compose.runtime.CompositionLocalProvider
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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.askein.gymtracker.R
import com.askein.gymtracker.data.exercise.ExerciseType
import com.askein.gymtracker.enums.DistanceUnits
import com.askein.gymtracker.enums.WeightUnits
import com.askein.gymtracker.enums.convertToDistanceUnit
import com.askein.gymtracker.enums.convertToWeightUnit
import com.askein.gymtracker.ui.ActionConfirmation
import com.askein.gymtracker.ui.AppViewModelProvider
import com.askein.gymtracker.ui.customCardElevation
import com.askein.gymtracker.ui.exercise.ExerciseUiState
import com.askein.gymtracker.ui.exercise.history.RecordExerciseHistoryViewModel
import com.askein.gymtracker.ui.exercise.history.UpdateExerciseHistoryScreen
import com.askein.gymtracker.ui.exercise.history.state.CardioExerciseHistoryUiState
import com.askein.gymtracker.ui.exercise.history.state.ExerciseHistoryUiState
import com.askein.gymtracker.ui.exercise.history.state.WeightsExerciseHistoryUiState
import com.askein.gymtracker.ui.theme.GymTrackerTheme
import com.askein.gymtracker.ui.user.LocalUserPreferences
import com.askein.gymtracker.ui.user.UserPreferencesUiState
import com.askein.gymtracker.ui.visualisations.Calendar
import com.askein.gymtracker.ui.visualisations.MonthPicker
import com.askein.gymtracker.util.getTimeStringResourceFromSeconds
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

@Composable
fun ExerciseHistoryCalendar(
    uiState: ExerciseDetailsUiState,
    chosenDate: LocalDate?,
    modifier: Modifier = Modifier
) {
    val currentMonth =
        if (chosenDate == null) YearMonth.now() else YearMonth.of(chosenDate.year, chosenDate.month)
    var selectedMonth by remember { mutableStateOf(currentMonth) }
    var showDay: Int? by remember { mutableStateOf(chosenDate?.dayOfMonth) }
    val activeDays = listOf(uiState.weightsHistory, uiState.cardioHistory).flatten()
        .filter { history -> history.date.month == selectedMonth.month && history.date.year == selectedMonth.year }
        .map { history -> history.date.dayOfMonth }
    val exerciseHistory =
        if (uiState.exercise.type == ExerciseType.CARDIO) uiState.cardioHistory else uiState.weightsHistory
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
                text = stringResource(
                    id = R.string.exercises_on,
                    date.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG))
                ),
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
                contentDescription = stringResource(id = R.string.close)
            )
        }
    }
}

@Composable
fun ExerciseHistoryDetails(
    exerciseHistory: ExerciseHistoryUiState,
    exercise: ExerciseUiState,
    deleteFunction: (ExerciseHistoryUiState) -> Unit,
    modifier: Modifier = Modifier
) {
    var editExercise by remember { mutableStateOf(false) }
    var deleteExercise by remember { mutableStateOf(false) }
    Card(
        elevation = customCardElevation(),
        onClick = { editExercise = true },
        modifier = modifier
    ) {
        when (exercise.type) {
            ExerciseType.WEIGHTS -> {
                WeightsExerciseHistoryDetails(
                    exerciseHistory = exerciseHistory as WeightsExerciseHistoryUiState,
                    deleteFunction = { deleteExercise = true },
                    editEnabled = true
                )
            }

            ExerciseType.CARDIO -> {
                CardioExerciseHistoryDetails(
                    exerciseHistory = exerciseHistory as CardioExerciseHistoryUiState,
                    deleteFunction = { deleteExercise = true },
                    editEnabled = true
                )
            }

            ExerciseType.CALISTHENICS -> {
                CalisthenicsExerciseHistoryDetails(
                    exerciseHistory = exerciseHistory as WeightsExerciseHistoryUiState,
                    deleteFunction = { deleteExercise = true },
                    editEnabled = true
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
                actionTitle = stringResource(id = R.string.delete_exercise_confirm),
                confirmFunction = {
                    deleteFunction(exerciseHistory)
                    deleteExercise = false
                },
                cancelFunction = { deleteExercise = false }
            )
        }
    }
}

@Composable
fun ExerciseHistoryDetails(
    exerciseHistory: ExerciseHistoryUiState,
    exerciseType: ExerciseType,
    modifier: Modifier = Modifier
) {
    when (exerciseType) {
        ExerciseType.WEIGHTS -> {
            WeightsExerciseHistoryDetails(
                exerciseHistory = exerciseHistory as WeightsExerciseHistoryUiState,
                deleteFunction = { },
                editEnabled = false,
                modifier = modifier
            )
        }

        ExerciseType.CARDIO -> {
            CardioExerciseHistoryDetails(
                exerciseHistory = exerciseHistory as CardioExerciseHistoryUiState,
                deleteFunction = { },
                editEnabled = false,
                modifier = modifier
            )
        }

        ExerciseType.CALISTHENICS -> {
            CalisthenicsExerciseHistoryDetails(
                exerciseHistory = exerciseHistory as WeightsExerciseHistoryUiState,
                deleteFunction = { },
                editEnabled = false,
                modifier = modifier
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
    val weights = if (userPreferencesUiState.defaultWeightUnit == WeightUnits.KILOGRAMS) {
        exerciseHistory.weight
    } else {
        exerciseHistory.weight.map {
            convertToWeightUnit(
                userPreferencesUiState.defaultWeightUnit,
                it
            )
        }
    }
    Row(
        modifier = modifier
            .padding(8.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1F)
        ) {
            for (i in 0 until exerciseHistory.sets) {
                if (exerciseHistory.reps != null) {
                    val stringResource = if (exerciseHistory.reps!![i] == 1) {
                        R.string.display_set_rep_weight_info_singular
                    } else {
                        R.string.display_set_rep_weight_info
                    }
                    Text(
                        text = stringResource(
                            id = stringResource,
                            i + 1,
                            exerciseHistory.reps!![i],
                            weights[i],
                            stringResource(id = userPreferencesUiState.defaultWeightUnit.shortForm)
                        )
                    )
                } else {
                    val (resourceId, resourceArgs) = getTimeStringResourceFromSeconds(
                        exerciseHistory.seconds!![i]
                    )
                    Text(
                        text = stringResource(
                            id = R.string.display_set_time_weight_info,
                            i + 1,
                            stringResource(id = resourceId, *resourceArgs.toTypedArray()),
                            weights[i],
                            stringResource(id = userPreferencesUiState.defaultWeightUnit.shortForm)
                        )
                    )
                }
            }
            if (exerciseHistory.rest != null) {
                Text(text = stringResource(id = R.string.display_rest, exerciseHistory.rest!!))
            }
        }
        if (editEnabled) {
            IconButton(onClick = deleteFunction) {
                Icon(
                    imageVector = Icons.Outlined.Delete,
                    tint = Color.Red,
                    contentDescription = stringResource(id = R.string.delete_history)
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
    val (resourceId, resourceArgs) = getTimeStringResourceFromSeconds(seconds)
    Row(
        modifier = modifier
            .padding(8.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1F)
        ) {
            if (seconds != 0) {
                Text(
                    text = stringResource(
                        id = R.string.exercise_time, stringResource(
                            id = resourceId,
                            *resourceArgs.toTypedArray<String>()
                        )
                    )
                )
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
                Text(
                    text = stringResource(
                        id = R.string.exercise_distance,
                        distance!!,
                        stringResource(id = userPreferencesUiState.defaultDistanceUnit.shortForm)
                    )
                )
            }
            if (exerciseHistory.calories != null) {
                Text(
                    text = stringResource(
                        id = R.string.exercise_calories,
                        exerciseHistory.calories!!
                    )
                )
            }
        }
        if (editEnabled) {
            IconButton(onClick = deleteFunction) {
                Icon(
                    imageVector = Icons.Outlined.Delete,
                    tint = Color.Red,
                    contentDescription = stringResource(id = R.string.delete_history)
                )
            }
        }
    }
}

@Composable
fun CalisthenicsExerciseHistoryDetails(
    exerciseHistory: WeightsExerciseHistoryUiState,
    deleteFunction: () -> Unit,
    modifier: Modifier = Modifier,
    editEnabled: Boolean
) {
    Row(
        modifier = modifier
            .padding(8.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1F)
        ) {
            for (i in 0 until exerciseHistory.sets) {
                if (exerciseHistory.reps != null) {
                    Text(
                        text = stringResource(
                            id = R.string.display_set_reps_info,
                            i + 1,
                            exerciseHistory.reps!![i]
                        )
                    )
                } else {
                    val (resourceId, resourceArgs) = getTimeStringResourceFromSeconds(
                        exerciseHistory.seconds!![i]
                    )
                    Text(
                        text = stringResource(
                            id = R.string.display_set_time_info,
                            i + 1,
                            stringResource(id = resourceId, *resourceArgs.toTypedArray())
                        )
                    )
                }
            }
            if (exerciseHistory.rest != null) {
                Text(text = stringResource(id = R.string.display_rest, exerciseHistory.rest!!))
            }
        }
        if (editEnabled) {
            IconButton(onClick = deleteFunction) {
                Icon(
                    imageVector = Icons.Outlined.Delete,
                    tint = Color.Red,
                    contentDescription = stringResource(id = R.string.delete_history)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun WeightsHistoryDetailsPreview() {
    val userPreferencesUiState = UserPreferencesUiState()
    CompositionLocalProvider(LocalUserPreferences provides userPreferencesUiState) {
        GymTrackerTheme(darkTheme = false) {
            WeightsExerciseHistoryDetails(
                exerciseHistory = WeightsExerciseHistoryUiState(
                    id = 1,
                    weight = listOf(13.0),
                    sets = 1,
                    reps = listOf(2),
                    rest = 1,
                    date = LocalDate.now().minusDays(5)

                ),
                deleteFunction = { },
                editEnabled = true
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CardioHistoryDetailsPreview() {
    val userPreferencesUiState = UserPreferencesUiState()
    CompositionLocalProvider(LocalUserPreferences provides userPreferencesUiState) {
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
}
