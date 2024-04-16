package com.askein.gymtracker.ui.workout.history.create.live

import com.askein.gymtracker.rules.TestCoroutineRule
import com.askein.gymtracker.ui.exercise.history.state.WeightsExerciseHistoryUiState
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Rule
import org.junit.Test

class LiveRecordWeightsExerciseViewModelTest {

    @get:Rule
    val coroutineTestRule = TestCoroutineRule()

    @Test
    fun shouldUpdateExerciseData() = runTest {
        val viewModel = LiveRecordWeightsExerciseViewModel()

        assertThat(viewModel.exerciseState.value, equalTo(WeightsExerciseHistoryUiState()))

        viewModel.setExerciseData(1, 10, 15, 10.0)

        assertThat(viewModel.exerciseState.value.exerciseId, equalTo(1))
        assertThat(viewModel.exerciseState.value.reps, equalTo(10))
        assertThat(viewModel.exerciseState.value.rest, equalTo(15))
        assertThat(viewModel.exerciseState.value.weight, equalTo(10.0))
    }

    @Test
    fun shouldUpdateExerciseDataSets() = runTest {
        val viewModel = LiveRecordWeightsExerciseViewModel()

        assertThat(viewModel.exerciseState.value.sets, equalTo(0))

        viewModel.finishSet()

        assertThat(viewModel.exerciseState.value.sets, equalTo(1))
    }

    @Test
    fun shouldStartTimer() = runTest {
        val viewModel = LiveRecordWeightsExerciseViewModel()

        assertThat(viewModel.timerState.value, equalTo(TimerState()))
        assertThat(viewModel.completed.value, equalTo(false))

        viewModel.startTimer(3)

        assertThat(viewModel.timerState.value.timerRunning, equalTo(true))
        assertThat(viewModel.timerState.value.currentTime, equalTo(2))

        delay(4000)

        assertThat(viewModel.timerState.value.timerRunning, equalTo(false))
        assertThat(viewModel.completed.value, equalTo(true))
    }

    @Test
    fun shouldResetTimer() = runTest {
        val viewModel = LiveRecordWeightsExerciseViewModel()

        assertThat(viewModel.completed.value, equalTo(false))

        viewModel.startTimer(3)

        delay(4000)

        assertThat(viewModel.completed.value, equalTo(true))

        viewModel.reset()

        assertThat(viewModel.completed.value, equalTo(false))
    }

    @Test
    fun shouldResetTimerWhileRunning() = runTest {
        val viewModel = LiveRecordWeightsExerciseViewModel()

        viewModel.startTimer(3)

        val currentTime = viewModel.timerState.value.currentTime
        assertThat(viewModel.timerState.value.timerRunning, equalTo(true))

        viewModel.reset()

        assertThat(viewModel.timerState.value.timerRunning, equalTo(false))

        delay(2000)

        assertThat(viewModel.timerState.value.currentTime, equalTo(currentTime))
    }
}
