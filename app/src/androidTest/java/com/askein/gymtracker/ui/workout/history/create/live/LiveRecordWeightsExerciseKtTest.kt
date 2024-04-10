package com.askein.gymtracker.ui.workout.history.create.live

import androidx.activity.ComponentActivity
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import com.askein.gymtracker.enums.WeightUnits
import com.askein.gymtracker.ui.exercise.ExerciseUiState
import com.askein.gymtracker.ui.exercise.history.state.WeightsExerciseHistoryUiState
import com.askein.gymtracker.ui.user.LocalUserPreferences
import com.askein.gymtracker.ui.user.UserPreferencesUiState
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.notNullValue
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Rule
import org.junit.Test

class LiveRecordWeightsExerciseKtTest {

    @get:Rule
    val rule = createAndroidComposeRule<ComponentActivity>()

    private val timerStopButton = rule.onNode(hasText("Stop"))
    private val finishSetButton = rule.onNode(hasText("Finish Set"))
    private val finishExerciseButton = rule.onNode(hasText("Finish Exercise"))
    private val repsField = rule.onNode(hasContentDescription("Reps"))
    private val restField = rule.onNode(hasContentDescription("Rest"))
    private val weightField = rule.onNode(hasContentDescription("Weight"))
    private val weightKilogramsChosen = rule.onNode(hasText("kg"))
    private val weightPoundsChosen = rule.onNode(hasText("lb"))
    private val startButton = rule.onNode(hasText("Start"))
    private val cancelButton = rule.onNode(hasText("Cancel"))

    @Test
    fun rendersTimerThatCountsDown() {
        rule.setContent {
            Timer(timerState = 5) { }
        }

        timerStopButton.assertExists()
        rule.onNode(hasText("00:05")).assertExists()
    }

    @Test
    fun timerClickingStopFinishesTimer() {
        var finished = false
        rule.setContent {
            Timer(timerState = 15) {
                finished = true
            }
        }

        timerStopButton.assertExists()
        rule.onNode(hasText("00:15")).assertExists()

        timerStopButton.performClick()

        assertThat(finished, equalTo(true))
    }

    @Test
    fun rendersLiveRecordExerciseSetsAndTimer() {
        rule.setContent {
            LiveRecordExerciseSetsAndTimer(
                exerciseData = WeightsExerciseHistoryUiState(rest = 15),
                timerState = TimerState(),
                timerFinishedState = false,
                timerStart = { },
                finishSet = { },
                resetTimer = { },
                exerciseFinished = { }
            )
        }

        rule.onNode(hasText("Sets Completed: 0")).assertExists()
        finishSetButton.assertExists()
        finishExerciseButton.assertExists()
    }

    @Test
    fun liveRecordExerciseSetsAndTimerFinishingSetStartsTimer() {
        rule.setContent {
            var sets by remember { mutableIntStateOf(0) }
            LiveRecordExerciseSetsAndTimer(
                exerciseData = WeightsExerciseHistoryUiState(rest = 15, sets = sets),
                timerState = TimerState(currentTime = 15),
                timerFinishedState = false,
                timerStart = { },
                finishSet = { sets += 1 },
                resetTimer = { },
                exerciseFinished = { }
            )
        }

        rule.onNode(hasText("Sets Completed: 0")).assertExists()
        finishSetButton.assertExists()
        finishExerciseButton.assertExists()

        finishSetButton.performClick()

        rule.onNode(hasText("Sets Completed: 1")).assertExists()
        rule.onNode(hasText("Sets Completed: 0")).assertDoesNotExist()
        finishSetButton.assertDoesNotExist()
        rule.onNode(hasText("00:15")).assertExists()
    }

    @Test
    fun liveRecordExerciseSetsAndTimerReturnsCompletedSets() {
        rule.setContent {
            var sets by remember { mutableIntStateOf(0) }
            LiveRecordExerciseSetsAndTimer(
                exerciseData = WeightsExerciseHistoryUiState(rest = 15, sets = sets),
                timerState = TimerState(),
                timerFinishedState = false,
                timerStart = { },
                finishSet = { sets += 1 },
                resetTimer = { },
                exerciseFinished = { }
            )
        }

        rule.onNode(hasText("Sets Completed: 0")).assertExists()
        finishSetButton.assertExists()
        finishExerciseButton.assertExists()

        finishSetButton.performClick()
        timerStopButton.performClick()
        finishSetButton.performClick()
        timerStopButton.performClick()
        finishSetButton.performClick()
        finishExerciseButton.performClick()

        rule.onNode(hasText("Sets Completed: 0")).assertDoesNotExist()
        rule.onNode(hasText("Sets Completed: 3")).assertExists()
    }

    @Test
    fun rendersLiveRecordExerciseInfo() {
        rule.setContent {
            val userPreferencesUiState = UserPreferencesUiState()
            CompositionLocalProvider(LocalUserPreferences provides userPreferencesUiState) {
                LiveRecordWeightsExerciseInfo(
                    onStart = { },
                    onCancel = {}
                )
            }
        }

        repsField.assertExists()
        restField.assertExists()
        weightField.assertExists()
        startButton.assertExists()
        cancelButton.assertExists()
    }

    @Test
    fun liveRecordExerciseInfoClickingCancelCallsOnCancel() {
        var cancelled = false
        rule.setContent {
            val userPreferencesUiState = UserPreferencesUiState()
            CompositionLocalProvider(LocalUserPreferences provides userPreferencesUiState) {
                LiveRecordWeightsExerciseInfo(
                    onStart = { },
                    onCancel = { cancelled = true }
                )
            }
        }

        cancelButton.performClick()

        assertThat(cancelled, equalTo(true))
    }

    @Test
    fun liveRecordExerciseInfoClickingStartPopulatesExerciseData() {
        var exerciseData: ExerciseData? = null
        rule.setContent {
            val userPreferencesUiState = UserPreferencesUiState()
            CompositionLocalProvider(LocalUserPreferences provides userPreferencesUiState) {
                LiveRecordWeightsExerciseInfo(
                    onStart = { data -> exerciseData = data },
                    onCancel = { }
                )
            }
        }

        repsField.performClick()
        repsField.performTextInput("5")
        restField.performClick()
        restField.performTextInput("15")
        weightField.performClick()
        weightField.performTextInput("13.0")
        startButton.performClick()

        assertThat(exerciseData!!.reps, equalTo(5))
        assertThat(exerciseData!!.rest, equalTo(15))
        assertThat(exerciseData!!.weight, equalTo(13.0))
    }

    @Test
    fun rendersLiveRecordExerciseInfoAndClickingFinishExerciseReturnsHistory() {
        var exerciseHistory: WeightsExerciseHistoryUiState? = null
        rule.setContent {
            val userPreferencesUiState = UserPreferencesUiState()
            CompositionLocalProvider(LocalUserPreferences provides userPreferencesUiState) {
                LiveRecordWeightsExercise(
                    uiState = ExerciseUiState(name = "Curls"),
                    exerciseComplete = { history -> exerciseHistory = history },
                    exerciseCancel = { }
                )
            }
        }

        rule.onNode(hasText("Curls")).assertExists()

        repsField.performClick()
        repsField.performTextInput("5")
        restField.performClick()
        restField.performTextInput("15")
        weightField.performClick()
        weightField.performTextInput("13.0")
        startButton.performClick()

        finishSetButton.performClick()
        timerStopButton.performClick()
        finishSetButton.performClick()
        timerStopButton.performClick()
        finishSetButton.performClick()

        finishExerciseButton.performClick()

        assertThat(exerciseHistory, notNullValue())
        assertThat(exerciseHistory!!.sets, equalTo(3))
        assertThat(exerciseHistory!!.reps, equalTo(5))
        assertThat(exerciseHistory!!.rest, equalTo(15))
        assertThat(exerciseHistory!!.weight, equalTo(13.0))
    }

    @Test
    fun rendersLiveRecordExerciseInfoWithKilogramsChosen() {
        rule.setContent {
            val userPreferencesUiState = UserPreferencesUiState(
                defaultWeightUnit = WeightUnits.KILOGRAMS
            )
            CompositionLocalProvider(LocalUserPreferences provides userPreferencesUiState) {
                LiveRecordWeightsExerciseInfo(
                    onStart = { },
                    onCancel = {}
                )
            }
        }

        weightKilogramsChosen.assertExists()
    }

    @Test
    fun rendersLiveRecordExerciseInfoWithPoundsChosen() {
        rule.setContent {
            val userPreferencesUiState = UserPreferencesUiState(
                defaultWeightUnit = WeightUnits.POUNDS
            )
            CompositionLocalProvider(LocalUserPreferences provides userPreferencesUiState) {
                LiveRecordWeightsExerciseInfo(
                    onStart = { },
                    onCancel = {}
                )
            }
        }

        weightPoundsChosen.assertExists()
    }
}
