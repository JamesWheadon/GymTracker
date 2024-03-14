package com.example.gymtracker.ui.workout.history.create

import androidx.activity.ComponentActivity
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.hasAnySibling
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.hasSetTextAction
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import com.example.gymtracker.converters.DistanceUnits
import com.example.gymtracker.ui.exercise.ExerciseUiState
import com.example.gymtracker.ui.exercise.history.state.CardioExerciseHistoryUiState
import com.example.gymtracker.ui.user.LocalUserPreferences
import com.example.gymtracker.ui.user.UserPreferencesUiState
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Rule
import org.junit.Test

class RecordCardioExerciseCardKtTest {

    @get:Rule
    val rule = createAndroidComposeRule<ComponentActivity>()

    private val treadmillExercise = ExerciseUiState(3, "Treadmill")
    private val treadmillExerciseTitle = rule.onNode(hasText("Treadmill"))
    private val treadmillCheckbox =
        rule.onNode(hasClickAction() and hasSetTextAction().not() and hasAnySibling(hasText("Treadmill")))
    private val minutestField = rule.onNode(hasContentDescription("Minutes"))
    private val secondsField = rule.onNode(hasContentDescription("Seconds"))
    private val distanceField = rule.onNode(hasContentDescription("Distance"))
    private val caloriesField = rule.onNode(hasContentDescription("Calories"))
    private val unitField = rule.onNode(hasContentDescription("units"))
    private val distanceKilometersChosen = rule.onNode(hasText("km"))
    private val distanceMilesChosen = rule.onNode(hasText("mi"))
    private val distanceMetersChosen = rule.onNode(hasText("m"))
    private val cardioError = rule.onNode(hasText("Must have a time, distance or calories entered"))

    @Test
    fun rendersRecordCardioExerciseCard() {
        rule.setContent {
            RecordCardioExerciseCard(
                exercise = treadmillExercise,
                exerciseHistory = null,
                selectExerciseFunction = { },
                deselectExerciseFunction = { },
                errorStateChange = { _, _ -> }
            )
        }

        treadmillExerciseTitle.assertExists()
        treadmillCheckbox.assertExists()
        minutestField.assertDoesNotExist()
        secondsField.assertDoesNotExist()
        distanceField.assertDoesNotExist()
        caloriesField.assertDoesNotExist()
        unitField.assertDoesNotExist()
    }

    @Test
    fun recordCardioExerciseCardClickingCheckboxRendersFormFields() {
        var selected = false
        var deselected = false
        rule.setContent {
            val userPreferencesUiState = UserPreferencesUiState()
            CompositionLocalProvider(LocalUserPreferences provides userPreferencesUiState) {
                var exerciseHistory: CardioExerciseHistoryUiState? by remember { mutableStateOf(null) }
                RecordCardioExerciseCard(
                    exercise = treadmillExercise,
                    exerciseHistory = exerciseHistory,
                    selectExerciseFunction = {
                        selected = true
                        exerciseHistory = CardioExerciseHistoryUiState()
                    },
                    deselectExerciseFunction = {
                        deselected = true
                    },
                    errorStateChange = { _, _ -> }
                )
            }
        }

        treadmillCheckbox.performClick()

        treadmillExerciseTitle.assertExists()
        minutestField.assertExists()
        secondsField.assertExists()
        distanceField.assertExists()
        caloriesField.assertExists()
        unitField.assertExists()
        assertThat(selected, equalTo(true))
        assertThat(deselected, equalTo(false))

        treadmillCheckbox.performClick()

        assertThat(deselected, equalTo(true))
    }

    @Test
    fun recordCardioExerciseCardClickingCheckboxRendersFormFieldsWithErrors() {
        var error = false
        var id = -1

        rule.setContent {
            val userPreferencesUiState = UserPreferencesUiState()
            CompositionLocalProvider(LocalUserPreferences provides userPreferencesUiState) {
                RecordCardioExerciseCard(
                    exercise = treadmillExercise,
                    exerciseHistory = CardioExerciseHistoryUiState(),
                    selectExerciseFunction = { },
                    deselectExerciseFunction = { },
                    errorStateChange = { exerciseId, exerciseError ->
                        error = exerciseError
                        id = exerciseId
                    }
                )
            }
        }
        treadmillCheckbox.performClick()
        cardioError.assertExists()

        assertThat(error, equalTo(true))
        assertThat(id, equalTo(3))
    }

    @Test
    fun returnsFalseErrorStateWhenNoErrorsInRecordCardioExerciseCard() {
        var error = false
        var id = -1
        val exerciseHistory = CardioExerciseHistoryUiState()

        rule.setContent {
            val userPreferencesUiState = UserPreferencesUiState()
            CompositionLocalProvider(LocalUserPreferences provides userPreferencesUiState) {
                RecordCardioExerciseCard(
                    exercise = treadmillExercise,
                    exerciseHistory = exerciseHistory,
                    selectExerciseFunction = { },
                    deselectExerciseFunction = { },
                    errorStateChange = { exerciseId, exerciseError ->
                        error = exerciseError
                        id = exerciseId
                    }
                )
            }
        }
        treadmillCheckbox.performClick()
        distanceField.performTextInput("10.0")

        cardioError.assertDoesNotExist()
        assertThat(exerciseHistory.distance, equalTo(10.0))
        assertThat(error, equalTo(false))
        assertThat(id, equalTo(3))
    }

    @Test
    fun recordCardioExerciseCardClickingCheckboxRendersFormFieldsWithKilometersUnit() {
        rule.setContent {
            val userPreferencesUiState = UserPreferencesUiState(
                defaultDistanceUnit = DistanceUnits.KILOMETERS
            )
            CompositionLocalProvider(LocalUserPreferences provides userPreferencesUiState) {
                RecordCardioExerciseCard(
                    exercise = treadmillExercise,
                    exerciseHistory = CardioExerciseHistoryUiState(),
                    selectExerciseFunction = { },
                    deselectExerciseFunction = { },
                    errorStateChange = { _, _ -> }
                )
            }
        }

        treadmillCheckbox.performClick()

        distanceKilometersChosen.assertExists()
    }

    @Test
    fun recordCardioExerciseCardClickingCheckboxRendersFormFieldsWithMilesUnit() {
        rule.setContent {
            val userPreferencesUiState = UserPreferencesUiState(
                defaultDistanceUnit = DistanceUnits.MILES
            )
            CompositionLocalProvider(LocalUserPreferences provides userPreferencesUiState) {
                RecordCardioExerciseCard(
                    exercise = treadmillExercise,
                    exerciseHistory = CardioExerciseHistoryUiState(),
                    selectExerciseFunction = { },
                    deselectExerciseFunction = { },
                    errorStateChange = { _, _ -> }
                )
            }
        }

        treadmillCheckbox.performClick()

        distanceMilesChosen.assertExists()
    }

    @Test
    fun recordCardioExerciseCardClickingCheckboxRendersFormFieldsWithMetersUnit() {
        rule.setContent {
            val userPreferencesUiState = UserPreferencesUiState(
                defaultDistanceUnit = DistanceUnits.METERS
            )
            CompositionLocalProvider(LocalUserPreferences provides userPreferencesUiState) {
                RecordCardioExerciseCard(
                    exercise = treadmillExercise,
                    exerciseHistory = CardioExerciseHistoryUiState(),
                    selectExerciseFunction = { },
                    deselectExerciseFunction = { },
                    errorStateChange = { _, _ -> }
                )
            }
        }

        treadmillCheckbox.performClick()

        distanceMetersChosen.assertExists()
    }

    @Test
    fun recordCardioExerciseCardClickingCheckboxRendersFormFieldsWithMilesUnitAutoDistanceConvert() {
        rule.setContent {
            val userPreferencesUiState = UserPreferencesUiState(
                defaultDistanceUnit = DistanceUnits.MILES
            )
            CompositionLocalProvider(LocalUserPreferences provides userPreferencesUiState) {
                RecordCardioExerciseCard(
                    exercise = treadmillExercise,
                    exerciseHistory = CardioExerciseHistoryUiState(
                        distance = 10.0
                    ),
                    selectExerciseFunction = { },
                    deselectExerciseFunction = { },
                    errorStateChange = { _, _ -> }
                )
            }
        }

        treadmillCheckbox.performClick()

        rule.onNode(hasText("6.21"))
    }
}