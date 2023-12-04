package com.example.gymtracker.ui.exercise.history

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import com.example.gymtracker.data.exerciseHistory.ExerciseHistory
import com.example.gymtracker.ui.exercise.ExerciseUiState
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert
import org.junit.Rule
import org.junit.Test
import java.time.LocalDate

class RecordExerciseHistoryScreenKtTest {

    @get:Rule
    val rule = createAndroidComposeRule<ComponentActivity>()

    private val createButton = rule.onNode(hasText("Save"))
    private val setsField = rule.onNode(hasContentDescription("Sets"))
    private val repsField = rule.onNode(hasContentDescription("Reps"))
    private val weightField = rule.onNode(hasContentDescription("Weight"))
    private val unitsField = rule.onNode(hasContentDescription("Units"))
    private val closeButton = rule.onNode(hasContentDescription("Close"))

    private val exercise = ExerciseUiState(0, "Curls", "Biceps", "Dumbbells")
    private val history = ExerciseHistory(0, 0, 1.0, 1, 1, LocalDate.now())

    @Test
    fun rendersEmptyCreateForm() {
        rule.setContent {
            RecordExerciseHistoryScreen(
                exercise = exercise,
                saveFunction = {},
                onDismiss = {}
            )
        }

        createButton.assertExists()
        setsField.assertExists()
        repsField.assertExists()
        weightField.assertExists()
        unitsField.assertExists()
        closeButton.assertExists()
    }
    @Test
    fun clickCloseButtonToDismiss() {
        var dismissed = false
        rule.setContent {
            RecordExerciseHistoryScreen(
                exercise = exercise,
                saveFunction = {},
                onDismiss = { dismissed = true }
            )
        }

        closeButton.performClick()

        MatcherAssert.assertThat(dismissed, equalTo(true))
    }

    @Test
    fun doesNotSaveAndDismissWithEmptySetsField() {
        var created: ExerciseHistory? = null
        var dismissed = false
        rule.setContent {
            RecordExerciseHistoryScreen(
                exercise = exercise,
                saveFunction = { created = it },
                onDismiss = { dismissed = true }
            )
        }

        repsField.performTextInput(history.reps.toString())
        weightField.performTextInput(history.weight.toString())

        createButton.performClick()

        MatcherAssert.assertThat(created, equalTo(null))
        MatcherAssert.assertThat(dismissed, equalTo(false))
    }

    @Test
    fun doesNotSaveAndDismissWithEmptyRepsField() {
        var created: ExerciseHistory? = null
        var dismissed = false
        rule.setContent {
            RecordExerciseHistoryScreen(
                exercise = exercise,
                saveFunction = { created = it },
                onDismiss = { dismissed = true }
            )
        }

        setsField.performTextInput(history.sets.toString())
        weightField.performTextInput(history.weight.toString())

        createButton.performClick()

        MatcherAssert.assertThat(created, equalTo(null))
        MatcherAssert.assertThat(dismissed, equalTo(false))
    }

    @Test
    fun doesNotSaveAndDismissWithEmptyWeightField() {
        var created: ExerciseHistory? = null
        var dismissed = false
        rule.setContent {
            RecordExerciseHistoryScreen(
                exercise = exercise,
                saveFunction = { created = it },
                onDismiss = { dismissed = true }
            )
        }

        setsField.performTextInput(history.sets.toString())
        repsField.performTextInput(history.reps.toString())

        createButton.performClick()

        MatcherAssert.assertThat(created, equalTo(null))
        MatcherAssert.assertThat(dismissed, equalTo(false))
    }

    @Test
    fun savesAndDismissRecordScreenWithAllFieldsFull() {
        var created: ExerciseHistory? = null
        var dismissed = false
        rule.setContent {
            RecordExerciseHistoryScreen(
                exercise = exercise,
                saveFunction = { created = it },
                onDismiss = { dismissed = true }
            )
        }

        setsField.performTextInput(history.sets.toString())
        repsField.performTextInput(history.reps.toString())
        weightField.performTextInput(history.weight.toString())

        createButton.performClick()

        MatcherAssert.assertThat(created, equalTo(history))
        MatcherAssert.assertThat(dismissed, equalTo(true))
    }
}
