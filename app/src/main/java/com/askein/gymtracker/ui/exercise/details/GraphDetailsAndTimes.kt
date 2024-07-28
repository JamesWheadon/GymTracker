package com.askein.gymtracker.ui.exercise.details

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.askein.gymtracker.R
import com.askein.gymtracker.ui.DropdownBox
import java.time.LocalDate

@Composable
fun GraphOptions(
    @StringRes detailOptions: List<Int>,
    detailOnChange: (Int) -> Unit,
    @StringRes timeOptions: List<Int>,
    timeOnChange: (Int) -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.Top,
        modifier = Modifier.fillMaxWidth()
    ) {
        DropdownBox(
            options = detailOptions,
            onChange = { newDetail ->
                detailOnChange(newDetail)
            },
            modifier = Modifier
                .weight(1f)
        )
        Spacer(modifier = Modifier.width(16.dp))
        DropdownBox(
            options = timeOptions,
            onChange = { newTime ->
                timeOnChange(newTime)
            },
            modifier = Modifier
                .weight(1f)
        )
    }
}

fun graphOptionsForWeightsExercise(uiState: ExerciseDetailsUiState): GraphDetailsAndTimes {
    val repsPresent = uiState.weightsHistory.any { history -> history.reps != null }
    val timePresent = uiState.weightsHistory.any { history -> history.seconds != null }
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
    val timeOptionToStartTime = mapOf<Int, LocalDate>(
        R.string.seven_days to LocalDate.now().minusDays(7),
        R.string.thirty_days to LocalDate.now().minusDays(30),
        R.string.past_year to LocalDate.of(LocalDate.now().year, 1, 1),
        R.string.all_time to
                uiState.weightsHistory.minBy { history -> history.date.toEpochDay() }.date
    )
    return GraphDetailsAndTimes(
        detailOptions = detailOptions,
        timeOptionToStartTime = timeOptionToStartTime
    )
}

fun graphOptionsForCardioExercise(uiState: ExerciseDetailsUiState): GraphDetailsAndTimes {
    val detailOptions = listOf(R.string.distance, R.string.time, R.string.calories)
    val timeOptionToStartTime = mapOf<Int, LocalDate>(
        R.string.seven_days to LocalDate.now().minusDays(7),
        R.string.thirty_days to LocalDate.now().minusDays(30),
        R.string.past_year to LocalDate.of(LocalDate.now().year, 1, 1),
        R.string.all_time to uiState.cardioHistory.minBy { history -> history.date.toEpochDay() }.date
    )
    return GraphDetailsAndTimes(
        detailOptions = detailOptions,
        timeOptionToStartTime = timeOptionToStartTime
    )
}

data class GraphDetailsAndTimes(
    val detailOptions: List<Int>,
    val timeOptionToStartTime: Map<Int, LocalDate>
)