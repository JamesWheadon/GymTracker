package com.askein.gymtracker.ui.exercise.details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.unit.dp
import com.askein.gymtracker.R
import com.askein.gymtracker.enums.convertToWeightUnit
import com.askein.gymtracker.ui.user.LocalUserPreferences
import java.time.LocalDate

@Composable
fun CalisthenicsExerciseDetailsScreen(
    innerPadding: PaddingValues,
    uiState: ExerciseDetailsUiState,
    chosenDate: LocalDate?
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 16.dp, horizontal = 16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        CalisthenicsExerciseInformation(innerPadding, uiState)
        if (uiState.weightsHistory.isNotEmpty()) {
            CalisthenicsExerciseHistoryDetails(uiState = uiState)
            ExerciseHistoryCalendar(
                uiState = uiState,
                chosenDate = chosenDate
            )
        }
        Spacer(modifier = Modifier.height(72.dp))
    }
}

@Composable
private fun CalisthenicsExerciseInformation(
    innerPadding: PaddingValues,
    uiState: ExerciseDetailsUiState
) {
    if (uiState.exercise.muscleGroup != "") {
        ExerciseDetail(
            exerciseInfo = uiState.exercise.muscleGroup,
            iconId = R.drawable.info_48px,
            iconDescription = R.string.muscle_icon,
            modifier = Modifier
                .fillMaxWidth()
                .padding(innerPadding)
        )
    } else {
        ExerciseDetail(
            exerciseInfo = stringResource(id = R.string.calisthenics),
            iconId = R.drawable.info_48px,
            iconDescription = R.string.calisthenics_icon,
            modifier = Modifier
                .fillMaxWidth()
                .padding(innerPadding)
        )
    }
}

@Composable
fun CalisthenicsExerciseHistoryDetails(
    uiState: ExerciseDetailsUiState
) {
    val timeOptions =
        listOf(R.string.seven_days, R.string.thirty_days, R.string.past_year, R.string.all_time)
    val detailOptions =
        listOf(R.string.max_reps, R.string.max_sets, R.string.total_reps)
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
    CalisthenicsExerciseDetailsBestAndRecent(uiState)
    GraphOptions(
        detailOptions = detailOptions,
        detailOnChange = { newDetail -> detail = newDetail },
        timeOptions = timeOptions,
        timeOnChange = { newTime -> time = newTime }
    )
    val dataPoints = getCalisthenicsGraphDetails(
        uiState,
        detail,
        detailOptions
    ).filter { !it.first.isBefore(timeOptionToStartTime[time] ?: currentDate) }
    if (dataPoints.isNotEmpty()) {
        Graph(
            points = dataPoints,
            startDate = timeOptionToStartTime[time] ?: currentDate,
            yLabel = stringResource(id = detail),
            yUnit = ""
        )
    } else {
        Text(text = stringResource(id = R.string.no_data_error))
    }
}

@Composable
private fun CalisthenicsExerciseDetailsBestAndRecent(
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

fun getCalisthenicsGraphDetails(
    uiState: ExerciseDetailsUiState,
    detail: Int,
    detailOptions: List<Int>
) = uiState.weightsHistory.map { history ->
    when (detail) {
        detailOptions[0] -> {
            Pair(
                history.date,
                history.reps.toDouble()
            )
        }

        detailOptions[1] -> {
            Pair(
                history.date,
                history.sets.toDouble()
            )
        }

        detailOptions[2] -> {
            Pair(
                history.date,
                history.sets.toDouble() * history.reps.toDouble()
            )
        }

        else -> {
            Pair(
                history.date,
                history.reps.toDouble()
            )
        }
    }
}
