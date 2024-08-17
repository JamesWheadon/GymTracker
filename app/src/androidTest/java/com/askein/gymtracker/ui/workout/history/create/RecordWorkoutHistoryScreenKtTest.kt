package com.askein.gymtracker.ui.workout.history.create

import androidx.activity.ComponentActivity
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.hasAnySibling
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.hasSetTextAction
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextClearance
import androidx.compose.ui.test.performTextInput
import com.askein.gymtracker.ui.exercise.ExerciseUiState
import com.askein.gymtracker.ui.user.LocalUserPreferences
import com.askein.gymtracker.ui.user.UserPreferencesUiState
import com.askein.gymtracker.ui.workout.details.WorkoutWithExercisesUiState
import com.askein.gymtracker.ui.workout.history.WorkoutHistoryWithExercisesUiState
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Rule
import org.junit.Test


class RecordWorkoutHistoryScreenKtTest {

    @get:Rule
    val rule = createAndroidComposeRule<ComponentActivity>()

    private val curlsExercise = ExerciseUiState(1, "Curls", "Biceps", "Dumbbells")
    private val dipsExercise = ExerciseUiState(2, "Dips", "Triceps", "Bars")
    private val treadmillExercise = ExerciseUiState(3, "Treadmill")
    private val workoutWithExercises =
        WorkoutWithExercisesUiState(
            1,
            "Arms",
            listOf(curlsExercise, dipsExercise, treadmillExercise)
        )
    private val curlsExerciseTitle = rule.onNode(hasText("Curls"))
    private val dipsExerciseTitle = rule.onNode(hasText("Dips"))
    private val treadmillExerciseTitle = rule.onNode(hasText("Treadmill"))
    private val recordTitle = rule.onNode(hasText("Record Workout"))
    private val curlsCheckbox =
        rule.onNode(hasClickAction() and hasSetTextAction().not() and hasAnySibling(hasText("Curls")))
    private val setsField = rule.onNode(hasContentDescription("Sets"))
    private val repsField = rule.onNode(hasContentDescription("Reps"))
    private val weightField = rule.onNode(hasContentDescription("Weight"))
    private val closeButton = rule.onNode(hasContentDescription("Close"))
    private val saveButton = rule.onNode(hasText("Save"))

    @Test
    fun rendersRecordWorkoutHistoryScreen() {
        rule.setContent {
            val userPreferencesUiState = UserPreferencesUiState()
            CompositionLocalProvider(LocalUserPreferences provides userPreferencesUiState) {
                RecordWorkoutHistoryScreen(
                    uiState = workoutWithExercises,
                    onDismiss = { }
                )
            }
        }

        recordTitle.assertExists()
        curlsExerciseTitle.assertExists()
        dipsExerciseTitle.assertExists()
        treadmillExerciseTitle.assertExists()
        closeButton.assertExists()
        saveButton.assertExists()
        rule.onAllNodes(hasClickAction()).assertCountEquals(6)
    }

    @Test
    fun recordWorkoutScreenButtonNotEnabledWhenErrorsInForm() {
        rule.setContent {
            val userPreferencesUiState = UserPreferencesUiState()
            CompositionLocalProvider(LocalUserPreferences provides userPreferencesUiState) {
                RecordWorkoutHistoryScreen(
                    uiState = workoutWithExercises,
                    titleText = "Record Workout",
                    workoutSaveFunction = { },
                    onDismiss = { }
                )
            }
        }

        curlsCheckbox.performClick()

        setsField.assertExists()
        repsField.assertDoesNotExist()
        weightField.assertDoesNotExist()

        saveButton.assertIsNotEnabled()
    }

    @Test
    fun recordWorkoutScreenButtonEnabledWhenNoErrorsInForm() {
        rule.setContent {
            val userPreferencesUiState = UserPreferencesUiState()
            CompositionLocalProvider(LocalUserPreferences provides userPreferencesUiState) {
                RecordWorkoutHistoryScreen(
                    uiState = workoutWithExercises,
                    titleText = "Record Workout",
                    workoutSaveFunction = { },
                    onDismiss = { }
                )
            }
        }

        curlsCheckbox.performClick()

        setsField.performTextClearance()
        setsField.performTextInput("1")
        repsField.performTextClearance()
        repsField.performTextInput("2")

        saveButton.assertIsEnabled()
    }

    @Test
    fun recordWorkoutScreenSaveButtonSavesCheckedExercises() {
        var workout = WorkoutHistoryWithExercisesUiState()
        var dismissed = false

        rule.setContent {
            val userPreferencesUiState = UserPreferencesUiState()
            CompositionLocalProvider(LocalUserPreferences provides userPreferencesUiState) {
                RecordWorkoutHistoryScreen(
                    uiState = workoutWithExercises,
                    titleText = "Record Workout",
                    workoutSaveFunction = { workout = it },
                    onDismiss = { dismissed = true }
                )
            }
        }

        curlsCheckbox.performClick()

        setsField.performTextClearance()
        setsField.performTextInput("1")
        repsField.performTextClearance()
        repsField.performTextInput("2")

        saveButton.assertIsEnabled()

        saveButton.performClick()

        assertThat(workout.exerciseHistories.size, equalTo(1))
        assertThat(dismissed, equalTo(true))
    }

    @Test
    fun recordWorkoutScreenSaveButtonDoesNotSaveWithNoExercises() {
        var saved = false
        var dismissed = false

        rule.setContent {
            val userPreferencesUiState = UserPreferencesUiState()
            CompositionLocalProvider(LocalUserPreferences provides userPreferencesUiState) {
                RecordWorkoutHistoryScreen(
                    uiState = workoutWithExercises,
                    titleText = "Record Workout",
                    workoutSaveFunction = { saved = true },
                    onDismiss = { dismissed = true }
                )
            }
        }

        saveButton.performClick()

        assertThat(saved, equalTo(false))
        assertThat(dismissed, equalTo(true))
    }

    @Test
    fun clickingXButtonCallsOnDismiss() = runBlocking {
        var dismissed = false

        rule.setContent {
            val userPreferencesUiState = UserPreferencesUiState()
            CompositionLocalProvider(LocalUserPreferences provides userPreferencesUiState) {
                RecordWorkoutHistoryScreen(
                    uiState = workoutWithExercises,
                    onDismiss = { dismissed = true }
                )
            }
        }

        closeButton.performClick()

        assertThat(dismissed, equalTo(true))
    }
}
