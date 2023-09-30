package com.example.gymtracker.ui.exercise.create

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import com.example.gymtracker.data.exercise.Exercise
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Rule
import org.junit.Test

class CreateExerciseScreenKtTest {

    @get:Rule
    val rule = createAndroidComposeRule<ComponentActivity>()

    private val createButton = rule.onNode(hasText("Create"))
    private val exerciseNameField = rule.onNode(hasContentDescription("Exercise Name"))
    private val equipmentField = rule.onNode(hasContentDescription("Equipment"))
    private val muscleField = rule.onNode(hasContentDescription("Muscle Group"))
    private val closeButton = rule.onNode(hasContentDescription("Close"))

    private val exercise = Exercise(0, "Curls", "Biceps", "Dumbbells")

    @Test
    fun rendersEmptyCreateForm() {
        rule.setContent {
            CreateExerciseScreen(
                onDismiss = {},
                createFunction = {}
            )
        }

        createButton.assertExists()
        exerciseNameField.assertExists()
        equipmentField.assertExists()
        muscleField.assertExists()
        closeButton.assertExists()
    }
    @Test
    fun clickCloseButtonToDismiss() {
        var dismissed = false
        rule.setContent {
            CreateExerciseScreen(
                onDismiss = { dismissed = true },
                createFunction = {}
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
            CreateExerciseScreen(
                onDismiss = { dismissed = true },
                createFunction = { created = it }
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
            CreateExerciseScreen(
                onDismiss = { dismissed = true },
                createFunction = { created = it }
            )
        }

        exerciseNameField.performTextInput(exercise.name)
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
            CreateExerciseScreen(
                onDismiss = { dismissed = true },
                createFunction = { created = it }
            )
        }

        exerciseNameField.performTextInput(exercise.name)
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
            CreateExerciseScreen(
                onDismiss = { dismissed = true },
                createFunction = { created = it }
            )
        }

        exerciseNameField.performTextInput(exercise.name)
        equipmentField.performTextInput(exercise.equipment)
        muscleField.performTextInput(exercise.muscleGroup)

        createButton.performClick()

        assertThat(created, equalTo(exercise))
        assertThat(dismissed, equalTo(true))
    }
}