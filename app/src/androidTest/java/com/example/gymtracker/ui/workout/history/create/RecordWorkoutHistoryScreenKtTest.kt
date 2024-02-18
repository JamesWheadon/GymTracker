package com.example.gymtracker.ui.workout.history.create

import androidx.activity.ComponentActivity
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.hasAnySibling
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.hasSetTextAction
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextClearance
import androidx.compose.ui.test.performTextInput
import com.example.gymtracker.ui.exercise.ExerciseUiState
import com.example.gymtracker.ui.exercise.history.state.CardioExerciseHistoryUiState
import com.example.gymtracker.ui.exercise.history.state.WeightsExerciseHistoryUiState
import com.example.gymtracker.ui.workout.details.WorkoutWithExercisesUiState
import com.example.gymtracker.ui.workout.history.WorkoutHistoryWithExercisesUiState
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.containsString
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.not
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
    private val integerErrorText = "Must be a positive number"
    private val decimalErrorText = "Must be a number"

    private val curlsExerciseTitle = rule.onNode(hasText("Curls"))
    private val dipsExerciseTitle = rule.onNode(hasText("Dips"))
    private val treadmillExerciseTitle = rule.onNode(hasText("Treadmill"))
    private val recordTitle = rule.onNode(hasText("Record Workout"))
    private val curlsCheckbox =
        rule.onNode(hasClickAction() and hasSetTextAction().not() and hasAnySibling(hasText("Curls")))
    private val treadmillCheckbox =
        rule.onNode(hasClickAction() and hasSetTextAction().not() and hasAnySibling(hasText("Treadmill")))
    private val setsField = rule.onNode(hasContentDescription("Sets"))
    private val repsField = rule.onNode(hasContentDescription("Reps"))
    private val weightField = rule.onNode(hasContentDescription("Weight"))
    private val minutestField = rule.onNode(hasContentDescription("Minutes"))
    private val secondsField = rule.onNode(hasContentDescription("Seconds"))
    private val distanceField = rule.onNode(hasContentDescription("Distance"))
    private val caloriesField = rule.onNode(hasContentDescription("Calories"))
    private val unitField = rule.onNode(hasContentDescription("Units"))
    private val closeButton = rule.onNode(hasContentDescription("Close"))
    private val saveButton = rule.onNode(hasText("Save"))
    private val cardioError = rule.onNode(hasText("Must have a time, distance or calories entered"))

    @Test
    fun rendersRecordWeightsExerciseCard() {
        rule.setContent {
            RecordWeightsExerciseCard(
                exercise = curlsExercise,
                selectExerciseFunction = { },
                deselectExerciseFunction = { },
                errorStateChange = { _, _ -> },
                exerciseHistory = null
            )
        }

        curlsExerciseTitle.assertExists()
        curlsCheckbox.assertExists()
        setsField.assertDoesNotExist()
        repsField.assertDoesNotExist()
        weightField.assertDoesNotExist()
        unitField.assertDoesNotExist()
    }

    @Test
    fun recordWeightsExerciseCardClickingCheckboxRendersFormFields() {
        var selected = false
        var deselected = false
        rule.setContent {
            var exerciseHistory: WeightsExerciseHistoryUiState? by remember { mutableStateOf(null) }
            RecordWeightsExerciseCard(
                exercise = curlsExercise,
                selectExerciseFunction = {
                    selected = true
                    exerciseHistory = WeightsExerciseHistoryUiState()
                },
                deselectExerciseFunction = {
                    deselected = true
                },
                errorStateChange = { _, _ -> },
                exerciseHistory = exerciseHistory
            )
        }

        curlsCheckbox.performClick()

        curlsExerciseTitle.assertExists()
        setsField.assertExists()
        repsField.assertExists()
        weightField.assertExists()
        unitField.assertExists()
        assertThat(selected, equalTo(true))
        assertThat(deselected, equalTo(false))

        curlsCheckbox.performClick()

        assertThat(deselected, equalTo(true))
    }

    @Test
    fun recordWeightsExerciseCardClickingCheckboxRendersFormFieldsWithErrors() {
        var error = false
        var id = -1

        rule.setContent {
            RecordWeightsExerciseCard(
                exercise = curlsExercise,
                selectExerciseFunction = { },
                deselectExerciseFunction = { },
                errorStateChange = { exerciseId, exerciseError ->
                    error = exerciseError
                    id = exerciseId
                },
                exerciseHistory = WeightsExerciseHistoryUiState()
            )
        }
        curlsCheckbox.performClick()
        weightField.performTextClearance()

        setsField.assertTextContains(integerErrorText)
        repsField.assertTextContains(integerErrorText)
        weightField.assertTextContains(decimalErrorText)
        assertThat(error, equalTo(true))
        assertThat(id, equalTo(1))
    }

    @Test
    fun returnsFalseErrorStateWhenNoErrorsInRecordWeightsExerciseCard() {
        var error = false
        var id = -1

        rule.setContent {
            RecordWeightsExerciseCard(
                exercise = curlsExercise,
                selectExerciseFunction = { },
                deselectExerciseFunction = { },
                errorStateChange = { exerciseId, exerciseError ->
                    error = exerciseError
                    id = exerciseId
                },
                exerciseHistory = WeightsExerciseHistoryUiState()
            )
        }
        curlsCheckbox.performClick()
        setsField.performTextClearance()
        setsField.performTextInput("1")
        repsField.performTextClearance()
        repsField.performTextInput("1")

        assertTextDoesNotContain(setsField, integerErrorText)
        assertTextDoesNotContain(repsField, integerErrorText)
        assertTextDoesNotContain(weightField, decimalErrorText)
        assertThat(error, equalTo(false))
        assertThat(id, equalTo(1))
    }

    @Test
    fun rendersRecordCardioExerciseCard() {
        rule.setContent {
            RecordCardioExerciseCard(
                exercise = treadmillExercise,
                selectExerciseFunction = { },
                deselectExerciseFunction = { },
                errorStateChange = { _, _ -> },
                exerciseHistory = null
            )
        }

        treadmillExerciseTitle.assertExists()
        treadmillCheckbox.assertExists()
        minutestField.assertDoesNotExist()
        secondsField.assertDoesNotExist()
        distanceField.assertDoesNotExist()
        caloriesField.assertDoesNotExist()
        unitField.assertDoesNotExist()
    }

    @Test
    fun recordCardioExerciseCardClickingCheckboxRendersFormFields() {
        var selected = false
        var deselected = false
        rule.setContent {
            var exerciseHistory: CardioExerciseHistoryUiState? by remember { mutableStateOf(null) }
            RecordCardioExerciseCard(
                exercise = treadmillExercise,
                selectExerciseFunction = {
                    selected = true
                    exerciseHistory = CardioExerciseHistoryUiState()
                },
                deselectExerciseFunction = {
                    deselected = true
                },
                errorStateChange = { _, _ -> },
                exerciseHistory = exerciseHistory
            )
        }

        treadmillCheckbox.performClick()

        treadmillExerciseTitle.assertExists()
        minutestField.assertExists()
        secondsField.assertExists()
        distanceField.assertExists()
        caloriesField.assertExists()
        unitField.assertExists()
        assertThat(selected, equalTo(true))
        assertThat(deselected, equalTo(false))

        treadmillCheckbox.performClick()

        assertThat(deselected, equalTo(true))
    }

    @Test
    fun recordCardioExerciseCardClickingCheckboxRendersFormFieldsWithErrors() {
        var error = false
        var id = -1

        rule.setContent {
            RecordCardioExerciseCard(
                exercise = treadmillExercise,
                selectExerciseFunction = { },
                deselectExerciseFunction = { },
                errorStateChange = { exerciseId, exerciseError ->
                    error = exerciseError
                    id = exerciseId
                },
                exerciseHistory = CardioExerciseHistoryUiState()
            )
        }
        treadmillCheckbox.performClick()
        cardioError.assertExists()

        assertThat(error, equalTo(true))
        assertThat(id, equalTo(3))
    }

    @Test
    fun returnsFalseErrorStateWhenNoErrorsInRecordCardioExerciseCard() {
        var error = false
        var id = -1
        val exerciseHistory = CardioExerciseHistoryUiState()

        rule.setContent {
            RecordCardioExerciseCard(
                exercise = treadmillExercise,
                selectExerciseFunction = { },
                deselectExerciseFunction = { },
                errorStateChange = { exerciseId, exerciseError ->
                    error = exerciseError
                    id = exerciseId
                },
                exerciseHistory = exerciseHistory
            )
        }
        treadmillCheckbox.performClick()
        distanceField.performTextInput("10.0")

        cardioError.assertDoesNotExist()
        assertThat(exerciseHistory.distance, equalTo(10.0))
        assertThat(error, equalTo(false))
        assertThat(id, equalTo(3))
    }

    private fun assertTextDoesNotContain(
        semanticsNodeInteraction: SemanticsNodeInteraction, text: String
    ) {
        val config = semanticsNodeInteraction.fetchSemanticsNode().config
        assertThat(
            config[SemanticsProperties.Text].reduce { acc, string -> acc + string }.text
                    + config[SemanticsProperties.EditableText],
            not(containsString(text))
        )
    }

    @Test
    fun rendersRecordWorkoutHistoryScreen() {
        rule.setContent {
            RecordWorkoutHistoryScreen(
                uiState = workoutWithExercises,
                onDismiss = { }
            )
        }

        recordTitle.assertExists()
        curlsExerciseTitle.assertExists()
        dipsExerciseTitle.assertExists()
        treadmillExerciseTitle.assertExists()
        closeButton.assertExists()
        saveButton.assertExists()
        rule.onAllNodes(hasClickAction()).assertCountEquals(5)
    }

    @Test
    fun recordWorkoutScreenButtonNotEnabledWhenErrorsInForm() {
        rule.setContent {
            RecordWorkoutHistoryScreen(
                uiState = workoutWithExercises,
                onDismiss = { }
            )
        }

        curlsCheckbox.performClick()

        setsField.assertExists()
        repsField.assertExists()
        weightField.assertExists()

        saveButton.assertIsNotEnabled()
    }

    @Test
    fun recordWorkoutScreenButtonEnabledWhenNoErrorsInForm() {
        rule.setContent {
            RecordWorkoutHistoryScreen(
                uiState = workoutWithExercises,
                onDismiss = { }
            )
        }

        curlsCheckbox.performClick()

        setsField.performTextClearance()
        setsField.performTextInput("1")
        repsField.performTextClearance()
        repsField.performTextInput("2")

        saveButton.assertIsEnabled()
    }

    @Test
    fun recordWorkoutScreenSaveButtonSavesCheckedExercises() = runBlocking {
        var workout = WorkoutHistoryWithExercisesUiState()
        var dismissed = false

        rule.setContent {
            RecordWorkoutHistoryScreen(
                uiState = workoutWithExercises,
                titleText = "Record Workout",
                workoutSaveFunction = { workout = it },
                onDismiss = { dismissed = true }
            )
        }

        curlsCheckbox.performClick()

        setsField.performTextClearance()
        setsField.performTextInput("1")
        repsField.performTextClearance()
        repsField.performTextInput("2")

        saveButton.assertIsEnabled()

        saveButton.performClick()

        assertThat(workout.exercises.size, equalTo(1))
        assertThat(dismissed, equalTo(true))
    }

    @Test
    fun clickingXButtonCallsOnDismiss() = runBlocking {
        var dismissed = false

        rule.setContent {
            RecordWorkoutHistoryScreen(
                uiState = workoutWithExercises,
                onDismiss = { dismissed = true }
            )
        }

        closeButton.performClick()

        assertThat(dismissed, equalTo(true))
    }
}
