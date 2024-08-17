package com.askein.gymtracker.ui.exercise.create

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextClearance
import androidx.compose.ui.test.performTextInput
import com.askein.gymtracker.R
import com.askein.gymtracker.helper.getResourceString
import com.askein.gymtracker.ui.exercise.ExerciseUiState
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Rule
import org.junit.Test

class CreateExerciseScreenKtTest {

    @get:Rule
    val rule = createAndroidComposeRule<ComponentActivity>()

    private val createResourceId = R.string.create

    private val createButton = rule.onNode(hasText(getResourceString(createResourceId)))
    private val weightsType = rule.onNode(hasText("Weights"))
    private val cardioType = rule.onNode(hasText("Cardio"))
    private val calisthenicsType = rule.onNode(hasText("Calisthenics"))
    private val nameField = rule.onNode(hasContentDescription("Exercise Name"))
    private val equipmentField = rule.onNode(hasContentDescription("Equipment"))
    private val muscleField = rule.onNode(hasContentDescription("Muscle Group"))
    private val nameError = rule.onNode(hasText("Name already taken"))

    private val exercise = ExerciseUiState(0, "Test Curls", "Biceps", "Dumbbells")
    private val savedExercise = ExerciseUiState(1, "Saved Curls", "Biceps", "Dumbbells")

    @Test
    fun rendersEmptyCreateFormForWeightsExerciseByDefault() {
        rule.setContent {
            ExerciseInformationForm(
                createExerciseInfo()
            )
        }

        createButton.assertExists()
        weightsType.assertExists()
        cardioType.assertExists()
        calisthenicsType.assertExists()
        nameField.assertExists()
        equipmentField.assertExists()
        muscleField.assertExists()
    }

    @Test
    fun selectingCardioExerciseChangesAvailableInformationFields() {
        rule.setContent {
            ExerciseInformationForm(
                createExerciseInfo()
            )
        }

        cardioType.performClick()

        nameField.assertExists()
        equipmentField.assertDoesNotExist()
        muscleField.assertDoesNotExist()
        createButton.assertExists()
    }

    @Test
    fun selectingCalisthenicsExerciseChangesAvailableInformationFields() {
        rule.setContent {
            ExerciseInformationForm(
                createExerciseInfo()
            )
        }

        calisthenicsType.performClick()

        nameField.assertExists()
        equipmentField.assertDoesNotExist()
        muscleField.assertExists()
        createButton.assertExists()
    }

    @Test
    fun doesNotSaveAndDismissWithEmptyExerciseName() {
        rule.setContent {
            ExerciseInformationForm(
                createExerciseInfo()
            )
        }

        nameField.performTextClearance()
        createButton.assertIsNotEnabled()
    }

    @Test
    fun onlyNameFieldIsNeededToSaveExerciseAndCloseScreen() {
        var created: ExerciseUiState? = null
        var dismissed = false
        rule.setContent {
            ExerciseInformationForm(
                createExerciseInfo(
                    onDismiss = { dismissed = true },
                    createFunction = { created = it }
                )
            )
        }

        nameField.performTextInput(exercise.name)
        createButton.assertIsEnabled()
        equipmentField.performTextInput("Kit")
        createButton.assertIsEnabled()
        muscleField.performTextInput(exercise.muscleGroup)
        createButton.assertIsEnabled()
        equipmentField.performTextClearance()
        createButton.assertIsEnabled()

        createButton.performClick()

        assertThat(created?.name, equalTo(exercise.name))
        assertThat(created?.muscleGroup, equalTo(exercise.muscleGroup))
        assertThat(created?.equipment, equalTo(""))
        assertThat(dismissed, equalTo(true))
    }

    @Test
    fun cantSaveWithErrorMessageWhenExerciseNameAlreadyTaken() {
        rule.setContent {
            ExerciseInformationForm(
                createExerciseInfo(
                    savedExerciseNames = listOf(savedExercise.name)
                )
            )
        }

        nameField.performTextClearance()
        nameField.performTextInput(savedExercise.name)

        nameField.assertTextContains(savedExercise.name)
        nameError.assertExists()
        createButton.assertIsNotEnabled()
    }
}

private fun createExerciseInfo(
    formTitle: Int = R.string.create_exercise_title,
    buttonText: Int = R.string.create,
    savedExerciseNames: List<String> = listOf(),
    savedMuscleGroups: List<String> = listOf(),
    exercise: ExerciseUiState = ExerciseUiState(),
    onDismiss: () -> Unit = {},
    createFunction: (ExerciseUiState) -> Unit = {}
) = CreateExerciseInfo(
    formTitle = formTitle,
    buttonText = buttonText,
    savedExerciseNames = savedExerciseNames,
    savedMuscleGroups = savedMuscleGroups,
    exercise = exercise,
    onDismiss = onDismiss,
    createFunction = createFunction
)
