package com.example.gymtracker.ui.exercise.details

import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.unit.dp
import com.example.gymtracker.enums.DistanceUnits
import com.example.gymtracker.ui.exercise.ExerciseUiState
import com.example.gymtracker.ui.exercise.history.state.CardioExerciseHistoryUiState
import com.example.gymtracker.ui.user.LocalUserPreferences
import com.example.gymtracker.ui.user.UserPreferencesUiState
import org.junit.Rule
import org.junit.Test


private const val NAME = "Treadmill"

class CardioExerciseDetailsScreenKtTest {

    @get:Rule
    val rule = createAndroidComposeRule<ComponentActivity>()

    private val exerciseNoHistory = ExerciseDetailsUiState(
        exercise = ExerciseUiState(
            name = NAME
        )
    )
    private val exerciseSecondsBest = ExerciseDetailsUiState(
        exercise = ExerciseUiState(
            name = NAME
        ),
        cardioHistory = listOf(
            CardioExerciseHistoryUiState(
                id = 1,
                distance = 10.0,
                minutes = 0,
                seconds = 30,
                calories = 450
            ),
            CardioExerciseHistoryUiState(
                id = 2,
                distance = 20.0,
                calories = 300
            )
        )
    )
    private val exerciseMinutesBest = ExerciseDetailsUiState(
        exercise = ExerciseUiState(
            name = NAME
        ),
        cardioHistory = listOf(
            CardioExerciseHistoryUiState(
                id = 1,
                distance = 10.0,
                minutes = 10,
                seconds = 0,
                calories = 450
            ),
            CardioExerciseHistoryUiState(
                id = 2,
                distance = 20.0,
                minutes = 20,
                seconds = 0,
                calories = 300
            )
        )
    )
    private val exerciseHoursBest = ExerciseDetailsUiState(
        exercise = ExerciseUiState(
            name = NAME
        ),
        cardioHistory = listOf(
            CardioExerciseHistoryUiState(
                id = 1,
                distance = 10.0,
                minutes = 10,
                seconds = 0,
                calories = 450
            ),
            CardioExerciseHistoryUiState(
                id = 2,
                distance = 20.0,
                minutes = 90,
                seconds = 0,
                calories = 300
            )
        )
    )

    private val cardioIcon = rule.onNode(hasContentDescription("cardio icon"))
    private val bestExerciseIcon = rule.onAllNodes(hasContentDescription("best exercise icon"))
    private val bestDistance = rule.onNode(hasText("20.00 km"))
    private val bestSecondsTime = rule.onNode(hasText("30 s"))
    private val bestMinutesTime = rule.onNode(hasText("20:00"))
    private val bestHoursTime = rule.onNode(hasText("1:30:00"))
    private val bestCalories = rule.onNode(hasText("450 kcal"))
    private val bestDistanceMiles = rule.onNode(hasText("12.43 mi"))
    private val quickestHoursTime = rule.onNode(hasText("10:00"))

    @Test
    fun rendersExerciseDetailsWithNoHistory() {
        rule.setContent {
            CardioExerciseDetailsScreen(
                uiState = exerciseNoHistory,
                innerPadding = PaddingValues(0.dp)
            )
        }

        cardioIcon.assertExists()
        bestExerciseIcon.assertCountEquals(0)
    }

    @Test
    fun rendersExerciseDetailsWithSecondsHistory() {
        rule.setContent {
            val userPreferencesUiState = UserPreferencesUiState()
            CompositionLocalProvider(LocalUserPreferences provides userPreferencesUiState) {
                CardioExerciseDetailsScreen(
                    uiState = exerciseSecondsBest,
                    innerPadding = PaddingValues(0.dp)
                )
            }
        }

        cardioIcon.assertExists()
        bestExerciseIcon.assertCountEquals(3)
        bestDistance.assertExists()
        bestSecondsTime.assertExists()
        bestCalories.assertExists()
    }

    @Test
    fun rendersExerciseDetailsWithMinutesHistory() {
        rule.setContent {
            val userPreferencesUiState = UserPreferencesUiState()
            CompositionLocalProvider(LocalUserPreferences provides userPreferencesUiState) {
                CardioExerciseDetailsScreen(
                    uiState = exerciseMinutesBest,
                    innerPadding = PaddingValues(0.dp)
                )
            }
        }

        cardioIcon.assertExists()
        bestExerciseIcon.assertCountEquals(3)
        bestDistance.assertExists()
        bestMinutesTime.assertExists()
        bestCalories.assertExists()
    }

    @Test
    fun rendersExerciseDetailsWithHoursHistory() {
        rule.setContent {
            val userPreferencesUiState = UserPreferencesUiState()
            CompositionLocalProvider(LocalUserPreferences provides userPreferencesUiState) {
                CardioExerciseDetailsScreen(
                    uiState = exerciseHoursBest,
                    innerPadding = PaddingValues(0.dp)
                )
            }
        }

        cardioIcon.assertExists()
        bestExerciseIcon.assertCountEquals(3)
        bestDistance.assertExists()
        bestHoursTime.assertExists()
        bestCalories.assertExists()
    }

    @Test
    fun rendersExerciseDetailsWithShortestTimeAndMilesDistance() {
        rule.setContent {
            val userPreferencesUiState = UserPreferencesUiState(
                displayShortestTime = true,
                defaultDistanceUnit = DistanceUnits.MILES
            )
            CompositionLocalProvider(LocalUserPreferences provides userPreferencesUiState) {
                CardioExerciseDetailsScreen(
                    uiState = exerciseHoursBest,
                    innerPadding = PaddingValues(0.dp)
                )
            }
        }

        cardioIcon.assertExists()
        bestExerciseIcon.assertCountEquals(3)
        bestDistanceMiles.assertExists()
        quickestHoursTime.assertExists()
        bestCalories.assertExists()
    }
}
