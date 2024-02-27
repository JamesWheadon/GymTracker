package com.example.gymtracker.ui.exercise.history

import androidx.activity.ComponentActivity
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import com.example.gymtracker.converters.DistanceUnits
import com.example.gymtracker.ui.exercise.history.state.CardioExerciseHistoryUiState
import com.example.gymtracker.ui.user.LocalUserPreferences
import com.example.gymtracker.ui.user.UserPreferencesUiState
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Rule
import org.junit.Test

class RecordCardioExerciseHistoryScreenKtTest {

    @get:Rule
    val rule = createAndroidComposeRule<ComponentActivity>()

    private val cardTitleText = "Record Cardio"
    private val cardTitle = rule.onNode(hasText(cardTitleText))
    private val minuteField = rule.onNode(hasContentDescription("Minutes"))
    private val secondsField = rule.onNode(hasContentDescription("Seconds"))
    private val distanceField = rule.onNode(hasContentDescription("Distance"))
    private val caloriesField = rule.onNode(hasContentDescription("Calories"))
    private val unitsField = rule.onNode(hasContentDescription("Units"))
    private val distanceKilometersChosen = rule.onNode(hasText("km"))
    private val distanceMilesChosen = rule.onNode(hasText("mi"))
    private val distanceMetersChosen = rule.onNode(hasText("m"))
    private val saveButton = rule.onNode(hasText("Save"))

    @Test
    fun rendersRecordCardioExerciseHistoryCard() {
        rule.setContent {
            val userPreferencesUiState = UserPreferencesUiState()
            CompositionLocalProvider(LocalUserPreferences provides userPreferencesUiState) {
                RecordCardioExerciseHistoryCard(
                    exerciseId = 0,
                    cardTitle = cardTitleText,
                    saveFunction = { },
                    onDismiss = { }
                )
            }
        }

        cardTitle.assertExists()
        minuteField.assertExists()
        secondsField.assertExists()
        distanceField.assertExists()
        unitsField.assertExists()
        caloriesField.assertExists()
        saveButton.assertExists()
    }

    @Test
    fun saveButtonNotEnabledWithAllEmptyFields() {
        rule.setContent {
            val userPreferencesUiState = UserPreferencesUiState()
            CompositionLocalProvider(LocalUserPreferences provides userPreferencesUiState) {
                RecordCardioExerciseHistoryCard(
                    exerciseId = 0,
                    cardTitle = cardTitleText,
                    saveFunction = { },
                    onDismiss = { }
                )
            }
        }

        saveButton.assertIsNotEnabled()
    }

    @Test
    fun saveButtonNotEnabledWithOnlyMinutes() {
        rule.setContent {
            val userPreferencesUiState = UserPreferencesUiState()
            CompositionLocalProvider(LocalUserPreferences provides userPreferencesUiState) {
                RecordCardioExerciseHistoryCard(
                    exerciseId = 0,
                    cardTitle = cardTitleText,
                    saveFunction = { },
                    onDismiss = { }
                )
            }
        }

        minuteField.performTextInput("60")
        saveButton.assertIsNotEnabled()
    }

    @Test
    fun saveButtonNotEnabledWithOnlySeconds() {
        rule.setContent {
            val userPreferencesUiState = UserPreferencesUiState()
            CompositionLocalProvider(LocalUserPreferences provides userPreferencesUiState) {
                RecordCardioExerciseHistoryCard(
                    exerciseId = 0,
                    cardTitle = cardTitleText,
                    saveFunction = { },
                    onDismiss = { }
                )
            }
        }

        secondsField.performTextInput("59")
        saveButton.assertIsNotEnabled()
    }

    @Test
    fun saveButtonEnabledWithMinutesAndSeconds() {
        rule.setContent {
            val userPreferencesUiState = UserPreferencesUiState()
            CompositionLocalProvider(LocalUserPreferences provides userPreferencesUiState) {
                RecordCardioExerciseHistoryCard(
                    exerciseId = 0,
                    cardTitle = cardTitleText,
                    saveFunction = { },
                    onDismiss = { }
                )
            }
        }

        minuteField.performTextInput("60")
        secondsField.performTextInput("59")
        saveButton.assertIsEnabled()
    }

    @Test
    fun saveButtonNotEnabledWithSecondsError() {
        rule.setContent {
            val userPreferencesUiState = UserPreferencesUiState()
            CompositionLocalProvider(LocalUserPreferences provides userPreferencesUiState) {
                RecordCardioExerciseHistoryCard(
                    exerciseId = 0,
                    cardTitle = cardTitleText,
                    saveFunction = { },
                    onDismiss = { }
                )
            }
        }

        minuteField.performTextInput("60")
        secondsField.performTextInput("60")
        saveButton.assertIsNotEnabled()
    }

    @Test
    fun saveButtonEnabledWithDistance() {
        rule.setContent {
            val userPreferencesUiState = UserPreferencesUiState()
            CompositionLocalProvider(LocalUserPreferences provides userPreferencesUiState) {
                RecordCardioExerciseHistoryCard(
                    exerciseId = 0,
                    cardTitle = cardTitleText,
                    saveFunction = { },
                    onDismiss = { }
                )
            }
        }

        distanceField.performTextInput("500")
        saveButton.assertIsEnabled()
    }

    @Test
    fun saveButtonEnabledWithCalories() {
        rule.setContent {
            val userPreferencesUiState = UserPreferencesUiState()
            CompositionLocalProvider(LocalUserPreferences provides userPreferencesUiState) {
                RecordCardioExerciseHistoryCard(
                    exerciseId = 0,
                    cardTitle = cardTitleText,
                    saveFunction = { },
                    onDismiss = { }
                )
            }
        }

        caloriesField.performTextInput("500")
        saveButton.assertIsEnabled()
    }

    @Test
    fun saveButtonEnabledWithAllFields() {
        rule.setContent {
            val userPreferencesUiState = UserPreferencesUiState()
            CompositionLocalProvider(LocalUserPreferences provides userPreferencesUiState) {
                RecordCardioExerciseHistoryCard(
                    exerciseId = 0,
                    cardTitle = cardTitleText,
                    saveFunction = { },
                    onDismiss = { }
                )
            }
        }

        minuteField.performTextInput("60")
        secondsField.performTextInput("59")
        distanceField.performTextInput("500")
        caloriesField.performTextInput("500")
        saveButton.assertIsEnabled()
    }

    @Test
    fun clickingSaveButtonCallsSaveFunctionWithHistoryAndDismissFunction() {
        var created: CardioExerciseHistoryUiState? = null
        var dismissed = false
        rule.setContent {
            val userPreferencesUiState = UserPreferencesUiState()
            CompositionLocalProvider(LocalUserPreferences provides userPreferencesUiState) {
                RecordCardioExerciseHistoryCard(
                    exerciseId = 0,
                    cardTitle = cardTitleText,
                    saveFunction = { created = it as CardioExerciseHistoryUiState },
                    onDismiss = { dismissed = true }
                )
            }
        }

        minuteField.performTextInput("60")
        secondsField.performTextInput("59")
        caloriesField.performTextInput("500")
        saveButton.performClick()

        assertThat(created!!.exerciseId, equalTo(0))
        assertThat(created!!.minutes, equalTo(60))
        assertThat(created!!.seconds, equalTo(59))
        assertThat(created!!.distance, equalTo(null))
        assertThat(created!!.calories, equalTo(500))
        assertThat(dismissed, equalTo(true))
    }

    @Test
    fun populatesFormFieldsIfUIStateGiven() {
        rule.setContent {
            val userPreferencesUiState = UserPreferencesUiState()
            CompositionLocalProvider(LocalUserPreferences provides userPreferencesUiState) {
                RecordCardioExerciseHistoryCard(
                    exerciseId = 0,
                    cardTitle = cardTitleText,
                    saveFunction = { },
                    onDismiss = { },
                    savedHistory = CardioExerciseHistoryUiState(distance = 500.0)
                )
            }
        }

        distanceField.assertTextContains("500.0")
    }

    @Test
    fun rendersRecordCardioExerciseHistoryCardWithKilometersUnit() {
        rule.setContent {
            val userPreferencesUiState = UserPreferencesUiState(
                defaultDistanceUnit = DistanceUnits.KILOMETERS
            )
            CompositionLocalProvider(LocalUserPreferences provides userPreferencesUiState) {
                RecordCardioExerciseHistoryCard(
                    exerciseId = 0,
                    cardTitle = cardTitleText,
                    saveFunction = { },
                    onDismiss = { }
                )
            }
        }

        distanceKilometersChosen.assertExists()
    }

    @Test
    fun rendersRecordCardioExerciseHistoryCardWithMilesUnit() {
        rule.setContent {
            val userPreferencesUiState = UserPreferencesUiState(
                defaultDistanceUnit = DistanceUnits.MILES
            )
            CompositionLocalProvider(LocalUserPreferences provides userPreferencesUiState) {
                RecordCardioExerciseHistoryCard(
                    exerciseId = 0,
                    cardTitle = cardTitleText,
                    saveFunction = { },
                    onDismiss = { }
                )
            }
        }

        distanceMilesChosen.assertExists()
    }

    @Test
    fun rendersRecordCardioExerciseHistoryCardWithMetersUnit() {
        rule.setContent {
            val userPreferencesUiState = UserPreferencesUiState(
                defaultDistanceUnit = DistanceUnits.METERS
            )
            CompositionLocalProvider(LocalUserPreferences provides userPreferencesUiState) {
                RecordCardioExerciseHistoryCard(
                    exerciseId = 0,
                    cardTitle = cardTitleText,
                    saveFunction = { },
                    onDismiss = { }
                )
            }
        }

        distanceMetersChosen.assertExists()
    }

    @Test
    fun rendersRecordCardioExerciseHistoryCardWithMilesUnitAutoDistanceConvert() {
        rule.setContent {
            val userPreferencesUiState = UserPreferencesUiState(
                defaultDistanceUnit = DistanceUnits.MILES
            )
            CompositionLocalProvider(LocalUserPreferences provides userPreferencesUiState) {
                RecordCardioExerciseHistoryCard(
                    exerciseId = 0,
                    cardTitle = cardTitleText,
                    saveFunction = { },
                    onDismiss = { },
                    savedHistory = CardioExerciseHistoryUiState(
                        distance = 10.0
                    )
                )
            }
        }

        rule.onNode(hasText("6.21"))
    }
}
