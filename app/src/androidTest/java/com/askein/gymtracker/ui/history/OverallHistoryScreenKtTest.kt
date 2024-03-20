package com.askein.gymtracker.ui.history

import androidx.activity.ComponentActivity
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.performClick
import com.askein.gymtracker.ui.exercise.ExerciseUiState
import com.askein.gymtracker.ui.workout.WorkoutUiState
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Rule
import org.junit.Test
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.time.format.TextStyle
import java.util.Locale

class OverallHistoryScreenKtTest {

    @get:Rule
    val rule = createAndroidComposeRule<ComponentActivity>()

    private val armsWorkout = WorkoutUiState(workoutId = 1, name = "Arms")
    private val legsWorkout = WorkoutUiState(workoutId = 2, name = "Legs")
    private val treadmillExercise = ExerciseUiState(id = 3, name = "Treadmill")
    private val benchExercise = ExerciseUiState(id = 4, name = "Bench", muscleGroup = "Chest", equipment = "Bench")

    private val date = rule.onNode(hasText(LocalDate.now().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG))))
    private val workouts = rule.onNode(hasText("Workouts"))
    private val exercises = rule.onNode(hasText("Exercises"))
    private val armsWorkoutName = rule.onNode(hasText("Arms"))
    private val legsWorkoutName = rule.onNode(hasText("Legs"))
    private val treadmillExerciseName = rule.onNode(hasText("Treadmill"))
    private val benchExerciseName = rule.onNode(hasText("Bench"))
    private val closeButton = rule.onNode(hasContentDescription("Close"))
    private val month = rule.onNode(hasText("${LocalDate.now().month.getDisplayName(TextStyle.FULL_STANDALONE, Locale.ENGLISH)} ${LocalDate.now().year}"))
    private val dayButton = rule.onNode(hasText(LocalDate.now().dayOfMonth.toString()))

    @Test
    fun rendersHistoryOnDay() {
        rule.setContent {
            HistoryOnDay(
                date = LocalDate.now(),
                workoutNavigationFunction = { },
                exerciseNavigationFunction = { },
                workoutsOnDateUiState = listOf(armsWorkout, legsWorkout),
                exercisesOnDateUiState = listOf(treadmillExercise, benchExercise),
                onDismiss = { }
            )
        }

        date.assertExists()
        workouts.assertExists()
        exercises.assertExists()
        armsWorkoutName.assertExists()
        legsWorkoutName.assertExists()
        treadmillExerciseName.assertExists()
        benchExerciseName.assertExists()
        closeButton.assertExists()
    }

    @Test
    fun historyOnDayClickingWorkoutCallsWorkoutNavigationFunction() {
        var selected = 0

        rule.setContent {
            HistoryOnDay(
                date = LocalDate.now(),
                workoutNavigationFunction = { id -> selected = id },
                exerciseNavigationFunction = { },
                workoutsOnDateUiState = listOf(armsWorkout, legsWorkout),
                exercisesOnDateUiState = listOf(treadmillExercise, benchExercise),
                onDismiss = { }
            )
        }

        armsWorkoutName.performClick()

        assertThat(selected, equalTo(1))
    }

    @Test
    fun historyOnDayClickingExerciseCallsExerciseNavigationFunction() {
        var selected = 0

        rule.setContent {
            HistoryOnDay(
                date = LocalDate.now(),
                workoutNavigationFunction = { },
                exerciseNavigationFunction = { id -> selected = id },
                workoutsOnDateUiState = listOf(armsWorkout, legsWorkout),
                exercisesOnDateUiState = listOf(treadmillExercise, benchExercise),
                onDismiss = { }
            )
        }

        treadmillExerciseName.performClick()

        assertThat(selected, equalTo(3))
    }

    @Test
    fun historyOnDayClickingCloseButtonCallsOnDismiss() {
        var dismissed = false

        rule.setContent {
            HistoryOnDay(
                date = LocalDate.now(),
                workoutNavigationFunction = { },
                exerciseNavigationFunction = { },
                workoutsOnDateUiState = listOf(armsWorkout, legsWorkout),
                exercisesOnDateUiState = listOf(treadmillExercise, benchExercise),
                onDismiss = { dismissed = true }
            )
        }

        closeButton.performClick()

        assertThat(dismissed, equalTo(true))
    }

    @Test
    fun rendersOverallHistoryScreen() {
        rule.setContent {
            var workouts: List<WorkoutUiState> by remember { mutableStateOf(listOf()) }
            var exercises: List<ExerciseUiState> by remember { mutableStateOf(listOf()) }
            OverallHistoryScreen(
                workoutNavigationFunction = { },
                exerciseNavigationFunction = { },
                datesUiState = listOf(LocalDate.now()),
                workoutsOnDateUiState = workouts,
                exercisesOnDateUiState = exercises,
                dateSelector = {
                    workouts = listOf(armsWorkout, legsWorkout)
                    exercises = listOf(treadmillExercise, benchExercise)
                }
            )
        }

        month.assertExists()
        dayButton.assertExists()
        dayButton.assertIsEnabled()
        for (day in 1.. LocalDate.now().month.length(LocalDate.now().isLeapYear)) {
            rule.onNode(hasText("$day")).assertExists()
        }
    }

    @Test
    fun overallHistoryScreenClickingDayRendersHistoryOnDay() {
        rule.setContent {
            var workouts: List<WorkoutUiState> by remember { mutableStateOf(listOf()) }
            var exercises: List<ExerciseUiState> by remember { mutableStateOf(listOf()) }
            OverallHistoryScreen(
                workoutNavigationFunction = { },
                exerciseNavigationFunction = { },
                datesUiState = listOf(LocalDate.now()),
                workoutsOnDateUiState = workouts,
                exercisesOnDateUiState = exercises,
                dateSelector = {
                    workouts = listOf(armsWorkout, legsWorkout)
                    exercises = listOf(treadmillExercise, benchExercise)
                }
            )
        }

        dayButton.performClick()

        date.assertExists()
        workouts.assertExists()
        exercises.assertExists()
        armsWorkoutName.assertExists()
        legsWorkoutName.assertExists()
        treadmillExerciseName.assertExists()
        benchExerciseName.assertExists()
        closeButton.assertExists()
    }
}
