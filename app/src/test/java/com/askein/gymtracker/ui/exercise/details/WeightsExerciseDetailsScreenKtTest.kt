package com.askein.gymtracker.ui.exercise.details

import com.askein.gymtracker.enums.WeightUnits
import com.askein.gymtracker.ui.exercise.ExerciseUiState
import com.askein.gymtracker.ui.exercise.history.state.WeightsExerciseHistoryUiState
import com.askein.gymtracker.ui.user.UserPreferencesUiState
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import java.time.LocalDate

class WeightsExerciseDetailsScreenKtTest {

    private val options = listOf(1, 2, 3, 4)
    private val firstDate: LocalDate = LocalDate.now().minusDays(3)
    private val secondDate: LocalDate = LocalDate.now().minusDays(5)
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
            )
        )
    )

    @Test
    fun getGraphDetailsForFirstOption() {
        val result = getWeightsGraphDetails(exercise, 1, options, UserPreferencesUiState())

        assertThat(result.map { it.first }, equalTo(listOf(firstDate, secondDate)))
        assertThat(result.map { it.second }, equalTo(listOf(13.0, 12.0)))
    }

    @Test
    fun getGraphDetailsForFirstOptionNonKilogramsUnit() {
        val result = getWeightsGraphDetails(
            exercise,
            5,
            options,
            UserPreferencesUiState(defaultWeightUnit = WeightUnits.POUNDS)
        )

        assertThat(result.map { it.first }, equalTo(listOf(firstDate, secondDate)))
        assertThat(result.map { it.second }, equalTo(listOf(28.66, 26.46)))
    }

    @Test
    fun getGraphDetailsForSecondOption() {
        val result = getWeightsGraphDetails(exercise, 2, options, UserPreferencesUiState())

        assertThat(result.map { it.first }, equalTo(listOf(firstDate, secondDate)))
        assertThat(result.map { it.second }, equalTo(listOf(2.0, 3.0)))
    }

    @Test
    fun getGraphDetailsForThirdOption() {
        val result = getWeightsGraphDetails(exercise, 3, options, UserPreferencesUiState())

        assertThat(result.map { it.first }, equalTo(listOf(firstDate, secondDate)))
        assertThat(result.map { it.second }, equalTo(listOf(1.0, 2.0)))
    }

    @Test
    fun getGraphDetailsForFourthOption() {
        val result = getWeightsGraphDetails(exercise, 4, options, UserPreferencesUiState())

        assertThat(result.map { it.first }, equalTo(listOf(firstDate, secondDate)))
        assertThat(result.map { it.second }, equalTo(listOf(26.0, 72.0)))
    }

    @Test
    fun getGraphDetailsForOtherOption() {
        val result = getWeightsGraphDetails(exercise, 5, options, UserPreferencesUiState())

        assertThat(result.map { it.first }, equalTo(listOf(firstDate, secondDate)))
        assertThat(result.map { it.second }, equalTo(listOf(13.0, 12.0)))
    }

    @Test
    fun getGraphDetailsForOtherOptionNonKilogramsUnit() {
        val result = getWeightsGraphDetails(
            exercise,
            5,
            options,
            UserPreferencesUiState(defaultWeightUnit = WeightUnits.POUNDS)
        )

        assertThat(result.map { it.first }, equalTo(listOf(firstDate, secondDate)))
        assertThat(result.map { it.second }, equalTo(listOf(28.66, 26.46)))
    }
}
