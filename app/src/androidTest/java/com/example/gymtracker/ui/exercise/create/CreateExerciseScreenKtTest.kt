package com.example.gymtracker.ui.exercise.create

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import com.example.gymtracker.data.exercise.Exercise
import com.example.gymtracker.data.exercise.ExerciseRepository
import com.example.gymtracker.ui.exercise.ExercisesScreenViewModel
import com.example.gymtracker.ui.exercise.toExerciseUiState
import kotlinx.coroutines.flow.flowOf
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

class CreateExerciseScreenKtTest {

    @get:Rule
    val rule = createAndroidComposeRule<ComponentActivity>()

    @Mock
    private lateinit var repository: ExerciseRepository

    private lateinit var viewModel: ExercisesScreenViewModel

    private val createButton = rule.onNode(hasText("Create"))
    private val nameField = rule.onNode(hasContentDescription("Exercise Name"))
    private val equipmentField = rule.onNode(hasContentDescription("Equipment"))
    private val muscleField = rule.onNode(hasContentDescription("Muscle Group"))
    private val closeButton = rule.onNode(hasContentDescription("Close"))
    private val nameError = rule.onNode(hasText("Name already taken"))

    private val exercise = Exercise(0, "Test Curls", "Biceps", "Dumbbells")
    private val savedExercise = Exercise(1, "Saved Curls", "Biceps", "Dumbbells")

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)

        `when`(repository.getAllExercisesStream()).thenReturn(flowOf(listOf(savedExercise)))
        `when`(repository.getAllMuscleGroupsStream()).thenReturn(flowOf(listOf(savedExercise.muscleGroup)))
        `when`(repository.getAllExerciseNames()).thenReturn(flowOf(listOf(savedExercise.name)))

        viewModel = ExercisesScreenViewModel(repository)
    }

    @Test
    fun rendersEmptyCreateForm() {
        rule.setContent {
            ExerciseInformationForm(
                formTitle = "Create New Exercise",
                onDismiss = {},
                createFunction = {},
                buttonText = "Create",
                viewModel = viewModel
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
                buttonText = "Create",
                viewModel = viewModel
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
                buttonText = "Create",
                viewModel = viewModel
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
                buttonText = "Create",
                viewModel = viewModel
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
                buttonText = "Create",
                viewModel = viewModel
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
                buttonText = "Create",
                viewModel = viewModel
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
                exercise = exercise.toExerciseUiState(),
                viewModel = viewModel
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
                formTitle = "Create New Exercise",
                onDismiss = { },
                createFunction = { },
                buttonText = "Create",
                exercise = savedExercise.toExerciseUiState(),
                viewModel = viewModel
            )
        }

        nameField.assertTextContains(savedExercise.name)
        equipmentField.assertTextContains(savedExercise.equipment)
        muscleField.assertTextContains(savedExercise.muscleGroup)
        nameError.assertExists()
    }

    @Test
    fun cantSaveWithErrorMessageWhenExerciseNameAlreadyTaken() {
        var created: Exercise? = null
        var dismissed = false

        rule.setContent {
            ExerciseInformationForm(
                formTitle = "Create New Exercise",
                onDismiss = { dismissed = true },
                createFunction = { newExercise -> created = newExercise },
                buttonText = "Create",
                exercise = savedExercise.toExerciseUiState(),
                viewModel = viewModel
            )
        }

        nameField.assertTextContains(savedExercise.name)
        equipmentField.assertTextContains(savedExercise.equipment)
        muscleField.assertTextContains(savedExercise.muscleGroup)
        nameError.assertExists()

        createButton.performClick()
        assertThat(dismissed, equalTo(false))
        assertThat(created, equalTo(null))
    }
}
