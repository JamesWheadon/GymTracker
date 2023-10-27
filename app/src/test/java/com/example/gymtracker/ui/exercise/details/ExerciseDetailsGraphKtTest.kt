package com.example.gymtracker.ui.exercise.details

import com.example.gymtracker.ui.exercise.ExerciseDetailsUiState
import com.example.gymtracker.ui.history.ExerciseHistoryUiState
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import java.time.LocalDate


val firstDate: LocalDate = LocalDate.now().minusDays(3)
val secondDate: LocalDate = LocalDate.now().minusDays(5)

class ExerciseDetailsGraphKtTest {

    private val options = listOf("1", "2", "3", "4")
    private val exercise = ExerciseDetailsUiState(
        name = "Curls",
        muscleGroup = "Biceps",
        equipment = "Dumbbells",
        history = listOf(
            ExerciseHistoryUiState(
                id = 1,
                weight = 13.0,
                sets = 1,
                reps = 2,
                rest = 1,
                date = firstDate
            ),
            ExerciseHistoryUiState(
                id = 1,
                weight = 12.0,
                sets = 2,
                reps = 3,
                rest = 1,
                date = secondDate
            )
        )
    )

    @Test
    fun getGraphDetailsForFirstOption() {
        val result = getGraphDetails(exercise, "1", options)

        assertThat(result.map { it.first }, equalTo(listOf(firstDate, secondDate)))
        assertThat(result.map { it.second }, equalTo(listOf(13.0, 12.0)))
    }

    @Test
    fun getGraphDetailsForSecondOption() {
        val result = getGraphDetails(exercise, "2", options)

        assertThat(result.map { it.first }, equalTo(listOf(firstDate, secondDate)))
        assertThat(result.map { it.second }, equalTo(listOf(2.0, 3.0)))
    }

    @Test
    fun getGraphDetailsForThirdOption() {
        val result = getGraphDetails(exercise, "3", options)

        assertThat(result.map { it.first }, equalTo(listOf(firstDate, secondDate)))
        assertThat(result.map { it.second }, equalTo(listOf(1.0, 2.0)))
    }

    @Test
    fun getGraphDetailsForFourthOption() {
        val result = getGraphDetails(exercise, "4", options)

        assertThat(result.map { it.first }, equalTo(listOf(firstDate, secondDate)))
        assertThat(result.map { it.second }, equalTo(listOf(26.0, 72.0)))
    }

    @Test
    fun getGraphDetailsForOtherOption() {
        val result = getGraphDetails(exercise, "5", options)

        assertThat(result.map { it.first }, equalTo(listOf(firstDate, secondDate)))
        assertThat(result.map { it.second }, equalTo(listOf(13.0, 12.0)))
    }
}
