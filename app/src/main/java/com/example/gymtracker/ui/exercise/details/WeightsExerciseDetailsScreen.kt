package com.example.gymtracker.ui.exercise.details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.gymtracker.R
import com.example.gymtracker.converters.WeightUnits
import com.example.gymtracker.ui.exercise.ExerciseUiState
import com.example.gymtracker.ui.exercise.history.state.WeightsExerciseHistoryUiState
import com.example.gymtracker.ui.theme.GymTrackerTheme
import java.time.LocalDate


@Composable
fun WeightsExerciseDetailsScreen(
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
        WeightsExerciseInformation(innerPadding, uiState)
        if (uiState.weightsHistory.isNotEmpty()) {
            WeightsExerciseHistoryDetails(uiState = uiState)
            ExerciseHistoryCalendar(uiState = uiState)
        }
    }
}

@Composable
private fun WeightsExerciseInformation(
    innerPadding: PaddingValues,
    uiState: ExerciseDetailsUiState
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(innerPadding)
    ) {
        ExerciseDetail(
            exerciseInfo = uiState.exercise.muscleGroup,
            iconId = R.drawable.info_48px,
            iconDescription = "exercise icon",
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        )
        ExerciseDetail(
            exerciseInfo = uiState.exercise.equipment,
            iconId = R.drawable.exercise_filled_48px,
            iconDescription = "equipment icon",
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        )
    }
}

@Composable
fun WeightsExerciseHistoryDetails(uiState: ExerciseDetailsUiState) {
    val timeOptions = listOf("7 Days", "30 Days", "Past Year", "All Time")
    val detailOptions = listOf("Max Weight", "Max Reps", "Max Sets", "Total Weight")
    val currentDate = LocalDate.now()
    val timeOptionToStartTime = mapOf<String, LocalDate>(
        Pair(timeOptions[0], currentDate.minusDays(7)),
        Pair(timeOptions[1], currentDate.minusDays(30)),
        Pair(timeOptions[2], LocalDate.of(currentDate.year, 1, 1)),
        Pair(
            timeOptions[3],
            uiState.weightsHistory.minBy { history -> history.date.toEpochDay() }.date
        ),
    )
    var detail by remember { mutableStateOf(detailOptions[0]) }
    var time by remember { mutableStateOf(timeOptions[0]) }
    WeightsExerciseDetailsBestAndRecent(uiState)
    GraphOptions(
        detailOptions = detailOptions,
        detailOnChange = { newDetail -> detail = newDetail },
        timeOptions = timeOptions
    ) { newTime -> time = newTime }
    Graph(
        points = getWeightsGraphDetails(uiState, detail, detailOptions),
        startDate = timeOptionToStartTime[time] ?: currentDate,
        yLabel = detail,
        yUnit = if (detail == detailOptions[0] || detail == detailOptions[3]) " ${WeightUnits.KILOGRAMS.shortForm}" else ""
    )
}

@Composable
private fun WeightsExerciseDetailsBestAndRecent(uiState: ExerciseDetailsUiState) {
    val bestWeight = uiState.weightsHistory.maxOf { history -> history.weight }
    val best = uiState.weightsHistory
        .filter { history -> history.weight == bestWeight }
        .maxBy { history -> history.reps }
    val recent = uiState.weightsHistory.maxBy { history -> history.date.toEpochDay() }
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier.fillMaxWidth()
    ) {
        ExerciseDetail(
            exerciseInfo = "${best.weight} ${WeightUnits.KILOGRAMS.shortForm} for ${best.reps} reps",
            iconId = R.drawable.trophy_48dp,
            iconDescription = "best exercise icon",
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        )
        ExerciseDetail(
            exerciseInfo = "${recent.weight} ${WeightUnits.KILOGRAMS.shortForm} for ${recent.reps} reps",
            iconId = R.drawable.history_48px,
            iconDescription = "recent exercise icon",
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
    iconDescription: String,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
        modifier = modifier
    ) {
        Icon(
            painter = painterResource(id = iconId),
            contentDescription = iconDescription,
            tint = MaterialTheme.colorScheme.tertiary,
            modifier = Modifier.size(20.dp)
        )
        Text(
            text = exerciseInfo,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

fun getWeightsGraphDetails(
    uiState: ExerciseDetailsUiState,
    detail: String,
    detailOptions: List<String>
) = uiState.weightsHistory.map { history ->
    when (detail) {
        detailOptions[0] -> {
            Pair(
                history.date,
                history.weight
            )
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
            Pair(
                history.date,
                history.weight
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ItemDetailsScreenPreviewNoHistory() {
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

@Preview(showBackground = true)
@Composable
fun ItemDetailsScreenPreviewHistory() {
    GymTrackerTheme(darkTheme = false) {
        WeightsExerciseDetailsScreen(
            innerPadding = PaddingValues(),
            uiState = ExerciseDetailsUiState(
                exercise = ExerciseUiState(
                    name = "Curls",
                    muscleGroup = "Biceps",
                    equipment = "Dumbbells"
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
