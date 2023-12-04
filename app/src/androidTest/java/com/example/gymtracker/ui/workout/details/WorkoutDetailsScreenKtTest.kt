package com.example.gymtracker.ui.workout.details

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.performClick
import androidx.navigation.NavHostController
import com.example.gymtracker.ui.exercise.ExerciseUiState
import com.example.gymtracker.ui.exercise.history.ExerciseHistoryUiState
import com.example.gymtracker.ui.workout.history.WorkoutHistoryWithExercisesUiState
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale

class WorkoutDetailsScreenKtTest {

    @get:Rule
    val rule = createAndroidComposeRule<ComponentActivity>()

    @Mock
    private lateinit var navController: NavHostController

    private val workoutNoExercises = WorkoutWithExercisesUiState(
        workoutId = 1,
        name = "Test Workout"
    )
    private val curlsExerciseUiState = ExerciseUiState(0, "Curls", "Biceps", "Dumbbells")
    private val dipsExerciseUiState = ExerciseUiState(1, "Dips", "Triceps", "Dumbbells And Bars")
    private val curlsExerciseHistoryUiState = ExerciseHistoryUiState(0, 0)
    private val dipsExerciseHistoryUiState = ExerciseHistoryUiState(1, 1)
    private val firstWorkoutHistory = WorkoutHistoryWithExercisesUiState(0, 1, LocalDate.now(), listOf(curlsExerciseHistoryUiState, dipsExerciseHistoryUiState))
    private val secondWorkoutHistory = WorkoutHistoryWithExercisesUiState(1, 1, LocalDate.now().minusDays(1), listOf(curlsExerciseHistoryUiState, dipsExerciseHistoryUiState))
    private val workoutWithExercises = WorkoutWithExercisesUiState(
        workoutId = 1,
        name = "Test Workout",
        exercises = listOf(
            curlsExerciseUiState,
            dipsExerciseUiState
        ),
        workoutHistory = listOf(
            firstWorkoutHistory,
            secondWorkoutHistory
        )
    )

    private val workoutTitle = rule.onNode(hasText("Test Workout"))
    private val curlsExercise = rule.onNode(hasText("Curls"))
    private val dipsExercise = rule.onNode(hasText("Dips"))
    private val editButton = rule.onNode(hasContentDescription("Edit feature"))
    private val deleteButton = rule.onNode(hasContentDescription("Delete feature"))
    private val editExercisesButton = rule.onNode(hasText("Edit Exercises"))

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun rendersWorkoutWithNoExercises() {
        rule.setContent {
            WorkoutDetailsScreen(
                uiState = workoutNoExercises,
                navController = navController,
                exerciseNavigationFunction = { },
                updateWorkoutFunction = { },
                deleteWorkoutFunction = { }
            )
        }

        workoutTitle.assertExists()
    }

    @Test
    fun rendersWorkoutWithExercises() {
        rule.setContent {
            WorkoutDetailsScreen(
                uiState = workoutWithExercises,
                navController = navController,
                exerciseNavigationFunction = { },
                updateWorkoutFunction = { },
                deleteWorkoutFunction = { }
            )
        }

        workoutTitle.assertExists()
        curlsExercise.assertExists()
        dipsExercise.assertExists()
    }

    @Test
    fun clickingExerciseCallsExerciseNavigationFunctionWithId() {
        var calledExerciseId: Int? = null

        rule.setContent {
            WorkoutDetailsScreen(
                uiState = workoutWithExercises,
                navController = navController,
                exerciseNavigationFunction = { id -> calledExerciseId = id },
                updateWorkoutFunction = { },
                deleteWorkoutFunction = { }
            )
        }

        workoutTitle.assertExists()
        curlsExercise.assertExists()
        dipsExercise.assertExists()

        curlsExercise.performClick()

        assertThat(calledExerciseId, equalTo(0))
    }

    @Test
    fun clickingEditButtonOpensUpdateWorkoutDialog() {
        rule.setContent {
            WorkoutDetailsScreen(
                uiState = workoutWithExercises,
                navController = navController,
                exerciseNavigationFunction = { },
                updateWorkoutFunction = { },
                deleteWorkoutFunction = { }
            )
        }

        editButton.performClick()

        rule.onNode(hasText("Update Workout")).assertExists()
    }

    @Test
    fun clickingDeleteButtonOpensDeleteWorkoutDialog() {
        rule.setContent {
            WorkoutDetailsScreen(
                uiState = workoutWithExercises,
                navController = navController,
                exerciseNavigationFunction = { },
                updateWorkoutFunction = { },
                deleteWorkoutFunction = { }
            )
        }

        deleteButton.performClick()

        rule.onNode(hasText("Delete Test Workout Workout?")).assertExists()
    }

    @Test
    fun rendersMonthPickerAndCalendar() {
        rule.setContent {
            WorkoutDetailsScreen(
                uiState = workoutWithExercises,
                navController = navController,
                exerciseNavigationFunction = { },
                updateWorkoutFunction = { },
                deleteWorkoutFunction = { }
            )
        }

        rule.onNode(
            hasText(
                "${
                    LocalDate.now().month.getDisplayName(
                        TextStyle.FULL_STANDALONE,
                        Locale.ENGLISH
                    )
                } ${LocalDate.now().year}"
            )
        ).assertExists()
        for (i in 1 ..LocalDate.now().month.length(LocalDate.now().isLeapYear)) {
            rule.onNode(hasText(i.toString())).assertExists()
        }
    }

    @Test
    fun highlightsCalendarDaysThatMatchWorkoutHistory() {
        val dayOfMonth = LocalDate.now().dayOfMonth

        rule.setContent {
            WorkoutDetailsScreen(
                uiState = workoutWithExercises,
                navController = navController,
                exerciseNavigationFunction = { },
                updateWorkoutFunction = { },
                deleteWorkoutFunction = { }
            )
        }

        for (i in 1 ..LocalDate.now().month.length(LocalDate.now().isLeapYear)) {
            val dayNode = rule.onNode(hasText(i.toString()))
            dayNode.assertExists()
            if (i == dayOfMonth || (i + 1) == dayOfMonth) {
                dayNode.assertIsEnabled()
            } else {
                dayNode.assertIsNotEnabled()
            }
        }
    }

    @Test
    fun clickingHighlightedCalendarDayRendersWorkoutHistoryDetails() {
        val dayOfMonth = LocalDate.now().dayOfMonth

        rule.setContent {
            WorkoutDetailsScreen(
                uiState = workoutWithExercises,
                navController = navController,
                exerciseNavigationFunction = { },
                updateWorkoutFunction = { },
                deleteWorkoutFunction = { }
            )
        }

        val dayNode = rule.onNode(hasText(dayOfMonth.toString()))
        val closeButton = rule.onNode(hasContentDescription("Close"))
        dayNode.performClick()

        closeButton.assertExists()

        closeButton.performClick()

        closeButton.assertDoesNotExist()
    }

    @Test
    fun rendersEditExerciseButtonInWorkoutDetailsScreen() {
        rule.setContent {
            WorkoutDetailsScreen(
                uiState = workoutWithExercises,
                navController = navController,
                exerciseNavigationFunction = { },
                updateWorkoutFunction = { },
                deleteWorkoutFunction = { }
            )
        }

        editExercisesButton.assertExists()
    }

    @Test
    fun clickingEditExerciseButtonOpensEditWorkoutExercisesScreen() {
        rule.setContent {
            WorkoutDetailsScreen(
                uiState = workoutWithExercises,
                navController = navController,
                exerciseNavigationFunction = { },
                updateWorkoutFunction = { },
                deleteWorkoutFunction = { }
            )
        }

        editExercisesButton.performClick()

        rule.onNode(hasText("Workout Exercises"))
    }
}
