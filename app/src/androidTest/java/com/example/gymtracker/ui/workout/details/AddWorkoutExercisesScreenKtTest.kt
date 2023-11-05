package com.example.gymtracker.ui.workout.details

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsOff
import androidx.compose.ui.test.assertIsOn
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.performClick
import com.example.gymtracker.ui.exercise.ExerciseUiState
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.junit.Rule
import org.junit.Test

class AddWorkoutExercisesScreenKtTest {

    @get:Rule
    val rule = createAndroidComposeRule<ComponentActivity>()

    private val curlsExerciseUiState = ExerciseUiState(0, "Curls", "Biceps", "Dumbbells")
    private val dipsExerciseUiState = ExerciseUiState(1, "Dips", "Triceps", "Dumbbells And Bars")

    private val curlsExercise = rule.onNode(hasText("Curls"))
    private val curlsMuscle = rule.onNode(hasText("Biceps"))
    private val curlsEquipment = rule.onNode(hasText("Dumbbells"))
    private val dipsExercise = rule.onNode(hasText("Dips"))
    private val dipsMuscle = rule.onNode(hasText("Triceps"))
    private val dipsEquipment = rule.onNode(hasText("Dumbbells And Bars"))
    private val workoutExercisesTitles = rule.onNode(hasText("Workout Exercises"))
    private val availableExercisesTitles = rule.onNode(hasText("Available Exercises"))
    private val addWorkoutExercisesCloseButton = rule.onNode(hasContentDescription("Close"))

    @Test
    fun rendersAddRemoveExerciseCardWithBoxChecked() {
        rule.setContent {
            AddRemoveExerciseCard(
                exercise = curlsExerciseUiState,
                checked = true,
                clickFunction = { }
            )
        }

        curlsExercise.assertExists()
        curlsMuscle.assertExists()
        curlsEquipment.assertExists()
        val checkBox = rule.onNode(hasContentDescription("Deselect Curls"))
        checkBox.assertExists()
        checkBox.assertIsOn()
    }

    @Test
    fun rendersAddRemoveExerciseCardWithBoxUnchecked() {
        rule.setContent {
            AddRemoveExerciseCard(
                exercise = curlsExerciseUiState,
                checked = false,
                clickFunction = { }
            )
        }

        curlsExercise.assertExists()
        curlsMuscle.assertExists()
        curlsEquipment.assertExists()
        val checkBox = rule.onNode(hasContentDescription("Select Curls"))
        checkBox.assertExists()
        checkBox.assertIsOff()
    }

    @Test
    fun clickingAddRemoveExerciseCarCheckBoxCallsClickFunction() {
        var clicked = false

        rule.setContent {
            AddRemoveExerciseCard(
                exercise = curlsExerciseUiState,
                checked = false,
                clickFunction = { clicked = true }
            )
        }

        curlsExercise.assertExists()
        curlsMuscle.assertExists()
        curlsEquipment.assertExists()
        val checkBox = rule.onNode(hasContentDescription("Select Curls"))
        checkBox.assertExists()
        checkBox.assertIsOff()

        checkBox.performClick()
        checkBox.assertIsOff()
        MatcherAssert.assertThat(clicked, CoreMatchers.equalTo(true))
    }

    @Test
    fun rendersExerciseList() {
        rule.setContent {
            ExercisesList(
                chosenExercises = listOf(curlsExerciseUiState, dipsExerciseUiState),
                clickFunction = { },
                listTitle = "Test List",
                exercisesSelected = true
            )
        }

        val curlsCheckBox = rule.onNode(hasContentDescription("Deselect Curls"))
        val dipsCheckBox = rule.onNode(hasContentDescription("Deselect Dips"))

        rule.onNode(hasText("Test List")).assertExists()
        curlsExercise.assertExists()
        curlsMuscle.assertExists()
        curlsEquipment.assertExists()
        curlsCheckBox.assertExists()
        curlsCheckBox.assertIsOn()
        dipsExercise.assertExists()
        dipsMuscle.assertExists()
        dipsEquipment.assertExists()
        dipsCheckBox.assertExists()
        dipsCheckBox.assertIsOn()
    }

    @Test
    fun rendersAddWorkoutExercisesScreenWithNoExercises() {
        rule.setContent {
            AddWorkoutExercisesScreen(
                chosenExercises = listOf(),
                remainingExercises = listOf(),
                selectFunction = { },
                deselectFunction = { },
                onDismiss = {  }
            )
        }

        workoutExercisesTitles.assertDoesNotExist()
        availableExercisesTitles.assertDoesNotExist()
        addWorkoutExercisesCloseButton.assertExists()
    }

    @Test
    fun rendersAddWorkoutExercisesScreenWithNoWorkoutExercises() {
        rule.setContent {
            AddWorkoutExercisesScreen(
                chosenExercises = listOf(),
                remainingExercises = listOf(curlsExerciseUiState),
                selectFunction = { },
                deselectFunction = { },
                onDismiss = {  }
            )
        }

        workoutExercisesTitles.assertDoesNotExist()
        availableExercisesTitles.assertExists()
        addWorkoutExercisesCloseButton.assertExists()
        curlsExercise.assertExists()
    }

    @Test
    fun rendersAddWorkoutExercisesScreenWithNoAvailableExercises() {
        rule.setContent {
            AddWorkoutExercisesScreen(
                chosenExercises = listOf(curlsExerciseUiState),
                remainingExercises = listOf(),
                selectFunction = { },
                deselectFunction = { },
                onDismiss = {  }
            )
        }

        workoutExercisesTitles.assertExists()
        availableExercisesTitles.assertDoesNotExist()
        addWorkoutExercisesCloseButton.assertExists()
        curlsExercise.assertExists()
    }

    @Test
    fun rendersAddWorkoutExercisesScreenWithAvailableExercisesAndChosenExercises() {
        rule.setContent {
            AddWorkoutExercisesScreen(
                chosenExercises = listOf(curlsExerciseUiState),
                remainingExercises = listOf(dipsExerciseUiState),
                selectFunction = { },
                deselectFunction = { },
                onDismiss = {  }
            )
        }

        workoutExercisesTitles.assertExists()
        availableExercisesTitles.assertExists()
        addWorkoutExercisesCloseButton.assertExists()
        curlsExercise.assertExists()
        dipsExercise.assertExists()
    }

    @Test
    fun addWorkoutExercisesScreenClickingCheckBoxesCallsFunctions() {
        var selected: String? = null
        var deselected: String? = null

        rule.setContent {
            AddWorkoutExercisesScreen(
                chosenExercises = listOf(curlsExerciseUiState),
                remainingExercises = listOf(dipsExerciseUiState),
                selectFunction = { exerciseUiState -> deselected = exerciseUiState.name },
                deselectFunction = { exerciseUiState -> selected = exerciseUiState.name },
                onDismiss = {  }
            )
        }

        val curlsCheckBox = rule.onNode(hasContentDescription("Deselect Curls"))
        val dipsCheckBox = rule.onNode(hasContentDescription("Select Dips"))

        workoutExercisesTitles.assertExists()
        availableExercisesTitles.assertExists()
        addWorkoutExercisesCloseButton.assertExists()
        curlsExercise.assertExists()
        dipsExercise.assertExists()

        curlsCheckBox.performClick()
        dipsCheckBox.performClick()

        MatcherAssert.assertThat(selected, CoreMatchers.equalTo(curlsExerciseUiState.name))
        MatcherAssert.assertThat(deselected, CoreMatchers.equalTo(dipsExerciseUiState.name))
    }

    @Test
    fun addWorkoutExercisesScreenClickingCloseButtonCallsOnDismiss() {
        var dismissed = false

        rule.setContent {
            AddWorkoutExercisesScreen(
                chosenExercises = listOf(curlsExerciseUiState),
                remainingExercises = listOf(dipsExerciseUiState),
                selectFunction = { },
                deselectFunction = { },
                onDismiss = { dismissed = true }
            )
        }

        addWorkoutExercisesCloseButton.assertExists()
        addWorkoutExercisesCloseButton.performClick()
        MatcherAssert.assertThat(dismissed, CoreMatchers.equalTo(true))
    }
}
