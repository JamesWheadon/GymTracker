package com.askein.gymtracker.ui.exercise.details

import com.askein.gymtracker.R
import com.askein.gymtracker.ui.exercise.history.state.WeightsExerciseHistoryUiState
import java.time.LocalDate

fun calisthenicsGraphDataPoints(
    chosenDetail: Int,
    historyUiStates: List<WeightsExerciseHistoryUiState>
): List<Pair<LocalDate, Double>> = historyUiStates.mapNotNull { history ->
    when (chosenDetail) {
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