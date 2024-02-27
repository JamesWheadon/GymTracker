package com.example.gymtracker.ui.workout.history

import androidx.activity.ComponentActivity
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.performClick
import com.example.gymtracker.ui.exercise.ExerciseUiState
import com.example.gymtracker.ui.exercise.history.state.WeightsExerciseHistoryUiState
import com.example.gymtracker.ui.user.LocalUserPreferences
import com.example.gymtracker.ui.user.UserPreferencesUiState
import com.example.gymtracker.ui.workout.details.WorkoutWithExercisesUiState
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
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
        weight = 1.0,
        sets = 1,
        reps = 1,
        rest = 1
    )
    private val benchHistory = WeightsExerciseHistoryUiState(
        id = 1,
        exerciseId = 3,
        date = LocalDate.now(),
        workoutHistoryId = 1,
        weight = 1.0,
        sets = 1,
        reps = 1,
        rest = 1
    )
    private val workoutHistory = WorkoutHistoryWithExercisesUiState(1, 1, LocalDate.now(), listOf(curlsHistory, benchHistory))

    private val curlsExerciseName = rule.onNode(hasText("Curls"))
    private val dipsExerciseName = rule.onNode(hasText("Dips"))
    private val benchExerciseName = rule.onNode(hasText("Bench"))
    private val sets = rule.onNode(hasText("Sets: 1"))
    private val reps = rule.onNode(hasText("Reps: 1"))
    private val weight = rule.onNode(hasText("Weight: 1.0kg"))
    private val rest = rule.onNode(hasText("Rest time: 1"))
    private val deleteHistoryButton = rule.onNode(hasContentDescription("Delete history"))
    private val closeButton = rule.onNode(hasContentDescription("Close"))
    private val editButton = rule.onNode(hasContentDescription("Edit"))
    private val deleteButton = rule.onNode(hasContentDescription("Delete"))

    @Test
    fun rendersWorkoutHistoryExerciseCard() {
        rule.setContent {
            val userPreferencesUiState = UserPreferencesUiState()
            CompositionLocalProvider(LocalUserPreferences provides userPreferencesUiState) {
                WorkoutHistoryExerciseCard(
                    exercise = curls,
                    exerciseHistory = curlsHistory
                )
            }
        }

        curlsExerciseName.assertExists()
        sets.assertExists()
        reps.assertExists()
        weight.assertExists()
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
                    onDismiss = { }
                )
            }
        }

        curlsExerciseName.assertExists()
        dipsExerciseName.assertDoesNotExist()
        benchExerciseName.assertExists()
        closeButton.assertExists()
    }

    @Test
    fun clickingXCallsOnDismiss() {
        var dismissed = false
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
                    onDismiss = { dismissed = true }
                )
            }
        }

        closeButton.performClick()

        assertThat(dismissed, equalTo(true))
    }

    @Test
    fun clickingEditRendersRecordWorkoutHistoryScreenWithAlternateTitle() {
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
                    onDismiss = { }
                )
            }
        }

        editButton.performClick()

        rule.onNode(hasText("Update Workout")).assertExists()
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
                    onDismiss = { }
                )
            }
        }

        deleteButton.performClick()

        rule.onNode(hasText("Do you want to delete this workout?")).assertExists()
    }
}
