package com.askein.gymtracker.ui.exercise.details

import com.askein.gymtracker.R
import com.askein.gymtracker.enums.DistanceUnits
import com.askein.gymtracker.enums.WeightUnits
import com.askein.gymtracker.enums.convertToDistanceUnit
import com.askein.gymtracker.enums.convertToWeightUnit
import com.askein.gymtracker.ui.exercise.history.state.CardioExerciseHistoryUiState
import com.askein.gymtracker.ui.exercise.history.state.WeightsExerciseHistoryUiState
import com.askein.gymtracker.ui.user.UserPreferencesUiState
import java.time.LocalDate

fun calisthenicsAndWeightsGraphDataPoints(
    chosenDetail: Int,
    historyUiStates: List<WeightsExerciseHistoryUiState>,
    weightUnit: WeightUnits = WeightUnits.KILOGRAMS
): List<Pair<LocalDate, Double>> = historyUiStates.mapNotNull { history ->
    when (chosenDetail) {
        R.string.max_weight -> {
            if (weightUnit == WeightUnits.KILOGRAMS) {
                Pair(
                    history.date,
                    history.weight.max()
                )
            } else {
                Pair(
                    history.date,
                    history.weight.maxOf {
                        convertToWeightUnit(weightUnit, it)
                    }
                )
            }
        }

        R.string.max_reps -> {
            val reps = history.reps?.max()?.toDouble()
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

        R.string.max_sets -> {
            Pair(history.date, history.sets.toDouble())
        }

        R.string.total_weight -> {
            if (history.reps == null) {
                null
            } else {
                if (weightUnit == WeightUnits.KILOGRAMS) {
                    Pair(
                        history.date,
                        history.weight.zip(history.reps!!).sumOf { it.first * it.second }
                    )
                } else {
                    Pair(
                        history.date,
                        history.weight
                            .map {
                                convertToWeightUnit(weightUnit, it)
                            }.zip(history.reps!!)
                            .sumOf { it.first * it.second }
                    )
                }
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

        R.string.total_time -> {
            val seconds = history.seconds?.sum()?.toDouble()
            if (seconds == null) {
                null
            } else {
                Pair(history.date, seconds)
            }
        }

        else -> {
            null
        }
    }
}

fun cardioGraphDataPoints(
    detail: Int,
    preferences: UserPreferencesUiState,
    historyUiStates: List<CardioExerciseHistoryUiState>
) = historyUiStates.mapNotNull { history ->
    when (detail) {
        R.string.distance -> {
            if (history.distance == null) {
                null
            } else {
                val distance = if (preferences.defaultDistanceUnit == DistanceUnits.KILOMETERS) {
                    history.distance!!
                } else {
                    convertToDistanceUnit(preferences.defaultDistanceUnit, history.distance!!)
                }
                Pair(history.date, distance)
            }
        }

        R.string.time -> {
            if (history.seconds == null) {
                null
            } else {
                Pair(
                    history.date,
                    (history.minutes!! * 60 + history.seconds!!).toDouble()
                )
            }
        }

        R.string.calories -> {
            if (history.calories == null) {
                null
            } else {
                Pair(
                    history.date,
                    history.calories!!.toDouble()
                )
            }
        }

        else -> {
            null
        }
    }
}
