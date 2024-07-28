package com.askein.gymtracker.ui.exercise.details

import com.askein.gymtracker.ui.exercise.history.state.WeightsExerciseHistoryUiState
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import java.time.LocalDate

class WeightsExerciseDetailsScreenKtTest {

    private val firstDate: LocalDate = LocalDate.now().minusDays(1)
    private val secondDate: LocalDate = LocalDate.now().minusDays(2)
    private val thirdDate: LocalDate = LocalDate.now().minusDays(3)
    private val fourthDate: LocalDate = LocalDate.now().minusDays(4)
    private val weightsHistory = listOf(
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
            weight = listOf(18.0, 10.0),
            sets = 2,
            seconds = listOf(30, 90),
            rest = 1,
            date = fourthDate
        )
    )

    @Test
    fun shouldGetBestRepsForExerciseForHighestWeight() {
        val bestReps = bestRepsForWeightsExercise(weightsHistory, true)

        assertThat(bestReps!!.first, equalTo(13.0))
        assertThat(bestReps.second, equalTo(2))
    }

    @Test
    fun shouldGetBestRepsForExerciseForHighestWeightOverSet() {
        val bestReps = bestRepsForWeightsExercise(weightsHistory, false)

        assertThat(bestReps!!.first, equalTo(12.0))
        assertThat(bestReps.second, equalTo(3))
    }

    @Test
    fun shouldReturnNullIfNoRepsInHistory() {
        val bestReps = bestRepsForWeightsExercise(listOf(), true)

        assertThat(bestReps, equalTo(null))
    }

    @Test
    fun shouldGetBestTimeForExerciseForHighestWeight() {
        val bestTime = bestTimeForWeightsExercise(weightsHistory, true)

        assertThat(bestTime!!.first, equalTo(18.0))
        assertThat(bestTime.second, equalTo(30))
    }

    @Test
    fun shouldGetBestTimeForExerciseForHighestWeightOverSet() {
        val bestTime = bestTimeForWeightsExercise(weightsHistory, false)

        assertThat(bestTime!!.first, equalTo(10.0))
        assertThat(bestTime.second, equalTo(90))
    }

    @Test
    fun shouldReturnNullIfNoTimeInHistory() {
        val bestTime = bestTimeForWeightsExercise(listOf(), true)

        assertThat(bestTime, equalTo(null))
    }
}
