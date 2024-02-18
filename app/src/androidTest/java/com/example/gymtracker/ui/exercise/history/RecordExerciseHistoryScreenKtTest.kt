package com.example.gymtracker.ui.exercise.history

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.performClick
import com.example.gymtracker.ui.exercise.ExerciseUiState
import com.example.gymtracker.ui.exercise.history.state.CardioExerciseHistoryUiState
import com.example.gymtracker.ui.exercise.history.state.WeightsExerciseHistoryUiState
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Rule
import org.junit.Test
import java.time.LocalDate

class RecordExerciseHistoryScreenKtTest {

    @get:Rule
    val rule = createAndroidComposeRule<ComponentActivity>()

    private val weightsExercise = ExerciseUiState(0, "Curls", "Biceps", "Dumbbells")
    private val cardioExercise = ExerciseUiState(0, "Cardio")
    private val weightsHistory = WeightsExerciseHistoryUiState(
        id = 0,
        exerciseId = 0,
        date = LocalDate.now(),
        workoutHistoryId = 1,
        weight = 1.0,
        sets = 1,
        reps = 1
    )
    private val cardioHistory = CardioExerciseHistoryUiState(
        id = 0,
        exerciseId = 0,
        date = LocalDate.now(),
        workoutHistoryId = 1,
        minutes = 60,
        seconds = 0
    )

    private val newWeightsExerciseTitle =
        rule.onNode(hasText("New ${weightsExercise.name} Workout"))
    private val updateWeightsExerciseTitle =
        rule.onNode(hasText("Update ${weightsExercise.name} Workout"))
    private val newCardioExerciseTitle = rule.onNode(hasText("New ${cardioExercise.name} Workout"))
    private val updateCardioExerciseTitle =
        rule.onNode(hasText("Update ${cardioExercise.name} Workout"))
    private val setsField = rule.onNode(hasContentDescription("Sets"))
    private val distanceField = rule.onNode(hasContentDescription("Distance"))
    private val createButton = rule.onNode(hasText("Save"))
    private val closeButton = rule.onNode(hasContentDescription("Close"))

    @Test
    fun rendersEmptyCreateFormForWeightsExercise() {
        rule.setContent {
            RecordExerciseHistoryScreen(
                exercise = weightsExercise,
                saveFunction = {},
                onDismiss = {}
            )
        }

        newWeightsExerciseTitle.assertExists()
        createButton.assertExists()
        setsField.assertExists()
        closeButton.assertExists()
        updateWeightsExerciseTitle.assertDoesNotExist()
        newCardioExerciseTitle.assertDoesNotExist()
        distanceField.assertDoesNotExist()
    }

    @Test
    fun clickCloseButtonToDismissForWeightsExercise() {
        var dismissed = false
        rule.setContent {
            RecordExerciseHistoryScreen(
                exercise = weightsExercise,
                saveFunction = {},
                onDismiss = { dismissed = true }
            )
        }

        closeButton.performClick()

        assertThat(dismissed, equalTo(true))
    }

    @Test
    fun passExistingWeightsHistoryToRenderScreenToUpdateHistory() {
        rule.setContent {
            RecordExerciseHistoryScreen(
                exercise = weightsExercise,
                saveFunction = {},
                onDismiss = { },
                history = weightsHistory
            )
        }

        updateWeightsExerciseTitle.assertExists()
        createButton.assertExists()
        setsField.assertExists()
        closeButton.assertExists()
        newWeightsExerciseTitle.assertDoesNotExist()
        newCardioExerciseTitle.assertDoesNotExist()
        distanceField.assertDoesNotExist()
    }

    @Test
    fun rendersEmptyCreateFormForCardioExercise() {
        rule.setContent {
            RecordExerciseHistoryScreen(
                exercise = cardioExercise,
                saveFunction = {},
                onDismiss = {}
            )
        }

        newCardioExerciseTitle.assertExists()
        createButton.assertExists()
        distanceField.assertExists()
        closeButton.assertExists()
        updateCardioExerciseTitle.assertDoesNotExist()
        newWeightsExerciseTitle.assertDoesNotExist()
        setsField.assertDoesNotExist()
    }

    @Test
    fun clickCloseButtonToDismissForCardioExercise() {
        var dismissed = false
        rule.setContent {
            RecordExerciseHistoryScreen(
                exercise = cardioExercise,
                saveFunction = {},
                onDismiss = { dismissed = true }
            )
        }

        closeButton.performClick()

        assertThat(dismissed, equalTo(true))
    }

    @Test
    fun passExistingCardioHistoryToRenderScreenToUpdateHistory() {
        rule.setContent {
            RecordExerciseHistoryScreen(
                exercise = cardioExercise,
                saveFunction = {},
                onDismiss = { },
                history = cardioHistory
            )
        }

        updateCardioExerciseTitle.assertExists()
        createButton.assertExists()
        distanceField.assertExists()
        closeButton.assertExists()
        newCardioExerciseTitle.assertDoesNotExist()
        newWeightsExerciseTitle.assertDoesNotExist()
        setsField.assertDoesNotExist()
    }
}
