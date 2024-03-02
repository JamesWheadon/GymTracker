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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.gymtracker.R
import com.example.gymtracker.converters.DistanceUnits
import com.example.gymtracker.converters.convertToDistanceUnit
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
        exerciseInfo = "Cardio",
        iconId = R.drawable.cardio_48dp,
        iconDescription = "cardio icon",
        modifier = Modifier
            .fillMaxWidth()
            .padding(innerPadding)
    )
}

@Composable
fun CardioExerciseHistoryDetails(
    uiState: ExerciseDetailsUiState
) {
    val timeOptions = listOf("7 Days", "30 Days", "Past Year", "All Time")
    val detailOptions = listOf("Distance", "Time", "Calories")
    val yUnit = mapOf(
        detailOptions[0] to LocalUserPreferences.current.defaultDistanceUnit.shortForm,
        detailOptions[1] to "min",
        detailOptions[2] to "kcal"
    )
    val currentDate = LocalDate.now()
    val timeOptionToStartTime = mapOf<String, LocalDate>(
        Pair(timeOptions[0], currentDate.minusDays(7)),
        Pair(timeOptions[1], currentDate.minusDays(30)),
        Pair(timeOptions[2], LocalDate.of(currentDate.year, 1, 1)),
        Pair(
            timeOptions[3],
            uiState.cardioHistory.minBy { history -> history.date.toEpochDay() }.date
        ),
    )
    var detail by remember { mutableStateOf(detailOptions[0]) }
    var time by remember { mutableStateOf(timeOptions[0]) }
    CardioExerciseDetailsBest(uiState = uiState)
    GraphOptions(
        detailOptions = detailOptions,
        detailOnChange = { newDetail -> detail = newDetail },
        timeOptions = timeOptions,
        timeOnChange = { newTime -> time = newTime }
    )
    Graph(
        points = getCardioGraphDetails(
            uiState,
            detail,
            detailOptions,
            LocalUserPreferences.current
        ),
        startDate = timeOptionToStartTime[time] ?: currentDate,
        yLabel = detail,
        yUnit = yUnit[detail]!!
    )
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
                exerciseInfo = "$bestDistance ${userPreferencesUiState.defaultDistanceUnit.shortForm}",
                iconId = R.drawable.trophy_48dp,
                iconDescription = "best exercise icon",
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            )
        }
        if (bestTime != 0) {
            val bestTimeString = if (bestTime >= 3600) {
                "${bestTime / 3600}:${
                    String.format(
                        "%02d",
                        (bestTime % 3600) / 60
                    )
                }:${String.format("%02d", bestTime % 60)}"
            } else if (bestTime >= 60) {
                "${String.format("%02d", bestTime / 60)}:${String.format("%02d", bestTime % 60)}"
            } else {
                "${String.format("%02d", bestTime % 60)} s"
            }
            ExerciseDetail(
                exerciseInfo = bestTimeString,
                iconId = R.drawable.trophy_48dp,
                iconDescription = "best exercise icon",
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            )
        }
        if (bestCalories != 0) {
            ExerciseDetail(
                exerciseInfo = "$bestCalories kcal",
                iconId = R.drawable.trophy_48dp,
                iconDescription = "best exercise icon",
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            )
        }
    }
}

fun getCardioGraphDetails(
    uiState: ExerciseDetailsUiState,
    detail: String,
    detailOptions: List<String>,
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
