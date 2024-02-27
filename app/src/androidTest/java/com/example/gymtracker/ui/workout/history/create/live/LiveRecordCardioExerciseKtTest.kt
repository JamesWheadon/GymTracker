package com.example.gymtracker.ui.workout.history.create.live

import androidx.activity.ComponentActivity
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import com.example.gymtracker.converters.DistanceUnits
import com.example.gymtracker.ui.exercise.ExerciseUiState
import com.example.gymtracker.ui.exercise.history.state.CardioExerciseHistoryUiState
import com.example.gymtracker.ui.user.LocalUserPreferences
import com.example.gymtracker.ui.user.UserPreferencesUiState
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Rule
import org.junit.Test

class LiveRecordCardioExerciseKtTest {

    @get:Rule
    val rule = createAndroidComposeRule<ComponentActivity>()

    private val exercise = ExerciseUiState(name = "Treadmill")

    private val minutesField = rule.onNode(hasContentDescription("Minutes"))
    private val secondsField = rule.onNode(hasContentDescription("Seconds"))
    private val distanceField = rule.onNode(hasContentDescription("Distance"))
    private val unitsField = rule.onNode(hasContentDescription("Units"))
    private val caloriesField = rule.onNode(hasContentDescription("Calories"))
    private val distanceKilometersChosen = rule.onNode(hasText("km"))
    private val distanceMilesChosen = rule.onNode(hasText("mi"))
    private val distanceMetersChosen = rule.onNode(hasText("m"))
    private val saveButton = rule.onNode(hasText("Save"))
    private val cancelButton = rule.onNode(hasText("Cancel"))

    @Test
    fun rendersLiveRecordCardioExerciseScreen() {
        rule.setContent {
            val userPreferencesUiState = UserPreferencesUiState()
            CompositionLocalProvider(LocalUserPreferences provides userPreferencesUiState) {
                LiveRecordCardioExercise(
                    uiState = exercise,
                    exerciseComplete = { },
                    exerciseCancel = { }
                )
            }
        }

        minutesField.assertExists()
        secondsField.assertExists()
        distanceField.assertExists()
        unitsField.assertExists()
        caloriesField.assertExists()
        saveButton.assertExists()
        cancelButton.assertExists()
    }

    @Test
    fun liveRecordCardioExerciseScreenSaveButtonDisabledWithEmptyFields() {
        rule.setContent {
            val userPreferencesUiState = UserPreferencesUiState()
            CompositionLocalProvider(LocalUserPreferences provides userPreferencesUiState) {
                LiveRecordCardioExercise(
                    uiState = exercise,
                    exerciseComplete = { },
                    exerciseCancel = { }
                )
            }
        }

        saveButton.assertIsNotEnabled()
    }

    @Test
    fun liveRecordCardioExerciseScreenPopulatingDistanceAndClickingSaveCallsCompleteFunction() {
        var history: CardioExerciseHistoryUiState? = null
        rule.setContent {
            val userPreferencesUiState = UserPreferencesUiState()
            CompositionLocalProvider(LocalUserPreferences provides userPreferencesUiState) {
                LiveRecordCardioExercise(
                    uiState = exercise,
                    exerciseComplete = { history = it },
                    exerciseCancel = { }
                )
            }
        }

        distanceField.performTextInput("300")
        saveButton.performClick()

        assertThat(history!!.distance, equalTo(300.0))
    }

    @Test
    fun liveRecordCardioExerciseScreenPopulatingCaloriesAndClickingSaveCallsCompleteFunction() {
        var history: CardioExerciseHistoryUiState? = null
        rule.setContent {
            val userPreferencesUiState = UserPreferencesUiState()
            CompositionLocalProvider(LocalUserPreferences provides userPreferencesUiState) {
                LiveRecordCardioExercise(
                    uiState = exercise,
                    exerciseComplete = { history = it },
                    exerciseCancel = { }
                )
            }
        }

        caloriesField.performTextInput("300")
        saveButton.performClick()

        assertThat(history!!.calories, equalTo(300))
    }

    @Test
    fun liveRecordCardioExerciseScreenPopulatingTimeAndClickingSaveCallsCompleteFunction() {
        var history: CardioExerciseHistoryUiState? = null
        rule.setContent {
            val userPreferencesUiState = UserPreferencesUiState()
            CompositionLocalProvider(LocalUserPreferences provides userPreferencesUiState) {
                LiveRecordCardioExercise(
                    uiState = exercise,
                    exerciseComplete = { history = it },
                    exerciseCancel = { }
                )
            }
        }

        minutesField.performTextInput("60")
        saveButton.assertIsNotEnabled()
        secondsField.performTextInput("0")
        saveButton.performClick()

        assertThat(history!!.minutes, equalTo(60))
        assertThat(history!!.seconds, equalTo(0))
    }

    @Test
    fun liveRecordCardioExerciseScreenClickingCancelCallsCancelFunction() {
        var cancelled = false
        rule.setContent {
            val userPreferencesUiState = UserPreferencesUiState()
            CompositionLocalProvider(LocalUserPreferences provides userPreferencesUiState) {
                LiveRecordCardioExercise(
                    uiState = exercise,
                    exerciseComplete = { },
                    exerciseCancel = { cancelled = true }
                )
            }
        }

        cancelButton.performClick()

        assertThat(cancelled, equalTo(true))
    }

    @Test
    fun rendersLiveRecordCardioExerciseScreenWithKilometersUnit() {
        rule.setContent {
            val userPreferencesUiState = UserPreferencesUiState(
                defaultDistanceUnit = DistanceUnits.KILOMETERS
            )
            CompositionLocalProvider(LocalUserPreferences provides userPreferencesUiState) {
                LiveRecordCardioExercise(
                    uiState = exercise,
                    exerciseComplete = { },
                    exerciseCancel = { }
                )
            }
        }

        distanceKilometersChosen.assertExists()
    }

    @Test
    fun rendersLiveRecordCardioExerciseScreenWithMilesUnit() {
        rule.setContent {
            val userPreferencesUiState = UserPreferencesUiState(
                defaultDistanceUnit = DistanceUnits.MILES
            )
            CompositionLocalProvider(LocalUserPreferences provides userPreferencesUiState) {
                LiveRecordCardioExercise(
                    uiState = exercise,
                    exerciseComplete = { },
                    exerciseCancel = { }
                )
            }
        }

        distanceMilesChosen.assertExists()
    }

    @Test
    fun rendersLiveRecordCardioExerciseScreenWithMetersUnit() {
        rule.setContent {
            val userPreferencesUiState = UserPreferencesUiState(
                defaultDistanceUnit = DistanceUnits.METERS
            )
            CompositionLocalProvider(LocalUserPreferences provides userPreferencesUiState) {
                LiveRecordCardioExercise(
                    uiState = exercise,
                    exerciseComplete = { },
                    exerciseCancel = { }
                )
            }
        }

        distanceMetersChosen.assertExists()
    }
}