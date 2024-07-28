package com.askein.gymtracker.ui.exercise.details

import com.askein.gymtracker.R
import com.askein.gymtracker.enums.DistanceUnits
import com.askein.gymtracker.ui.exercise.history.state.CardioExerciseHistoryUiState
import com.askein.gymtracker.ui.exercise.history.state.WeightsExerciseHistoryUiState
import com.askein.gymtracker.ui.user.UserPreferencesUiState
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import java.time.LocalDate

class GraphDataPointsKtTest {

    private val firstDate: LocalDate = LocalDate.now().minusDays(1)
    private val secondDate: LocalDate = LocalDate.now().minusDays(2)
    private val thirdDate: LocalDate = LocalDate.now().minusDays(3)
    private val fourthDate: LocalDate = LocalDate.now().minusDays(4)
    private val weightsHistory = listOf(
        WeightsExerciseHistoryUiState(
            weight = listOf(13.0),
            sets = 1,
            reps = listOf(2),
            rest = 1,
            date = firstDate
        ),
        WeightsExerciseHistoryUiState(
            weight = listOf(12.0, 12.0),
            sets = 2,
            reps = listOf(3, 3),
            rest = 1,
            date = secondDate
        ),
        WeightsExerciseHistoryUiState(
            weight = listOf(4.0),
            sets = 1,
            seconds = listOf(50),
            rest = 1,
            date = thirdDate
        ),
        WeightsExerciseHistoryUiState(
            weight = listOf(8.0, 10.0),
            sets = 2,
            seconds = listOf(30, 90),
            rest = 1,
            date = fourthDate
        )
    )
    private val cardioHistory = listOf(
        CardioExerciseHistoryUiState(
            minutes = 30,
            seconds = 0,
            distance = 5.0,
            date = firstDate
        ),
        CardioExerciseHistoryUiState(
            minutes = 20,
            seconds = 30,
            calories = 1200,
            date = secondDate
        ),
        CardioExerciseHistoryUiState(
            distance = 4.0,
            calories = 600,
            date = thirdDate
        )
    )

    @Test
    fun getGraphDetailsForMaxReps() {
        val result = calisthenicsGraphDataPoints(R.string.max_reps, weightsHistory)

        assertThat(result.map { it.first }, equalTo(listOf(firstDate, secondDate)))
        assertThat(result.map { it.second }, equalTo(listOf(2.0, 3.0)))
    }

    @Test
    fun getGraphDetailsForTotalReps() {
        val result = calisthenicsGraphDataPoints(R.string.total_reps, weightsHistory)

        assertThat(result.map { it.first }, equalTo(listOf(firstDate, secondDate)))
        assertThat(result.map { it.second }, equalTo(listOf(2.0, 6.0)))
    }

    @Test
    fun getGraphDetailsForMaxTime() {
        val result = calisthenicsGraphDataPoints(R.string.max_time, weightsHistory)

        assertThat(result.map { it.first }, equalTo(listOf(thirdDate, fourthDate)))
        assertThat(result.map { it.second }, equalTo(listOf(50.0, 90.0)))
    }

    @Test
    fun getGraphDetailsForTotalTime() {
        val result = calisthenicsGraphDataPoints(R.string.total_time, weightsHistory)

        assertThat(result.map { it.first }, equalTo(listOf(thirdDate, fourthDate)))
        assertThat(result.map { it.second }, equalTo(listOf(50.0, 120.0)))
    }

    @Test
    fun getGraphDetailsForMaxSets() {
        val result = calisthenicsGraphDataPoints(R.string.max_sets, weightsHistory)

        assertThat(
            result.map { it.first },
            equalTo(listOf(firstDate, secondDate, thirdDate, fourthDate))
        )
        assertThat(
            result.map { it.second },
            equalTo(listOf(1.0, 2.0, 1.0, 2.0))
        )
    }

    @Test
    fun getGraphDetailsForInvalidOption() {
        val result = calisthenicsGraphDataPoints(0, weightsHistory)

        assertThat(result.map { it.first }, equalTo(listOf()))
        assertThat(result.map { it.second }, equalTo(listOf()))
    }

    @Test
    fun getGraphDetailsForDefaultDistance() {
        val result = cardioGraphDataPoints(
            R.string.distance,
            UserPreferencesUiState(),
            cardioHistory
        )

        assertThat(result.map { it.first }, equalTo(listOf(firstDate, thirdDate)))
        assertThat(result.map { it.second }, equalTo(listOf(5.0, 4.0)))
    }

    @Test
    fun getGraphDetailsForPreferredDistance() {
        val result = cardioGraphDataPoints(
            R.string.distance,
            UserPreferencesUiState(defaultDistanceUnit = DistanceUnits.MILES),
            cardioHistory
        )

        assertThat(result.map { it.first }, equalTo(listOf(firstDate, thirdDate)))
        assertThat(result.map { it.second }, equalTo(listOf(3.11, 2.49)))
    }

    @Test
    fun getGraphDetailsForTime() {
        val result = cardioGraphDataPoints(
            R.string.time,
            UserPreferencesUiState(),
            cardioHistory
        )

        assertThat(result.map { it.first }, equalTo(listOf(firstDate, secondDate)))
        assertThat(result.map { it.second }, equalTo(listOf(1800.0, 1230.0)))
    }

    @Test
    fun getGraphDetailsForCalories() {
        val result = cardioGraphDataPoints(
            R.string.calories,
            UserPreferencesUiState(),
            cardioHistory
        )

        assertThat(result.map { it.first }, equalTo(listOf(secondDate, thirdDate)))
        assertThat(result.map { it.second }, equalTo(listOf(1200.0, 600.0)))
    }
}
