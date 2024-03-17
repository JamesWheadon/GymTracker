package com.example.gymtracker.ui.exercise.details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.gymtracker.R
import com.example.gymtracker.enums.DistanceUnits
import com.example.gymtracker.enums.convertToDistanceUnit
import com.example.gymtracker.ui.exercise.ExerciseUiState
import com.example.gymtracker.ui.exercise.history.state.CardioExerciseHistoryUiState
import com.example.gymtracker.ui.theme.GymTrackerTheme
import com.example.gymtracker.ui.user.LocalUserPreferences
import com.example.gymtracker.ui.user.UserPreferencesUiState
import java.time.LocalDate

@Composable
fun CardioExerciseDetailsScreen(
    innerPadding: PaddingValues,
    uiState: ExerciseDetailsUiState
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(vertical = 16.dp, horizontal = 16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        CardioExerciseInformation(innerPadding)
        if (uiState.cardioHistory.isNotEmpty()) {
            CardioExerciseHistoryDetails(
                uiState = uiState
            )
            ExerciseHistoryCalendar(uiState = uiState)
        }
        Spacer(modifier = Modifier.height(72.dp))
    }
}

@Composable
private fun CardioExerciseInformation(
    innerPadding: PaddingValues
) {
    ExerciseDetail(
        exerciseInfo = stringResource(id = R.string.cardio),
        iconId = R.drawable.cardio_48dp,
        iconDescription = R.string.cardio_icon,
        modifier = Modifier
            .fillMaxWidth()
            .padding(innerPadding)
    )
}

@Composable
fun CardioExerciseHistoryDetails(
    uiState: ExerciseDetailsUiState
) {
    val timeOptions = listOf(R.string.seven_days, R.string.thirty_days, R.string.past_year, R.string.all_time)
    val detailOptions = listOf(R.string.distance, R.string.time, R.string.calories)
    val yUnit = mapOf(
        detailOptions[0] to LocalUserPreferences.current.defaultDistanceUnit.shortForm,
        detailOptions[1] to R.string.minute_unit,
        detailOptions[2] to R.string.calories_unit
    )
    val currentDate = LocalDate.now()
    val timeOptionToStartTime = mapOf<Int, LocalDate>(
        Pair(timeOptions[0], currentDate.minusDays(7)),
        Pair(timeOptions[1], currentDate.minusDays(30)),
        Pair(timeOptions[2], LocalDate.of(currentDate.year, 1, 1)),
        Pair(
            timeOptions[3],
            uiState.cardioHistory.minBy { history -> history.date.toEpochDay() }.date
        ),
    )
    var detail by remember { mutableIntStateOf(detailOptions[0]) }
    var time by remember { mutableIntStateOf(timeOptions[0]) }
    CardioExerciseDetailsBest(uiState = uiState)
    GraphOptions(
        detailOptions = detailOptions,
        detailOnChange = { newDetail -> detail = newDetail },
        timeOptions = timeOptions,
        timeOnChange = { newTime -> time = newTime }
    )
    val dataPoints = getCardioGraphDetails(
        uiState = uiState,
        detail = detail,
        detailOptions = detailOptions,
        userPreferencesUiState = LocalUserPreferences.current
    )
    if (dataPoints.isNotEmpty()) {
        Graph(
            points = dataPoints,
            startDate = timeOptionToStartTime[time] ?: currentDate,
            yLabel = stringResource(id = detail),
            yUnit = stringResource(id = yUnit[detail]!!)
        )
    } else {
        Text(text = stringResource(id = R.string.no_data_error))
    }
}

@Composable
private fun CardioExerciseDetailsBest(
    uiState: ExerciseDetailsUiState
) {
    val userPreferencesUiState = LocalUserPreferences.current
    var bestDistance = uiState.cardioHistory.maxOf { it.distance ?: 0.0 }
    if (userPreferencesUiState.defaultDistanceUnit != DistanceUnits.KILOMETERS) {
        bestDistance =
            convertToDistanceUnit(userPreferencesUiState.defaultDistanceUnit, bestDistance)
    }
    val bestTime = if (userPreferencesUiState.displayShortestTime) {
        uiState.cardioHistory.filter { it.minutes != null }
            .minOfOrNull { (it.minutes?.times(60) ?: 0) + (it.seconds ?: 0) } ?: 0
    } else {
        uiState.cardioHistory.maxOf { (it.minutes?.times(60) ?: 0) + (it.seconds ?: 0) }
    }
    val bestCalories = uiState.cardioHistory.maxOf { it.calories ?: 0 }
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier.fillMaxWidth()
    ) {
        if (bestDistance != 0.0) {
            ExerciseDetail(
                exerciseInfo = stringResource(
                    id = R.string.best_distance,
                    bestDistance,
                    stringResource(id = userPreferencesUiState.defaultDistanceUnit.shortForm)
                ),
                iconId = R.drawable.trophy_48dp,
                iconDescription = R.string.best_exercise_icon,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            )
        }
        if (bestTime != 0) {
            val bestTimeString = if (bestTime >= 3600) {
                stringResource(
                    id = R.string.display_hours,
                    bestTime / 3600,
                    String.format("%02d", (bestTime % 3600) / 60),
                    String.format("%02d", bestTime % 60)
                )
            } else if (bestTime >= 60) {
                stringResource(
                    id = R.string.display_minutes,
                    String.format("%02d", (bestTime % 3600) / 60),
                    String.format("%02d", bestTime % 60)
                )
            } else {
                stringResource(
                    id = R.string.display_seconds,
                    String.format("%02d", bestTime % 60)
                )
            }
            ExerciseDetail(
                exerciseInfo = bestTimeString,
                iconId = R.drawable.trophy_48dp,
                iconDescription = R.string.best_exercise_icon,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            )
        }
        if (bestCalories != 0) {
            ExerciseDetail(
                exerciseInfo = stringResource(id = R.string.best_calories, bestCalories),
                iconId = R.drawable.trophy_48dp,
                iconDescription = R.string.best_exercise_icon,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            )
        }
    }
}

fun getCardioGraphDetails(
    uiState: ExerciseDetailsUiState,
    detail: Int,
    detailOptions: List<Int>,
    userPreferencesUiState: UserPreferencesUiState
) = uiState.cardioHistory.map { history ->
    when (detail) {
        detailOptions[0] -> {
            if (userPreferencesUiState.defaultDistanceUnit == DistanceUnits.KILOMETERS) {
                Pair(
                    history.date,
                    history.distance ?: 0.0
                )
            } else {
                Pair(
                    history.date,
                    convertToDistanceUnit(
                        userPreferencesUiState.defaultDistanceUnit,
                        history.distance ?: 0.0
                    )
                )
            }
        }

        detailOptions[1] -> {
            Pair(
                history.date,
                (history.minutes ?: 0) * 60 + (history.seconds ?: 0).toDouble()
            )
        }

        detailOptions[2] -> {
            Pair(
                history.date,
                (history.calories ?: 0).toDouble()
            )
        }

        else -> {
            if (userPreferencesUiState.defaultDistanceUnit == DistanceUnits.KILOMETERS) {
                Pair(
                    history.date,
                    history.distance ?: 0.0
                )
            } else {
                Pair(
                    history.date,
                    convertToDistanceUnit(
                        userPreferencesUiState.defaultDistanceUnit,
                        history.distance ?: 0.0
                    )
                )
            }
        }
    }
}.filter { pair -> pair.second != 0.0 }

@Preview(showBackground = true)
@Composable
fun ItemDetailsScreenPreviewNoCardioHistory() {
    GymTrackerTheme(darkTheme = false) {
        CardioExerciseDetailsScreen(
            innerPadding = PaddingValues(),
            uiState = ExerciseDetailsUiState(
                exercise = ExerciseUiState(
                    name = "Curls",
                    muscleGroup = "Biceps",
                    equipment = "Dumbbells"
                ),
                cardioHistory = listOf()
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ItemDetailsScreenPreviewCardioHistory() {
    GymTrackerTheme(darkTheme = false) {
        CardioExerciseDetailsScreen(
            innerPadding = PaddingValues(),
            uiState = ExerciseDetailsUiState(
                exercise = ExerciseUiState(
                    name = "Cycling",
                ),
                cardioHistory = listOf(
                    CardioExerciseHistoryUiState(
                        distance = 10.0,
                        minutes = 0,
                        seconds = 30,
                        calories = 450
                    )
                )
            )
        )
    }
}
