package com.askein.gymtracker.ui.exercise.create

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextClearance
import androidx.compose.ui.test.performTextInput
import com.askein.gymtracker.R
import com.askein.gymtracker.getResourceString
import com.askein.gymtracker.ui.exercise.ExerciseUiState
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Rule
import org.junit.Test

class CreateExerciseScreenKtTest {

    @get:Rule
    val rule = createAndroidComposeRule<ComponentActivity>()

    private val createTitleResourceId = R.string.create_exercise_title
    private val createResourceId = R.string.create

    private val createButton = rule.onNode(hasText(getResourceString(createResourceId)))
    private val typeToggle = rule.onNode(hasContentDescription(getResourceString(R.string.exercise_type_toggle)))
    private val weightsType = rule.onNode(hasText("Weights"))
    private val cardioType = rule.onNode(hasText("Cardio"))
    private val nameField = rule.onNode(hasContentDescription("Exercise Name"))
    private val equipmentField = rule.onNode(hasContentDescription("Equipment"))
    private val muscleField = rule.onNode(hasContentDescription("Muscle Group"))
    private val closeButton = rule.onNode(hasContentDescription("Close"))
    private val nameError = rule.onNode(hasText("Name already taken"))

    private val exercise = ExerciseUiState(0, "Test Curls", "Biceps", "Dumbbells")
    private val savedExercise = ExerciseUiState(1, "Saved Curls", "Biceps", "Dumbbells")

    @Test
    fun rendersEmptyCreateForm() {
        rule.setContent {
            ExerciseInformationForm(
                formTitle = createTitleResourceId,
                buttonText = createResourceId,
                savedExerciseNames = listOf(),
                savedMuscleGroups = listOf(),
                exercise = ExerciseUiState(),
                onDismiss = {},
                createFunction = {}
            )
        }

        createButton.assertExists()
        typeToggle.assertExists()
        weightsType.assertExists()
        cardioType.assertExists()
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
                formTitle = createTitleResourceId,
                buttonText = createResourceId,
                savedExerciseNames = listOf(),
                savedMuscleGroups = listOf(),
                exercise = ExerciseUiState(),
                onDismiss = { dismissed = true },
                createFunction = {}
            )
        }

        closeButton.performClick()

        assertThat(dismissed, equalTo(true))
    }

    @Test
    fun doesNotSaveAndDismissWithEmptyExerciseName() {
        var created: ExerciseUiState? = null
        var dismissed = false
        rule.setContent {
            ExerciseInformationForm(
                formTitle = createTitleResourceId,
                buttonText = createResourceId,
                savedExerciseNames = listOf(),
                savedMuscleGroups = listOf(),
                exercise = ExerciseUiState(),
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
        var created: ExerciseUiState? = null
        var dismissed = false
        rule.setContent {
            ExerciseInformationForm(
                formTitle = createTitleResourceId,
                buttonText = createResourceId,
                savedExerciseNames = listOf(),
                savedMuscleGroups = listOf(),
                exercise = ExerciseUiState(),
                onDismiss = { dismissed = true },
                createFunction = { created = it }
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
        var created: ExerciseUiState? = null
        var dismissed = false
        rule.setContent {
            ExerciseInformationForm(
                formTitle = createTitleResourceId,
                buttonText = createResourceId,
                savedExerciseNames = listOf(),
                savedMuscleGroups = listOf(),
                exercise = ExerciseUiState(),
                onDismiss = { dismissed = true },
                createFunction = { created = it }
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
        var created: ExerciseUiState? = null
        var dismissed = false
        rule.setContent {
            ExerciseInformationForm(
                formTitle = createTitleResourceId,
                buttonText = createResourceId,
                savedExerciseNames = listOf(),
                savedMuscleGroups = listOf(),
                exercise = ExerciseUiState(),
                onDismiss = { dismissed = true },
                createFunction = { created = it }
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
                formTitle = createTitleResourceId,
                buttonText = createResourceId,
                savedExerciseNames = listOf(),
                savedMuscleGroups = listOf(),
                exercise = exercise,
                onDismiss = { },
                createFunction = { }
            )
        }

        typeToggle.assertDoesNotExist()
        nameField.assertTextContains(exercise.name)
        equipmentField.assertTextContains(exercise.equipment)
        muscleField.assertTextContains(exercise.muscleGroup)
    }

    @Test
    fun displaysErrorMessageWhenExerciseNameAlreadyTaken() {
        rule.setContent {
            ExerciseInformationForm(
                formTitle = createTitleResourceId,
                buttonText = createResourceId,
                savedExerciseNames = listOf(savedExercise.name),
                savedMuscleGroups = listOf(),
                exercise = exercise,
                onDismiss = { },
                createFunction = { }
            )
        }

        nameField.performTextClearance()
        nameField.performTextInput(savedExercise.name)

        nameField.assertTextContains(savedExercise.name)
        equipmentField.assertTextContains(exercise.equipment)
        muscleField.assertTextContains(exercise.muscleGroup)
        nameError.assertExists()
    }

    @Test
    fun cantSaveWithErrorMessageWhenExerciseNameAlreadyTaken() {
        var created: ExerciseUiState? = null
        var dismissed = false

        rule.setContent {
            ExerciseInformationForm(
                formTitle = createTitleResourceId,
                buttonText = createResourceId,
                savedExerciseNames = listOf(savedExercise.name),
                savedMuscleGroups = listOf(),
                exercise = exercise,
                onDismiss = { dismissed = true },
                createFunction = { newExercise -> created = newExercise }
            )
        }

        nameField.performTextClearance()
        nameField.performTextInput(savedExercise.name)

        nameField.assertTextContains(savedExercise.name)
        equipmentField.assertTextContains(exercise.equipment)
        muscleField.assertTextContains(exercise.muscleGroup)
        nameError.assertExists()

        createButton.performClick()
        assertThat(dismissed, equalTo(false))
        assertThat(created, equalTo(null))
    }

    @Test
    fun typeToggleChangesAvailableInformationFields() {
        rule.setContent {
            ExerciseInformationForm(
                formTitle = createTitleResourceId,
                buttonText = createResourceId,
                savedExerciseNames = listOf(),
                savedMuscleGroups = listOf(),
                exercise = ExerciseUiState(),
                onDismiss = { },
                createFunction = { }
            )
        }

        nameField.assertExists()
        equipmentField.assertExists()
        muscleField.assertExists()
        createButton.assertExists()

        typeToggle.performClick()

        nameField.assertExists()
        equipmentField.assertDoesNotExist()
        muscleField.assertDoesNotExist()
        createButton.assertExists()
    }

    @Test
    fun createCardioExerciseWithJustNameField() {
        var created: ExerciseUiState? = null
        var dismissed = false

        rule.setContent {
            ExerciseInformationForm(
                formTitle = createTitleResourceId,
                buttonText = createResourceId,
                savedExerciseNames = listOf(),
                savedMuscleGroups = listOf(),
                exercise = ExerciseUiState(),
                onDismiss = { dismissed = true },
                createFunction = { newExercise -> created = newExercise }
            )
        }

        typeToggle.performClick()
        nameField.performTextInput("Cardio")
        createButton.performClick()

        assertThat(dismissed, equalTo(true))
        assertThat(created!!.name, equalTo("Cardio"))
        assertThat(created!!.equipment, equalTo(""))
        assertThat(created!!.muscleGroup, equalTo(""))
    }

    @Test
    fun doesNotCreateCardioExerciseWithEmptyNameField() {
        var created: ExerciseUiState? = null
        var dismissed = false

        rule.setContent {
            ExerciseInformationForm(
                formTitle = createTitleResourceId,
                buttonText = createResourceId,
                savedExerciseNames = listOf(),
                savedMuscleGroups = listOf(),
                exercise = ExerciseUiState(),
                onDismiss = { dismissed = true },
                createFunction = { newExercise -> created = newExercise }
            )
        }

        typeToggle.performClick()
        createButton.performClick()

        assertThat(dismissed, equalTo(false))
        assertThat(created, equalTo(null))
    }

    @Test
    fun updateCardioExerciseWithNameField() {
        rule.setContent {
            ExerciseInformationForm(
                formTitle = createTitleResourceId,
                buttonText = createResourceId,
                savedExerciseNames = listOf(),
                savedMuscleGroups = listOf(),
                exercise = ExerciseUiState(name = "Cardio"),
                onDismiss = { },
                createFunction = { }
            )
        }

        typeToggle.assertDoesNotExist()
        equipmentField.assertDoesNotExist()
        muscleField.assertDoesNotExist()
        nameField.assertTextContains("Cardio")
    }
}
