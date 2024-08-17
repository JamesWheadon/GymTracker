package com.askein.gymtracker.ui.exercise.details

import com.askein.gymtracker.enums.DistanceUnits
import com.askein.gymtracker.ui.exercise.history.state.CardioExerciseHistoryUiState
import com.askein.gymtracker.ui.user.UserPreferencesUiState
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test

class CardioExerciseDetailsScreenKtTest {

    @Test
    fun shouldReturnTheBestDistanceFromHistory() {
        val bestDistance = bestDistanceForCardioExercise(
            UserPreferencesUiState(),
            listOf(
                CardioExerciseHistoryUiState(distance = 10.0),
                CardioExerciseHistoryUiState(distance = 5.0)
            )
        )

        assertThat(bestDistance, equalTo(10.0))
    }

    @Test
    fun shouldReturnTheBestDistanceForPreferredDistanceUnitFromHistory() {
        val bestDistance = bestDistanceForCardioExercise(
            UserPreferencesUiState(defaultDistanceUnit = DistanceUnits.METERS),
            listOf(
                CardioExerciseHistoryUiState(distance = 10.0),
                CardioExerciseHistoryUiState(distance = 5.0)
            )
        )

        assertThat(bestDistance, equalTo(10000.0))
    }

    @Test
    fun shouldReturnNullWhenNoDistanceInHistory() {
        val bestDistance = bestDistanceForCardioExercise(
            UserPreferencesUiState(),
            listOf(CardioExerciseHistoryUiState())
        )

        assertThat(bestDistance, equalTo(null))
    }

    @Test
    fun shouldReturnShortestExerciseTime() {
        val shortestTime = bestTimeForCardioExercise(
            UserPreferencesUiState(displayShortestTime = true),
            listOf(
                CardioExerciseHistoryUiState(minutes = 10, seconds = 0),
                CardioExerciseHistoryUiState(minutes = 1, seconds = 0)
            )
        )

        assertThat(shortestTime, equalTo(60))
    }

    @Test
    fun shouldReturnNullWhenNoTimeHistory() {
        val shortestTime = bestTimeForCardioExercise(
            UserPreferencesUiState(displayShortestTime = true),
            listOf(CardioExerciseHistoryUiState())
        )

        assertThat(shortestTime, equalTo(null))
    }

    @Test
    fun shouldReturnLongestExerciseTime() {
        val shortestTime = bestTimeForCardioExercise(
            UserPreferencesUiState(displayShortestTime = false),
            listOf(
                CardioExerciseHistoryUiState(minutes = 10, seconds = 0),
                CardioExerciseHistoryUiState(minutes = 1, seconds = 0)
            )
        )

        assertThat(shortestTime, equalTo(600))
    }

    @Test
    fun shouldReturnNullWhenNoTimeHistoryForLongestTime() {
        val shortestTime = bestTimeForCardioExercise(
            UserPreferencesUiState(displayShortestTime = false),
            listOf(CardioExerciseHistoryUiState())
        )

        assertThat(shortestTime, equalTo(null))
    }

    @Test
    fun shouldReturnMostCalories() {
        val mostCalories = mostCaloriesForCardioExercise(
            listOf(
                CardioExerciseHistoryUiState(calories = 100),
                CardioExerciseHistoryUiState(calories = 50)
            )
        )

        assertThat(mostCalories, equalTo(100))
    }

    @Test
    fun shouldReturnNullWhenNoCaloriesHistory() {
        val mostCalories = mostCaloriesForCardioExercise(
            listOf(CardioExerciseHistoryUiState())
        )

        assertThat(mostCalories, equalTo(null))
    }
}
