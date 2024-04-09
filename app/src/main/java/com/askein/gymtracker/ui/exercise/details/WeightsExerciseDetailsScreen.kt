package com.askein.gymtracker.ui.exercise.details

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.askein.gymtracker.R
import com.askein.gymtracker.enums.WeightUnits
import com.askein.gymtracker.enums.convertToWeightUnit
import com.askein.gymtracker.ui.exercise.ExerciseUiState
import com.askein.gymtracker.ui.exercise.history.state.WeightsExerciseHistoryUiState
import com.askein.gymtracker.ui.theme.GymTrackerTheme
import com.askein.gymtracker.ui.user.LocalUserPreferences
import com.askein.gymtracker.ui.user.UserPreferencesUiState
import java.time.LocalDate

@Composable
fun WeightsExerciseDetailsScreen(
    innerPadding: PaddingValues,
    uiState: ExerciseDetailsUiState
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 16.dp, horizontal = 16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        WeightsExerciseInformation(innerPadding, uiState)
        if (uiState.weightsHistory.isNotEmpty()) {
            WeightsExerciseHistoryDetails(uiState = uiState)
            ExerciseHistoryCalendar(uiState = uiState)
        }
        Spacer(modifier = Modifier.height(72.dp))
    }
}

@Composable
private fun WeightsExerciseInformation(
    innerPadding: PaddingValues,
    uiState: ExerciseDetailsUiState
) {
    Row(
        verticalAlignment = Alignment.Top,
        modifier = Modifier
            .fillMaxWidth()
            .padding(innerPadding)
    ) {
        ExerciseDetail(
            exerciseInfo = uiState.exercise.muscleGroup,
            iconId = R.drawable.info_48px,
            iconDescription = R.string.muscle_icon,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        )
        ExerciseDetail(
            exerciseInfo = uiState.exercise.equipment,
            iconId = R.drawable.exercise_filled_48px,
            iconDescription = R.string.equipment_icon,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        )
    }
}

@Composable
fun WeightsExerciseHistoryDetails(
    uiState: ExerciseDetailsUiState
) {
    val timeOptions =
        listOf(R.string.seven_days, R.string.thirty_days, R.string.past_year, R.string.all_time)
    val detailOptions =
        listOf(R.string.max_weight, R.string.max_reps, R.string.max_sets, R.string.total_weight)
    val currentDate = LocalDate.now()
    val timeOptionToStartTime = mapOf<Int, LocalDate>(
        Pair(timeOptions[0], currentDate.minusDays(7)),
        Pair(timeOptions[1], currentDate.minusDays(30)),
        Pair(timeOptions[2], LocalDate.of(currentDate.year, 1, 1)),
        Pair(
            timeOptions[3],
            uiState.weightsHistory.minBy { history -> history.date.toEpochDay() }.date
        ),
    )
    var detail by remember { mutableIntStateOf(detailOptions[0]) }
    var time by remember { mutableIntStateOf(timeOptions[0]) }
    WeightsExerciseDetailsBestAndRecent(uiState)
    GraphOptions(
        detailOptions = detailOptions,
        detailOnChange = { newDetail -> detail = newDetail },
        timeOptions = timeOptions,
        timeOnChange = { newTime -> time = newTime }
    )
    Graph(
        points = getWeightsGraphDetails(
            uiState,
            detail,
            detailOptions,
            LocalUserPreferences.current
        ),
        startDate = timeOptionToStartTime[time] ?: currentDate,
        yLabel = stringResource(id = detail),
        yUnit = if (detail == detailOptions[0] || detail == detailOptions[3]) stringResource(id = LocalUserPreferences.current.defaultWeightUnit.shortForm) else ""
    )
}

@Composable
private fun WeightsExerciseDetailsBestAndRecent(
    uiState: ExerciseDetailsUiState
) {
    val userPreferencesUiState = LocalUserPreferences.current
    val best = if (userPreferencesUiState.displayHighestWeight) {
        uiState.weightsHistory.maxBy { history -> history.weight }
    } else {
        uiState.weightsHistory.maxBy { history -> history.weight * history.reps }
    }
    val recent = uiState.weightsHistory.maxBy { history -> history.date.toEpochDay() }
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier.fillMaxWidth()
    ) {
        ExerciseDetail(
            exerciseInfo = stringResource(
                id = R.string.weights_exercise_reps,
                convertToWeightUnit(userPreferencesUiState.defaultWeightUnit, best.weight),
                stringResource(id = userPreferencesUiState.defaultWeightUnit.shortForm),
                best.reps
            ),
            iconId = R.drawable.trophy_48dp,
            iconDescription = R.string.best_exercise_icon,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        )
        ExerciseDetail(
            exerciseInfo = stringResource(
                id = R.string.weights_exercise_reps,
                convertToWeightUnit(userPreferencesUiState.defaultWeightUnit, recent.weight),
                stringResource(id = userPreferencesUiState.defaultWeightUnit.shortForm),
                recent.reps
            ),
            iconId = R.drawable.history_48px,
            iconDescription = R.string.recent_exercise_icon,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        )
    }
}

@Composable
fun ExerciseDetail(
    exerciseInfo: String,
    iconId: Int,
    @StringRes iconDescription: Int,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
        modifier = modifier
    ) {
        Icon(
            painter = painterResource(id = iconId),
            contentDescription = stringResource(id = iconDescription),
            tint = MaterialTheme.colorScheme.tertiary,
            modifier = Modifier.size(20.dp)
        )
        Text(
            text = exerciseInfo,
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center
        )
    }
}

fun getWeightsGraphDetails(
    uiState: ExerciseDetailsUiState,
    detail: Int,
    detailOptions: List<Int>,
    userPreferencesUiState: UserPreferencesUiState
) = uiState.weightsHistory.map { history ->
    when (detail) {
        detailOptions[0] -> {
            if (userPreferencesUiState.defaultWeightUnit == WeightUnits.KILOGRAMS) {
                Pair(
                    history.date,
                    history.weight
                )
            } else {
                Pair(
                    history.date,
                    convertToWeightUnit(userPreferencesUiState.defaultWeightUnit, history.weight)
                )
            }
        }

        detailOptions[1] -> {
            Pair(
                history.date,
                history.reps.toDouble()
            )
        }

        detailOptions[2] -> {
            Pair(
                history.date,
                history.sets.toDouble()
            )
        }

        detailOptions[3] -> {
            Pair(
                history.date,
                history.weight * history.reps * history.sets
            )
        }

        else -> {
            if (userPreferencesUiState.defaultWeightUnit == WeightUnits.KILOGRAMS) {
                Pair(
                    history.date,
                    history.weight
                )
            } else {
                Pair(
                    history.date,
                    convertToWeightUnit(userPreferencesUiState.defaultWeightUnit, history.weight)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ItemDetailsScreenPreviewNoHistory() {
    val userPreferencesUiState = UserPreferencesUiState()
    CompositionLocalProvider(LocalUserPreferences provides userPreferencesUiState) {
        GymTrackerTheme(darkTheme = false) {
            WeightsExerciseDetailsScreen(
                innerPadding = PaddingValues(),
                uiState = ExerciseDetailsUiState(
                    exercise = ExerciseUiState(
                        name = "Curls",
                        muscleGroup = "Biceps",
                        equipment = "Dumbbells"
                    ),
                    weightsHistory = listOf()
                )
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ItemDetailsScreenPreviewHistory() {
    val userPreferencesUiState = UserPreferencesUiState()
    CompositionLocalProvider(LocalUserPreferences provides userPreferencesUiState) {
        GymTrackerTheme(darkTheme = false) {
            WeightsExerciseDetailsScreen(
                innerPadding = PaddingValues(),
                uiState = ExerciseDetailsUiState(
                    exercise = ExerciseUiState(
                        name = "Curls",
                        muscleGroup = "Biceps",
                        equipment = "Dumbbells And BenchPress"
                    ),
                    weightsHistory = listOf(
                        WeightsExerciseHistoryUiState(
                            id = 1,
                            weight = 13.0,
                            sets = 1,
                            reps = 2,
                            rest = 1,
                            date = LocalDate.now().minusDays(5)
                        )
                    )
                )
            )
        }
    }
}
