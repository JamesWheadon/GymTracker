package com.example.gymtracker.ui.exercise.create

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import com.example.gymtracker.data.exercise.Exercise
import com.example.gymtracker.ui.exercise.toExerciseUiState
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Rule
import org.junit.Test

class CreateExerciseScreenKtTest {

    @get:Rule
    val rule = createAndroidComposeRule<ComponentActivity>()

    private val createButton = rule.onNode(hasText("Create"))
    private val nameField = rule.onNode(hasContentDescription("Exercise Name"))
    private val equipmentField = rule.onNode(hasContentDescription("Equipment"))
    private val muscleField = rule.onNode(hasContentDescription("Muscle Group"))
    private val closeButton = rule.onNode(hasContentDescription("Close"))

    private val exercise = Exercise(0, "Curls", "Biceps", "Dumbbells")

    @Test
    fun rendersEmptyCreateForm() {
        rule.setContent {
            ExerciseInformationForm(
                formTitle = "Create New Exercise",
                onDismiss = {},
                createFunction = {},
                buttonText = "Create"
            )
        }

        createButton.assertExists()
        nameField.assertExists()
        equipmentField.assertExists()
        muscleField.assertExists()
        closeButton.assertExists()
    }
    @Test
    fun clickCloseButtonToDismiss() {
        var dismissed = false
        rule.setContent {
            ExerciseInformationForm(
                formTitle = "Create New Exercise",
                onDismiss = { dismissed = true },
                createFunction = {},
                buttonText = "Create"
            )
        }

        closeButton.performClick()

        assertThat(dismissed, equalTo(true))
    }

    @Test
    fun doesNotSaveAndDismissWithEmptyExerciseName() {
        var created: Exercise? = null
        var dismissed = false
        rule.setContent {
            ExerciseInformationForm(
                formTitle = "Create New Exercise",
                onDismiss = { dismissed = true },
                createFunction = { created = it },
                buttonText = "Create"
            )
        }

        equipmentField.performTextInput(exercise.equipment)
        muscleField.performTextInput(exercise.muscleGroup)

        createButton.performClick()

        assertThat(created, equalTo(null))
        assertThat(dismissed, equalTo(false))
    }

    @Test
    fun doesNotSaveAndDismissWithEmptyEquipment() {
        var created: Exercise? = null
        var dismissed = false
        rule.setContent {
            ExerciseInformationForm(
                formTitle = "Create New Exercise",
                onDismiss = { dismissed = true },
                createFunction = { created = it },
                buttonText = "Create"
            )
        }

        nameField.performTextInput(exercise.name)
        muscleField.performTextInput(exercise.muscleGroup)

        createButton.performClick()

        assertThat(created, equalTo(null))
        assertThat(dismissed, equalTo(false))
    }

    @Test
    fun doesNotSaveAndDismissWithEmptyMuscleGroup() {
        var created: Exercise? = null
        var dismissed = false
        rule.setContent {
            ExerciseInformationForm(
                formTitle = "Create New Exercise",
                onDismiss = { dismissed = true },
                createFunction = { created = it },
                buttonText = "Create"
            )
        }

        nameField.performTextInput(exercise.name)
        equipmentField.performTextInput(exercise.equipment)

        createButton.performClick()

        assertThat(created, equalTo(null))
        assertThat(dismissed, equalTo(false))
    }

    @Test
    fun savesAndDismissCreateScreenWithAllFieldsFull() {
        var created: Exercise? = null
        var dismissed = false
        rule.setContent {
            ExerciseInformationForm(
                formTitle = "Create New Exercise",
                onDismiss = { dismissed = true },
                createFunction = { created = it },
                buttonText = "Create"
            )
        }

        nameField.performTextInput(exercise.name)
        equipmentField.performTextInput(exercise.equipment)
        muscleField.performTextInput(exercise.muscleGroup)

        createButton.performClick()

        assertThat(created, equalTo(exercise))
        assertThat(dismissed, equalTo(true))
    }

    @Test
    fun fillsInFieldsWithInformationOfExercisePassedIn() {
        rule.setContent {
            ExerciseInformationForm(
                formTitle = "Create New Exercise",
                onDismiss = { },
                createFunction = { },
                buttonText = "Create",
                exercise = exercise.toExerciseUiState()
            )
        }

        nameField.assertTextContains(exercise.name)
        equipmentField.assertTextContains(exercise.equipment)
        muscleField.assertTextContains(exercise.muscleGroup)
    }
}