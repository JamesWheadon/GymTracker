package com.askein.gymtracker.ui.exercise.details

import com.askein.gymtracker.ui.exercise.history.state.WeightsExerciseHistoryUiState
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test

class CalisthenicsExerciseDetailsScreenKtTest {

    @Test
    fun shouldReturnMostRepsFromHistory() {
        val mostReps =
            mostRepsForExercise(listOf(WeightsExerciseHistoryUiState(reps = listOf(5, 10))))

        assertThat(mostReps, equalTo(10))
    }

    @Test
    fun shouldReturnNullWhenNoRepsInHistory() {
        val mostReps = mostRepsForExercise(listOf())

        assertThat(mostReps, equalTo(null))
    }

    @Test
    fun shouldReturnMostSecondsFromHistory() {
        val bestTime =
            bestTimeForExercise(listOf(WeightsExerciseHistoryUiState(seconds = listOf(5, 10))))

        assertThat(bestTime, equalTo(10))
    }

    @Test
    fun shouldReturnNullWhenNoSecondsInHistory() {
        val bestTime = bestTimeForExercise(listOf())

        assertThat(bestTime, equalTo(null))
    }
}