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
import com.askein.gymtracker.util.getTimeStringResourceFromSeconds
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
    val repsPresent = uiState.weightsHistory.any { history -> history.reps != null }
    val timePresent = uiState.weightsHistory.any { history -> history.seconds != null }
    val timeOptions =
        listOf(R.string.seven_days, R.string.thirty_days, R.string.past_year, R.string.all_time)
    val detailOptions = mutableListOf(
        R.string.max_reps,
        R.string.max_time,
        R.string.max_sets,
        R.string.total_reps,
        R.string.total_time
    )
    if (!repsPresent) {
        detailOptions.removeAll(listOf(R.string.max_reps, R.string.total_reps))
    }
    if (!timePresent) {
        detailOptions.removeAll(listOf(R.string.max_time, R.string.total_time))
    }
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
        detail
    ).filter { !it.first.isBefore(timeOptionToStartTime[time] ?: currentDate) }
    if (dataPoints.isNotEmpty()) {
        val yUnit = when (detail) {
            R.string.max_time, R.string.total_time -> stringResource(
                id = R.string.seconds_unit
            )
            else -> ""
        }
        Graph(
            points = dataPoints,
            startDate = timeOptionToStartTime[time] ?: currentDate,
            yLabel = stringResource(id = detail),
            yUnit = yUnit
        )
    } else {
        Text(text = stringResource(id = R.string.no_data_error))
    }
}

@Composable
private fun CalisthenicsExerciseDetailsBestAndRecent(
    uiState: ExerciseDetailsUiState
) {
    val bestReps = uiState.weightsHistory.filter { history -> history.reps != null }
        .maxByOrNull { history -> history.reps!!.max() }
    val bestTime = uiState.weightsHistory.filter { history -> history.seconds != null }
        .maxByOrNull { history -> history.seconds!!.max() }
    val recent = uiState.weightsHistory.maxBy { history -> history.date.toEpochDay() }
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier.fillMaxWidth()
    ) {
        if (bestReps != null) {
            ExerciseDetail(
                exerciseInfo = stringResource(
                    id = R.string.calisthenics_exercise_reps,
                    bestReps.reps!!.max()
                ),
                iconId = R.drawable.trophy_48dp,
                iconDescription = R.string.best_exercise_icon,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            )
        }
        if (bestTime != null) {
            val (resourceId, resourceArgs) = getTimeStringResourceFromSeconds(bestTime.seconds!!.max())
            ExerciseDetail(
                exerciseInfo = stringResource(
                    id = resourceId,
                    *resourceArgs.toTypedArray()
                ),
                iconId = R.drawable.trophy_48dp,
                iconDescription = R.string.best_exercise_icon,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            )
        }
        if (recent.reps != null) {
            ExerciseDetail(
                exerciseInfo = stringResource(
                    id = R.string.calisthenics_exercise_reps,
                    recent.reps!!.last()
                ),
                iconId = R.drawable.history_48px,
                iconDescription = R.string.recent_exercise_icon,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            )
        } else {
            val (resourceId, resourceArgs) = getTimeStringResourceFromSeconds(recent.seconds!!.max())
            ExerciseDetail(
                exerciseInfo = stringResource(
                    id = resourceId,
                    *resourceArgs.toTypedArray()
                ),
                iconId = R.drawable.history_48px,
                iconDescription = R.string.recent_exercise_icon,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            )
        }
    }
}

fun getCalisthenicsGraphDetails(
    uiState: ExerciseDetailsUiState,
    detail: Int
) = uiState.weightsHistory.mapNotNull { history ->
    when (detail) {
        R.string.max_reps -> {
            val reps = history.reps?.max()?.toDouble()
            if (reps == null) {
                null
            } else {
                Pair(history.date, reps)
            }
        }

        R.string.total_reps -> {
            val reps = history.reps?.sum()?.toDouble()
            if (reps == null) {
                null
            } else {
                Pair(history.date, reps)
            }
        }

        R.string.max_time -> {
            val seconds = history.seconds?.max()?.toDouble()
            if (seconds == null) {
                null
            } else {
                Pair(history.date, seconds)
            }
        }

        R.string.total_time -> {
            val seconds = history.seconds?.sum()?.toDouble()
            if (seconds == null) {
                null
            } else {
                Pair(history.date, seconds)
            }
        }

        R.string.max_sets -> {
            Pair(history.date, history.sets.toDouble())
        }

        else -> {
            null
        }
    }
}
