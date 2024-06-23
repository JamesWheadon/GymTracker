package com.askein.gymtracker.ui.workout.history.create.live

import com.askein.gymtracker.enums.WeightUnits
import com.askein.gymtracker.rules.TestCoroutineRule
import com.askein.gymtracker.ui.exercise.history.state.WeightsExerciseHistoryUiState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
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

        viewModel.setExerciseData(1, 15)

        assertThat(viewModel.exerciseState.value.exerciseId, equalTo(1))
        assertThat(viewModel.exerciseState.value.rest, equalTo(15))
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

        val startJob = launch {
            viewModel.timerState.collect {
                assertThat(it.timerRunning, equalTo(true))
                assertThat(it.currentTime, equalTo(2))
            }
        }

        startJob.cancel()

        delay(4000)

        val finishJobTimer = launch {
            viewModel.timerState.collect {
                assertThat(it.timerRunning, equalTo(false))
            }
        }
        val finishJobCompleted = launch {
            viewModel.completed.collect {
                assertThat(it, equalTo(true))
            }
        }

        finishJobTimer.cancel()
        finishJobCompleted.cancel()
    }

    @Test
    fun shouldResetTimer() = runTest {
        val viewModel = LiveRecordWeightsExerciseViewModel()

        assertThat(viewModel.completed.value, equalTo(false))

        viewModel.startTimer(3)

        delay(4000)

        val startJob = launch {
            viewModel.completed.collect {
                assertThat(it, equalTo(true))
            }
        }

        startJob.cancel()

        viewModel.reset()

        val restJob = launch {
            viewModel.completed.collect {
                assertThat(it, equalTo(false))
            }
        }

        restJob.cancel()
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

    @Test
    fun shouldSetUnitState() {
        val viewModel = LiveRecordWeightsExerciseViewModel()

        assertThat(viewModel.unitState.value, equalTo(WeightUnits.KILOGRAMS))

        viewModel.setUnitState(WeightUnits.POUNDS)

        assertThat(viewModel.unitState.value, equalTo(WeightUnits.POUNDS))
    }

    @Test
    fun shouldAddSetInfo() {
        val viewModel = LiveRecordWeightsExerciseViewModel()

        assertThat(viewModel.exerciseState.value, equalTo(WeightsExerciseHistoryUiState()))

        viewModel.addSetInfo(5, 10.0)

        assertThat(
            viewModel.exerciseState.value,
            equalTo(WeightsExerciseHistoryUiState(reps = listOf(5), weight = listOf(10.0)))
        )
    }
}
