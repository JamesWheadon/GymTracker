package com.example.gymtracker.ui.workout.create

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Rule
import org.junit.Test

class CreateWorkoutScreenKtTest {

    @get:Rule
    val rule = createAndroidComposeRule<ComponentActivity>()

    private val title = rule.onNode(hasText("Create Workout"))
    private val nameField = rule.onNode(hasContentDescription("Workout Name"))
    private val saveButton = rule.onNode(hasText("Save Workout"))
    private val closeButton = rule.onNode(hasContentDescription("Close"))

    @Test
    fun rendersEmptyCreateForm() {
        rule.setContent {
            CreateWorkoutForm(
                saveFunction = { },
                onDismiss = { },
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
            )
        }

        saveButton.assertIsNotEnabled()
    }

    @Test
    fun enteringWorkoutNameClickingSaveButtonCallsSaveFunctionAndOnDismiss() {
        var saved = false
        var dismissed = false
        rule.setContent {
            CreateWorkoutForm(
                saveFunction = { saved = true },
                onDismiss = { dismissed = true },
            )
        }

        nameField.performTextInput("Test Name")

        saveButton.assertIsEnabled()
        saveButton.performClick()
        assertThat(saved, equalTo(true))
        assertThat(dismissed, equalTo(true))
    }
}