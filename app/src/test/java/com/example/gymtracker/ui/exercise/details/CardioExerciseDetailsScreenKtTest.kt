package com.example.gymtracker.ui.exercise.details

import com.example.gymtracker.ui.exercise.ExerciseUiState
import com.example.gymtracker.ui.exercise.history.state.CardioExerciseHistoryUiState
import com.example.gymtracker.ui.user.UserPreferencesUiState
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import java.time.LocalDate

class CardioExerciseDetailsScreenKtTest {

    private val options = listOf(1, 2, 3)
    private val firstDate: LocalDate = LocalDate.now().minusDays(3)
    private val secondDate: LocalDate = LocalDate.now().minusDays(5)
    private val thirdDate: LocalDate = LocalDate.now().minusDays(7)
    private val exercise = ExerciseDetailsUiState(
        ExerciseUiState(
            name = "Curls"
        ),
        cardioHistory = listOf(
            CardioExerciseHistoryUiState(
                minutes = 30,
                seconds = 0,
                distance = 5.0,
                calories = 1000,
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
    )

    @Test
    fun getGraphDetailsForFirstOption() {
        val result = getCardioGraphDetails(exercise, 1, options, UserPreferencesUiState())

        assertThat(result.map { it.first }, equalTo(listOf(firstDate, thirdDate)))
        assertThat(result.map { it.second }, equalTo(listOf(5.0, 4.0)))
    }

    @Test
    fun getGraphDetailsForSecondOption() {
        val result = getCardioGraphDetails(exercise, 2, options, UserPreferencesUiState())

        assertThat(result.map { it.first }, equalTo(listOf(firstDate, secondDate)))
        assertThat(result.map { it.second }, equalTo(listOf(1800.0, 1230.0)))
    }

    @Test
    fun getGraphDetailsForThirdOption() {
        val result = getCardioGraphDetails(exercise, 3, options, UserPreferencesUiState())

        assertThat(result.map { it.first }, equalTo(listOf(firstDate, secondDate, thirdDate)))
        assertThat(result.map { it.second }, equalTo(listOf(1000.0, 1200.0, 600.0)))
    }

    @Test
    fun getGraphDetailsForOtherOption() {
        val result = getCardioGraphDetails(exercise, 4, options, UserPreferencesUiState())

        assertThat(result.map { it.first }, equalTo(listOf(firstDate, thirdDate)))
        assertThat(result.map { it.second }, equalTo(listOf(5.0, 4.0)))
    }
}
