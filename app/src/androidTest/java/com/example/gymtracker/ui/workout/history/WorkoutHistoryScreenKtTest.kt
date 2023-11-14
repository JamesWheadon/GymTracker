package com.example.gymtracker.ui.workout.history

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.performClick
import com.example.gymtracker.ui.exercise.ExerciseUiState
import com.example.gymtracker.ui.exercise.history.ExerciseHistoryUiState
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
    private val curlsHistory = ExerciseHistoryUiState(1, 1, 1.0, 1, 1, 1, LocalDate.now())
    private val benchHistory = ExerciseHistoryUiState(1, 3, 1.0, 1, 1, 1, LocalDate.now())
    private val workoutHistory = WorkoutHistoryUiState(1, 1, LocalDate.now(), listOf(curlsHistory, benchHistory))

    private val curlsExerciseName = rule.onNode(hasText("Curls"))
    private val dipsExerciseName = rule.onNode(hasText("Dips"))
    private val benchExerciseName = rule.onNode(hasText("Bench"))
    private val sets = rule.onNode(hasText("Sets: 1"))
    private val reps = rule.onNode(hasText("Reps: 1"))
    private val weight = rule.onNode(hasText("Weight: 1.0kg"))
    private val rest = rule.onNode(hasText("Rest time: 1"))
    private val deleteHistoryButton = rule.onNode(hasContentDescription("Delete history"))
    private val closeButton = rule.onNode(hasContentDescription("Close"))

    @Test
    fun rendersWorkoutHistoryExerciseCard() {
        rule.setContent {
            WorkoutHistoryExerciseCard(
                exercise = curls,
                exerciseHistory = curlsHistory
            )
        }

        curlsExerciseName.assertExists()
        sets.assertExists()
        reps.assertExists()
        weight.assertExists()
        rest.assertExists()
        deleteHistoryButton.assertExists()
    }

    @Test
    fun rendersWorkoutHistoryScreen() {
        rule.setContent {
            WorkoutHistoryScreen(
                uiState = workoutHistory,
                exercises = listOf(curls, dips, bench),
                onDismiss = { }
            )
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
            WorkoutHistoryScreen(
                uiState = workoutHistory,
                exercises = listOf(curls, dips, bench),
                onDismiss = { dismissed = true }
            )
        }

        closeButton.performClick()

        assertThat(dismissed, equalTo(true))
    }
}