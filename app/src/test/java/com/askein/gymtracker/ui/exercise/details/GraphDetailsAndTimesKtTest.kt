package com.askein.gymtracker.ui.exercise.details

import com.askein.gymtracker.R
import com.askein.gymtracker.ui.exercise.history.state.WeightsExerciseHistoryUiState
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import java.time.LocalDate


class GraphDetailsAndTimesKtTest {

    @Test
    fun shouldGetGraphOptionsWithRepsAndTimeOptions() {
        val weightsGraphOptions = graphOptionsForWeightsExercise(
            ExerciseDetailsUiState(
                weightsHistory = listOf(
                    WeightsExerciseHistoryUiState(reps = listOf(1), seconds = listOf(1), date = LocalDate.now().minusDays(15))
                )
            )
        )

        assertThat(
            weightsGraphOptions.detailOptions, equalTo(
                listOf(
                    R.string.max_reps,
                    R.string.max_time,
                    R.string.max_sets,
                    R.string.total_reps,
                    R.string.total_time
                )
            )
        )
        assertThat(
            weightsGraphOptions.timeOptionToStartTime, equalTo(
                mapOf(
                    R.string.seven_days to LocalDate.now().minusDays(7),
                    R.string.thirty_days to LocalDate.now().minusDays(30),
                    R.string.past_year to LocalDate.of(LocalDate.now().year, 1, 1),
                    R.string.all_time to LocalDate.now().minusDays(15)
                )
            )
        )
    }

    @Test
    fun shouldGetGraphOptionsWithoutTimeOptionsIfNoTimeExerciseHistory() {
        val weightsGraphOptions = graphOptionsForWeightsExercise(
            ExerciseDetailsUiState(
                weightsHistory = listOf(
                    WeightsExerciseHistoryUiState(reps = listOf(1), date = LocalDate.now().minusDays(15))
                )
            )
        )

        assertThat(
            weightsGraphOptions.detailOptions, equalTo(
                listOf(
                    R.string.max_reps,
                    R.string.max_sets,
                    R.string.total_reps,
                )
            )
        )
        assertThat(
            weightsGraphOptions.timeOptionToStartTime, equalTo(
                mapOf(
                    R.string.seven_days to LocalDate.now().minusDays(7),
                    R.string.thirty_days to LocalDate.now().minusDays(30),
                    R.string.past_year to LocalDate.of(LocalDate.now().year, 1, 1),
                    R.string.all_time to LocalDate.now().minusDays(15)
                )
            )
        )
    }

    @Test
    fun shouldGetGraphOptionsWithoutRepsOptionsIfNoRepsExerciseHistory() {
        val weightsGraphOptions = graphOptionsForWeightsExercise(
            ExerciseDetailsUiState(
                weightsHistory = listOf(
                    WeightsExerciseHistoryUiState(seconds = listOf(1), date = LocalDate.now().minusDays(15))
                )
            )
        )

        assertThat(
            weightsGraphOptions.detailOptions, equalTo(
                listOf(
                    R.string.max_time,
                    R.string.max_sets,
                    R.string.total_time
                )
            )
        )
        assertThat(
            weightsGraphOptions.timeOptionToStartTime, equalTo(
                mapOf(
                    R.string.seven_days to LocalDate.now().minusDays(7),
                    R.string.thirty_days to LocalDate.now().minusDays(30),
                    R.string.past_year to LocalDate.of(LocalDate.now().year, 1, 1),
                    R.string.all_time to LocalDate.now().minusDays(15)
                )
            )
        )
    }
}