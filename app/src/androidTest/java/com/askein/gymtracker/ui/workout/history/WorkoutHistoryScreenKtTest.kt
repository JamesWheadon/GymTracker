package com.askein.gymtracker.ui.workout.history

import androidx.activity.ComponentActivity
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.performClick
import com.askein.gymtracker.ui.exercise.ExerciseUiState
import com.askein.gymtracker.ui.exercise.history.state.WeightsExerciseHistoryUiState
import com.askein.gymtracker.ui.user.LocalUserPreferences
import com.askein.gymtracker.ui.user.UserPreferencesUiState
import com.askein.gymtracker.ui.workout.details.WorkoutWithExercisesUiState
import org.junit.Rule
import org.junit.Test
import java.time.LocalDate

class WorkoutHistoryScreenKtTest {

    @get:Rule
    val rule = createAndroidComposeRule<ComponentActivity>()

    private val curls = ExerciseUiState(1, "Curls", "Biceps", "Dumbbells")
    private val dips = ExerciseUiState(2, "Dips", "Triceps", "Bars")
    private val bench = ExerciseUiState(3, "Bench", "Chest", "Barbell")
    private val curlsHistory = WeightsExerciseHistoryUiState(
        id = 1,
        exerciseId = 1,
        date = LocalDate.now(),
        workoutHistoryId = 1,
        weight = listOf(1.0),
        sets = 1,
        reps = listOf(1),
        rest = 1
    )
    private val benchHistory = WeightsExerciseHistoryUiState(
        id = 1,
        exerciseId = 3,
        date = LocalDate.now(),
        workoutHistoryId = 1,
        weight = listOf(1.0),
        sets = 1,
        reps = listOf(1),
        rest = 1
    )
    private val workoutHistory = WorkoutHistoryWithExercisesUiState(1, 1, LocalDate.now(), listOf(curlsHistory, benchHistory))

    private val curlsExerciseName = rule.onNode(hasText("Curls"))
    private val dipsExerciseName = rule.onNode(hasText("Dips"))
    private val benchExerciseName = rule.onNode(hasText("Bench"))
    private val setInfo = rule.onNode(hasText("Set 1: 1 Rep of 1.00 kg"))
    private val rest = rule.onNode(hasText("Rest time: 1 s"))
    private val deleteHistoryButton = rule.onNode(hasContentDescription("Delete history"))
    private val editButton = rule.onNode(hasContentDescription("edit"))
    private val deleteButton = rule.onNode(hasContentDescription("delete"))

    @Test
    fun rendersWorkoutHistoryExerciseCard() {
        rule.setContent {
            val userPreferencesUiState = UserPreferencesUiState()
            CompositionLocalProvider(LocalUserPreferences provides userPreferencesUiState) {
                WorkoutHistoryExerciseCard(
                    exercise = curls,
                    exerciseHistory = curlsHistory,
                    exerciseNavigationFunction = { _, _ -> (Unit) },
                    chosenDate = LocalDate.now()
                )
            }
        }

        curlsExerciseName.assertExists()
        setInfo.assertExists()
        rest.assertExists()
        deleteHistoryButton.assertDoesNotExist()
    }

    @Test
    fun rendersWorkoutHistoryScreen() {
        rule.setContent {
            val userPreferencesUiState = UserPreferencesUiState()
            CompositionLocalProvider(LocalUserPreferences provides userPreferencesUiState) {
                WorkoutHistoryScreen(
                    workoutHistoryUiState = workoutHistory,
                    workoutUiState = WorkoutWithExercisesUiState(
                        exercises = listOf(
                            curls,
                            dips,
                            bench
                        )
                    ),
                    exerciseNavigationFunction = { _, _ -> (Unit) },
                    chosenDate = LocalDate.now()
                )
            }
        }

        curlsExerciseName.assertExists()
        dipsExerciseName.assertDoesNotExist()
        benchExerciseName.assertExists()
    }

    @Test
    fun clickingEditRendersRecordWorkoutHistoryScreenWithAlternateTitle() {
        rule.setContent {
            val userPreferencesUiState = UserPreferencesUiState()
            CompositionLocalProvider(LocalUserPreferences provides userPreferencesUiState) {
                WorkoutHistoryScreen(
                    workoutHistoryUiState = workoutHistory,
                    workoutUiState = WorkoutWithExercisesUiState(
                        name = "a",
                        exercises = listOf(
                            curls,
                            dips,
                            bench
                        )
                    ),
                    exerciseNavigationFunction = { _, _ -> (Unit) },
                    chosenDate = LocalDate.now()
                )
            }
        }

        editButton.performClick()

        rule.onNode(hasText("Update a Workout")).assertExists()
    }

    @Test
    fun clickingDeleteOpensActionConfirmation() {
        rule.setContent {
            val userPreferencesUiState = UserPreferencesUiState()
            CompositionLocalProvider(LocalUserPreferences provides userPreferencesUiState) {
                WorkoutHistoryScreen(
                    workoutHistoryUiState = workoutHistory,
                    workoutUiState = WorkoutWithExercisesUiState(
                        exercises = listOf(
                            curls,
                            dips,
                            bench
                        )
                    ),
                    exerciseNavigationFunction = { _, _ -> (Unit) },
                    chosenDate = LocalDate.now()
                )
            }
        }

        deleteButton.performClick()

        rule.onNode(hasText("Do you want to delete this workout?")).assertExists()
    }
}
