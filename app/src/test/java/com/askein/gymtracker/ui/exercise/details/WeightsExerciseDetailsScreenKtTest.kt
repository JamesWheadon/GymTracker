package com.askein.gymtracker.ui.exercise.details

import com.askein.gymtracker.R
import com.askein.gymtracker.enums.WeightUnits
import com.askein.gymtracker.ui.exercise.ExerciseUiState
import com.askein.gymtracker.ui.exercise.history.state.WeightsExerciseHistoryUiState
import com.askein.gymtracker.ui.user.UserPreferencesUiState
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import java.time.LocalDate

class WeightsExerciseDetailsScreenKtTest {

    private val firstDate: LocalDate = LocalDate.now().minusDays(1)
    private val secondDate: LocalDate = LocalDate.now().minusDays(2)
    private val thirdDate: LocalDate = LocalDate.now().minusDays(3)
    private val fourthDate: LocalDate = LocalDate.now().minusDays(4)
    private val exercise = ExerciseDetailsUiState(
        ExerciseUiState(
            name = "Curls",
            muscleGroup = "Biceps",
            equipment = "Dumbbells"
        ),
        weightsHistory = listOf(
            WeightsExerciseHistoryUiState(
                id = 1,
                weight = listOf(13.0),
                sets = 1,
                reps = listOf(2),
                rest = 1,
                date = firstDate
            ),
            WeightsExerciseHistoryUiState(
                id = 1,
                weight = listOf(12.0, 12.0),
                sets = 2,
                reps = listOf(3, 3),
                rest = 1,
                date = secondDate
            ),
            WeightsExerciseHistoryUiState(
                id = 1,
                weight = listOf(4.0),
                sets = 1,
                seconds = listOf(50),
                rest = 1,
                date = thirdDate
            ),
            WeightsExerciseHistoryUiState(
                id = 1,
                weight = listOf(8.0, 10.0),
                sets = 2,
                seconds = listOf(30, 90),
                rest = 1,
                date = fourthDate
            )
        )
    )

    @Test
    fun getGraphDetailsForMaxWeight() {
        val result = getWeightsGraphDetails(exercise, R.string.max_weight, UserPreferencesUiState())

        assertThat(result.map { it.first }, equalTo(listOf(firstDate, secondDate, thirdDate, fourthDate)))
        assertThat(result.map { it.second }, equalTo(listOf(13.0, 12.0, 4.0, 10.0)))
    }

    @Test
    fun getGraphDetailsForMaxWeightNonKilogramsUnit() {
        val result = getWeightsGraphDetails(
            exercise,R.string.max_weight,
            UserPreferencesUiState(defaultWeightUnit = WeightUnits.POUNDS)
        )

        assertThat(result.map { it.first }, equalTo(listOf(firstDate, secondDate, thirdDate, fourthDate)))
        assertThat(result.map { it.second }, equalTo(listOf(28.66, 26.46, 8.82, 22.05)))
    }

    @Test
    fun getGraphDetailsForMaxReps() {
        val result = getWeightsGraphDetails(exercise, R.string.max_reps, UserPreferencesUiState())

        assertThat(result.map { it.first }, equalTo(listOf(firstDate, secondDate)))
        assertThat(result.map { it.second }, equalTo(listOf(2.0, 3.0)))
    }

    @Test
    fun getGraphDetailsForTotalReps() {
        val result = getWeightsGraphDetails(exercise, R.string.total_reps, UserPreferencesUiState())

        assertThat(result.map { it.first }, equalTo(listOf(firstDate, secondDate)))
        assertThat(result.map { it.second }, equalTo(listOf(2.0, 6.0)))
    }

    @Test
    fun getGraphDetailsForMaxTime() {
        val result = getWeightsGraphDetails(exercise, R.string.max_time, UserPreferencesUiState())

        assertThat(result.map { it.first }, equalTo(listOf(thirdDate, fourthDate)))
        assertThat(result.map { it.second }, equalTo(listOf(50.0, 90.0)))
    }

    @Test
    fun getGraphDetailsForTotalTime() {
        val result = getWeightsGraphDetails(exercise, R.string.total_time, UserPreferencesUiState())

        assertThat(result.map { it.first }, equalTo(listOf(thirdDate, fourthDate)))
        assertThat(result.map { it.second }, equalTo(listOf(50.0, 120.0)))
    }

    @Test
    fun getGraphDetailsForMaxSets() {
        val result = getWeightsGraphDetails(exercise, R.string.max_sets, UserPreferencesUiState())

        assertThat(result.map { it.first }, equalTo(listOf(firstDate, secondDate, thirdDate, fourthDate)))
        assertThat(result.map { it.second }, equalTo(listOf(1.0, 2.0, 1.0, 2.0)))
    }

    @Test
    fun getGraphDetailsForInvalidOption() {
        val result = getWeightsGraphDetails(exercise, 0, UserPreferencesUiState())

        assertThat(result.map { it.first }, equalTo(listOf()))
        assertThat(result.map { it.second }, equalTo(listOf()))
    }
}
