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
import com.askein.gymtracker.data.exercise.ExerciseType
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
    private val weightsType = rule.onNode(hasText("Weights"))
    private val cardioType = rule.onNode(hasText("Cardio"))
    private val calisthenicsType = rule.onNode(hasText("Calisthenics"))
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
        weightsType.assertExists()
        cardioType.assertExists()
        calisthenicsType.assertExists()
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
    fun savesAndDismissesWithEmptyEquipment() {
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

        assertThat(created?.name, equalTo(exercise.name))
        assertThat(created?.muscleGroup, equalTo(exercise.muscleGroup))
        assertThat(created?.equipment, equalTo(""))
        assertThat(dismissed, equalTo(true))
    }

    @Test
    fun savesAndDismissesWithEmptyMuscleGroup() {
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

        assertThat(created?.name, equalTo(exercise.name))
        assertThat(created?.muscleGroup, equalTo(""))
        assertThat(created?.equipment, equalTo(exercise.equipment))
        assertThat(dismissed, equalTo(true))
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
    fun selectingCardioExerciseChangesAvailableInformationFields() {
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

        cardioType.performClick()

        nameField.assertExists()
        equipmentField.assertDoesNotExist()
        muscleField.assertDoesNotExist()
        createButton.assertExists()
    }

    @Test
    fun createCardioExerciseWithNameField() {
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

        cardioType.performClick()
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

        cardioType.performClick()
        createButton.performClick()

        assertThat(dismissed, equalTo(false))
        assertThat(created, equalTo(null))
    }

    @Test
    fun updateCardioExerciseWithNameField() {
        var created: ExerciseUiState? = null
        rule.setContent {
            ExerciseInformationForm(
                formTitle = createTitleResourceId,
                buttonText = createResourceId,
                savedExerciseNames = listOf(),
                savedMuscleGroups = listOf(),
                exercise = ExerciseUiState(type = ExerciseType.CARDIO, name = "Cardio"),
                onDismiss = { },
                createFunction = { newExercise -> created = newExercise }
            )
        }

        equipmentField.assertDoesNotExist()
        muscleField.assertDoesNotExist()
        nameField.assertTextContains("Cardio")

        nameField.performTextClearance()
        nameField.performTextInput("Treadmill")
        createButton.performClick()

        assertThat(created?.name, equalTo("Treadmill"))
    }

    @Test
    fun selectingCalisthenicsExerciseChangesAvailableInformationFields() {
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

        calisthenicsType.performClick()

        nameField.assertExists()
        equipmentField.assertDoesNotExist()
        muscleField.assertExists()
        createButton.assertExists()
    }

    @Test
    fun createCalisthenicsExerciseWithNameAndMuscleField() {
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

        calisthenicsType.performClick()
        nameField.performTextInput("Calisthenics")
        muscleField.performTextInput("Triceps")
        createButton.performClick()

        assertThat(dismissed, equalTo(true))
        assertThat(created!!.name, equalTo("Calisthenics"))
        assertThat(created!!.equipment, equalTo(""))
        assertThat(created!!.muscleGroup, equalTo("Triceps"))
    }

    @Test
    fun doesNotCreateCalisthenicsExerciseWithEmptyNameField() {
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

        calisthenicsType.performClick()
        createButton.performClick()

        assertThat(dismissed, equalTo(false))
        assertThat(created, equalTo(null))
    }

    @Test
    fun updateCalisthenicsExerciseWithNameAndMuscleField() {
        var created: ExerciseUiState? = null
        rule.setContent {
            ExerciseInformationForm(
                formTitle = createTitleResourceId,
                buttonText = createResourceId,
                savedExerciseNames = listOf(),
                savedMuscleGroups = listOf(),
                exercise = ExerciseUiState(type = ExerciseType.CALISTHENICS, name = "Calisthenics", muscleGroup = "Biceps"),
                onDismiss = { },
                createFunction = { newExercise -> created = newExercise }
            )
        }

        equipmentField.assertDoesNotExist()
        muscleField.assertTextContains("Biceps")
        nameField.assertTextContains("Calisthenics")

        muscleField.performTextClearance()
        muscleField.performTextInput("Triceps")
        nameField.performTextClearance()
        nameField.performTextInput("Seated Dips")
        createButton.performClick()

        assertThat(created?.name, equalTo("Seated Dips"))
        assertThat(created?.muscleGroup, equalTo("Triceps"))
    }
}
