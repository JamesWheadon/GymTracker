package com.askein.gymtracker.ui.workout.history.create

import androidx.activity.ComponentActivity
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextClearance
import androidx.compose.ui.test.performTextInput
import com.askein.gymtracker.enums.WeightUnits
import com.askein.gymtracker.helper.hasRole
import com.askein.gymtracker.ui.exercise.ExerciseUiState
import com.askein.gymtracker.ui.exercise.history.state.WeightsExerciseHistoryUiState
import com.askein.gymtracker.ui.exercise.history.state.record.RecordWeightsHistoryState
import com.askein.gymtracker.ui.exercise.history.state.record.toRecordWeightsHistoryState
import com.askein.gymtracker.ui.user.LocalUserPreferences
import com.askein.gymtracker.ui.user.UserPreferencesUiState
import org.hamcrest.CoreMatchers
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.not
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Rule
import org.junit.Test

class RecordWeightsExerciseCardKtTest {

    @get:Rule
    val rule = createAndroidComposeRule<ComponentActivity>()

    private val curlsExercise = ExerciseUiState(1, "Curls", "Biceps", "Dumbbells")
    private val integerErrorText = "Must be a positive number"
    private val decimalErrorText = "Must be a number"
    private val curlsExerciseTitle = rule.onNode(hasText("Curls"))
    private val curlsCheckbox = rule.onNode(hasRole(Role.Checkbox))
    private val setsField = rule.onNode(hasContentDescription("Sets"))
    private val repsField = rule.onNode(hasContentDescription("Reps"))
    private val weightField = rule.onNode(hasContentDescription("Weight"))
    private val unitField = rule.onNode(hasContentDescription("units"))
    private val weightKilogramsChosen = rule.onNode(hasText("kg"))
    private val weightPoundsChosen = rule.onNode(hasText("lb"))

    @Test
    fun rendersRecordWeightsExerciseCard() {
        rule.setContent {
            RecordWeightsExerciseCard(
                exercise = curlsExercise,
                recordWeightsHistory = null,
                recordWeightsHistoryOnChange = { },
                selectExerciseFunction = { },
                deselectExerciseFunction = { }
            )
        }

        curlsExerciseTitle.assertExists()
        curlsCheckbox.assertExists()
        setsField.assertDoesNotExist()
        repsField.assertDoesNotExist()
        weightField.assertDoesNotExist()
        unitField.assertDoesNotExist()
    }

    @Test
    fun recordWeightsExerciseCardClickingCheckboxRendersFormFields() {
        var selected = false
        var deselected = false
        rule.setContent {
            val userPreferencesUiState = UserPreferencesUiState()
            CompositionLocalProvider(LocalUserPreferences provides userPreferencesUiState) {
                var recordWeightsHistory: RecordWeightsHistoryState? by remember {
                    mutableStateOf(
                        null
                    )
                }
                RecordWeightsExerciseCard(
                    exercise = curlsExercise,
                    recordWeightsHistory = recordWeightsHistory,
                    recordWeightsHistoryOnChange = { },
                    selectExerciseFunction = {
                        selected = true
                        recordWeightsHistory =
                            WeightsExerciseHistoryUiState().toRecordWeightsHistoryState(
                                0,
                                true,
                                WeightUnits.KILOGRAMS
                            )
                    },
                    deselectExerciseFunction = {
                        deselected = true
                    }
                )
            }
        }

        curlsCheckbox.performClick()

        curlsExerciseTitle.assertExists()
        setsField.assertExists()
        unitField.assertExists()
        assertThat(selected, equalTo(true))
        assertThat(deselected, equalTo(false))

        curlsCheckbox.performClick()

        assertThat(deselected, equalTo(true))
    }

    @Test
    fun rendersNoErrorsWhenNoErrorsInRecordWeightsExerciseCard() {
        rule.setContent {
            val userPreferencesUiState = UserPreferencesUiState()

            var recordWeightsHistory by remember {
                mutableStateOf(
                    WeightsExerciseHistoryUiState().toRecordWeightsHistoryState(
                        0,
                        true,
                        WeightUnits.KILOGRAMS
                    )
                )
            }
            CompositionLocalProvider(LocalUserPreferences provides userPreferencesUiState) {
                RecordWeightsExerciseCard(
                    exercise = curlsExercise,
                    recordWeightsHistory = recordWeightsHistory,
                    recordWeightsHistoryOnChange = { newState -> recordWeightsHistory = newState },
                    selectExerciseFunction = { },
                    deselectExerciseFunction = { }
                )
            }
        }
        curlsCheckbox.performClick()
        setsField.performTextClearance()
        setsField.performTextInput("1")
        repsField.performTextClearance()
        repsField.performTextInput("1")

        assertTextDoesNotContain(setsField, integerErrorText)
        assertTextDoesNotContain(repsField, integerErrorText)
        assertTextDoesNotContain(weightField, decimalErrorText)
    }

    @Test
    fun recordWeightsExerciseCardClickingCheckboxRendersFormFieldsWithKilogramsChosen() {
        rule.setContent {
            val userPreferencesUiState = UserPreferencesUiState(
                defaultWeightUnit = WeightUnits.KILOGRAMS
            )
            CompositionLocalProvider(LocalUserPreferences provides userPreferencesUiState) {
                RecordWeightsExerciseCard(
                    exercise = curlsExercise,
                    recordWeightsHistory = WeightsExerciseHistoryUiState().toRecordWeightsHistoryState(
                        0,
                        true,
                        WeightUnits.KILOGRAMS
                    ),
                    recordWeightsHistoryOnChange = { },
                    selectExerciseFunction = { },
                    deselectExerciseFunction = { },
                )
            }
        }

        curlsCheckbox.performClick()

        weightKilogramsChosen.assertExists()
    }

    @Test
    fun recordWeightsExerciseCardClickingCheckboxRendersFormFieldsWithPoundsChosen() {
        rule.setContent {
            val userPreferencesUiState = UserPreferencesUiState(
                defaultWeightUnit = WeightUnits.POUNDS
            )
            CompositionLocalProvider(LocalUserPreferences provides userPreferencesUiState) {
                RecordWeightsExerciseCard(
                    exercise = curlsExercise,
                    recordWeightsHistory = WeightsExerciseHistoryUiState().toRecordWeightsHistoryState(
                        0,
                        true,
                        WeightUnits.POUNDS
                    ),
                    recordWeightsHistoryOnChange = { },
                    selectExerciseFunction = { },
                    deselectExerciseFunction = { },
                )
            }
        }

        curlsCheckbox.performClick()

        weightPoundsChosen.assertExists()
    }

    @Test
    fun recordWeightsExerciseCardClickingCheckboxRendersFormFieldsWithPoundsChosenAutoWeightConvert() {
        rule.setContent {
            val userPreferencesUiState = UserPreferencesUiState(
                defaultWeightUnit = WeightUnits.POUNDS
            )
            CompositionLocalProvider(LocalUserPreferences provides userPreferencesUiState) {
                RecordWeightsExerciseCard(
                    exercise = curlsExercise,
                    recordWeightsHistory = WeightsExerciseHistoryUiState(
                        sets = 1,
                        reps = listOf(1),
                        weight = listOf(10.0)
                    ).toRecordWeightsHistoryState(0, true, WeightUnits.POUNDS),
                    recordWeightsHistoryOnChange = { },
                    selectExerciseFunction = { },
                    deselectExerciseFunction = { },
                )
            }
        }

        curlsCheckbox.performClick()

        rule.onNode(hasText("22.05")).assertExists()
    }

    private fun assertTextDoesNotContain(
        semanticsNodeInteraction: SemanticsNodeInteraction, text: String
    ) {
        val config = semanticsNodeInteraction.fetchSemanticsNode().config
        assertThat(
            config[SemanticsProperties.Text].reduce { acc, string -> acc + string }.text
                    + config[SemanticsProperties.EditableText],
            not(CoreMatchers.containsString(text))
        )
    }
}
