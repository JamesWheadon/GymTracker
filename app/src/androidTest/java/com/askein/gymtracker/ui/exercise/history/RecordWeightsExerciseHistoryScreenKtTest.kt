package com.askein.gymtracker.ui.exercise.history

import androidx.activity.ComponentActivity
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextClearance
import androidx.compose.ui.test.performTextInput
import com.askein.gymtracker.enums.WeightUnits
import com.askein.gymtracker.ui.exercise.ExerciseUiState
import com.askein.gymtracker.ui.exercise.history.state.WeightsExerciseHistoryUiState
import com.askein.gymtracker.ui.user.LocalUserPreferences
import com.askein.gymtracker.ui.user.UserPreferencesUiState
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Rule
import org.junit.Test
import java.time.LocalDate

class RecordWeightsExerciseHistoryScreenKtTest {

    @get:Rule
    val rule = createAndroidComposeRule<ComponentActivity>()

    private val cardTitleText = "Record Weights"
    private val cardTitle = rule.onNode(hasText(cardTitleText))
    private val createButton = rule.onNode(hasText("Save"))
    private val setsField = rule.onNode(hasContentDescription("Sets"))
    private val repsField = rule.onNode(hasContentDescription("Reps"))
    private val weightField = rule.onNode(hasContentDescription("Weight"))
    private val unitsField = rule.onNode(hasContentDescription("units"))
    private val weightKilogramsChosen = rule.onNode(hasText("kg"))
    private val weightPoundsChosen = rule.onNode(hasText("lb"))

    private val exercise = ExerciseUiState(0, "Curls", "Biceps", "Dumbbells")
    private val history = WeightsExerciseHistoryUiState(
        id = 0,
        exerciseId = 0,
        date = LocalDate.now(),
        workoutHistoryId = null,
        weight = listOf(1.0),
        sets = 1,
        reps = listOf(1)
    )

    @Test
    fun rendersEmptyCreateForm() {
        rule.setContent {
            val userPreferencesUiState = UserPreferencesUiState()
            CompositionLocalProvider(LocalUserPreferences provides userPreferencesUiState) {
                RecordWeightsExerciseHistoryCard(
                    exerciseId = exercise.id,
                    cardTitle = cardTitleText,
                    saveFunction = {},
                    onDismiss = {}
                )
            }
        }

        cardTitle.assertExists()
        setsField.assertExists()
        unitsField.assertExists()
        createButton.assertExists()
    }

    @Test
    fun doesNotSaveAndDismissWithEmptySetsField() {
        var created: WeightsExerciseHistoryUiState? = null
        var dismissed = false
        rule.setContent {
            val userPreferencesUiState = UserPreferencesUiState()
            CompositionLocalProvider(LocalUserPreferences provides userPreferencesUiState) {
                RecordWeightsExerciseHistoryCard(
                    exerciseId = exercise.id,
                    cardTitle = cardTitleText,
                    saveFunction = { created = it as WeightsExerciseHistoryUiState },
                    onDismiss = { dismissed = true }
                )
            }
        }

        createButton.performClick()

        assertThat(created, equalTo(null))
        assertThat(dismissed, equalTo(false))
    }

    @Test
    fun doesNotSaveAndDismissWithEmptyRepsField() {
        var created: WeightsExerciseHistoryUiState? = null
        var dismissed = false
        rule.setContent {
            val userPreferencesUiState = UserPreferencesUiState()
            CompositionLocalProvider(LocalUserPreferences provides userPreferencesUiState) {
                RecordWeightsExerciseHistoryCard(
                    exerciseId = exercise.id,
                    cardTitle = cardTitleText,
                    saveFunction = { created = it as WeightsExerciseHistoryUiState },
                    onDismiss = { dismissed = true }
                )
            }
        }

        setsField.performTextClearance()
        setsField.performTextInput(history.sets.toString())
        repsField.performTextClearance()

        createButton.performClick()

        assertThat(created, equalTo(null))
        assertThat(dismissed, equalTo(false))
    }

    @Test
    fun doesNotSaveAndDismissWithEmptyWeightField() {
        var created: WeightsExerciseHistoryUiState? = null
        var dismissed = false
        rule.setContent {
            val userPreferencesUiState = UserPreferencesUiState()
            CompositionLocalProvider(LocalUserPreferences provides userPreferencesUiState) {
                RecordWeightsExerciseHistoryCard(
                    exerciseId = exercise.id,
                    cardTitle = cardTitleText,
                    saveFunction = { created = it as WeightsExerciseHistoryUiState },
                    onDismiss = { dismissed = true }
                )
            }
        }

        setsField.performTextClearance()
        setsField.performTextInput(history.sets.toString())
        repsField.performTextClearance()
        repsField.performTextInput(history.reps.toString())
        weightField.performTextClearance()

        createButton.performClick()

        assertThat(created, equalTo(null))
        assertThat(dismissed, equalTo(false))
    }

    @Test
    fun savesAndDismissRecordScreenWithAllFieldsFull() {
        var created: WeightsExerciseHistoryUiState? = null
        var dismissed = false
        rule.setContent {
            val userPreferencesUiState = UserPreferencesUiState()
            CompositionLocalProvider(LocalUserPreferences provides userPreferencesUiState) {
                RecordWeightsExerciseHistoryCard(
                    exerciseId = exercise.id,
                    cardTitle = cardTitleText,
                    saveFunction = { created = it as WeightsExerciseHistoryUiState },
                    onDismiss = { dismissed = true }
                )
            }
        }

        setsField.performTextClearance()
        setsField.performTextInput(history.sets.toString())
        repsField.performTextClearance()
        repsField.performTextInput(history.reps!!.first().toString())
        weightField.performTextClearance()
        weightField.performTextInput(history.weight.first().toString())

        createButton.performClick()

        assertThat(created, equalTo(history))
        assertThat(dismissed, equalTo(true))
    }

    @Test
    fun fillsFormFieldsWithInformationFromExistingHistory() {
        rule.setContent {
            val userPreferencesUiState = UserPreferencesUiState()
            CompositionLocalProvider(LocalUserPreferences provides userPreferencesUiState) {
                RecordWeightsExerciseHistoryCard(
                    exerciseId = exercise.id,
                    cardTitle = cardTitleText,
                    saveFunction = { },
                    onDismiss = { },
                    savedHistory = history
                )
            }
        }

        setsField.assertTextContains(history.sets.toString())
        repsField.assertTextContains(history.reps!!.first().toString())
        weightField.assertTextContains(history.weight.first().toString())
    }

    @Test
    fun rendersEmptyCreateFormWithKilogramsChosen() {
        rule.setContent {
            val userPreferencesUiState = UserPreferencesUiState(
                defaultWeightUnit = WeightUnits.KILOGRAMS
            )
            CompositionLocalProvider(LocalUserPreferences provides userPreferencesUiState) {
                RecordWeightsExerciseHistoryCard(
                    exerciseId = exercise.id,
                    cardTitle = cardTitleText,
                    saveFunction = {},
                    onDismiss = {}
                )
            }
        }

        weightKilogramsChosen.assertExists()
    }

    @Test
    fun rendersEmptyCreateFormWithPoundsChosen() {
        rule.setContent {
            val userPreferencesUiState = UserPreferencesUiState(
                defaultWeightUnit = WeightUnits.POUNDS
            )
            CompositionLocalProvider(LocalUserPreferences provides userPreferencesUiState) {
                RecordWeightsExerciseHistoryCard(
                    exerciseId = exercise.id,
                    cardTitle = cardTitleText,
                    saveFunction = {},
                    onDismiss = {}
                )
            }
        }

        weightPoundsChosen.assertExists()
    }

    @Test
    fun rendersEmptyCreateFormWithPoundsChosenWeightAutoConvert() {
        rule.setContent {
            val userPreferencesUiState = UserPreferencesUiState(
                defaultWeightUnit = WeightUnits.POUNDS
            )
            CompositionLocalProvider(LocalUserPreferences provides userPreferencesUiState) {
                RecordWeightsExerciseHistoryCard(
                    exerciseId = exercise.id,
                    cardTitle = cardTitleText,
                    saveFunction = {},
                    onDismiss = {},
                    savedHistory = WeightsExerciseHistoryUiState(
                        sets = 1,
                        reps = listOf(2),
                        weight = listOf(10.0)
                    )
                )
            }
        }

        rule.onNode(hasText("22.05")).assertExists()
    }
}
