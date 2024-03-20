package com.askein.gymtracker.ui.workout.create

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import com.askein.gymtracker.ui.workout.WorkoutUiState
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.notNullValue
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Rule
import org.junit.Test

class CreateWorkoutScreenKtTest {

    @get:Rule
    val rule = createAndroidComposeRule<ComponentActivity>()

    private val title = rule.onNode(hasText("Create Workout"))
    private val nameField = rule.onNode(hasContentDescription("Workout Name"))
    private val saveButton = rule.onNode(hasText("Save"))
    private val closeButton = rule.onNode(hasContentDescription("Close"))

    @Test
    fun rendersEmptyCreateForm() {
        rule.setContent {
            CreateWorkoutForm(
                saveFunction = { },
                onDismiss = { },
                screenTitle = "Create Workout",
            )
        }

        title.assertExists()
        nameField.assertExists()
        saveButton.assertExists()
        closeButton.assertExists()
    }

    @Test
    fun clickCloseButtonToDismiss() {
        var dismissed = false
        rule.setContent {
            CreateWorkoutForm(
                saveFunction = { },
                onDismiss = { dismissed = true },
                screenTitle = "Create Workout",
            )
        }

        closeButton.performClick()

        assertThat(dismissed, equalTo(true))
    }

    @Test
    fun doesNotEnableSaveButtonWithEmptyWorkoutName() {
        rule.setContent {
            CreateWorkoutForm(
                saveFunction = { },
                onDismiss = { },
                screenTitle = "Create Workout",
            )
        }

        saveButton.assertIsNotEnabled()
    }

    @Test
    fun enteringWorkoutNameClickingSaveButtonCallsSaveFunctionAndOnDismiss() {
        var saved: WorkoutUiState? = null
        var dismissed = false
        rule.setContent {
            CreateWorkoutForm(
                saveFunction = { workout -> saved = workout },
                onDismiss = { dismissed = true },
                screenTitle = "Create Workout",
            )
        }

        nameField.performTextInput("Test Name")

        saveButton.assertIsEnabled()
        saveButton.performClick()

        assertThat(saved!!.name, equalTo("Test Name"))
        assertThat(dismissed, equalTo(true))
    }

    @Test
    fun createWorkoutFormWithExistingWorkoutFillsInFormField() {
        rule.setContent {
            CreateWorkoutForm(
                saveFunction = { },
                onDismiss = { },
                screenTitle = "Create Workout",
                workout = WorkoutUiState(
                    name = "Existing workout"
                )
            )
        }

        nameField.assertTextContains("Existing workout")
    }

    @Test
    fun createWorkoutFormWithExistingWorkoutSavesWithExistingId() {
        var savedWorkout: WorkoutUiState? = null
        var dismissed = false
        rule.setContent {
            CreateWorkoutForm(
                saveFunction = { workout -> savedWorkout = workout },
                onDismiss = { dismissed = true },
                screenTitle = "Create Workout",
                workout = WorkoutUiState(
                    workoutId = 2,
                    name = "Existing workout"
                )
            )
        }

        nameField.performTextInput("Updated ")

        saveButton.performClick()

        assertThat(savedWorkout, notNullValue())
        assertThat(savedWorkout!!.workoutId, equalTo(2))
        assertThat(savedWorkout!!.name, equalTo("Updated Existing workout"))
        assertThat(dismissed, equalTo(true))
    }
}
