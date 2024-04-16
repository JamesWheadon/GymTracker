package com.askein.gymtracker.ui.workout.details

import androidx.activity.ComponentActivity
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.performClick
import androidx.navigation.NavHostController
import com.askein.gymtracker.ui.exercise.ExerciseUiState
import com.askein.gymtracker.ui.exercise.history.state.WeightsExerciseHistoryUiState
import com.askein.gymtracker.ui.user.LocalUserPreferences
import com.askein.gymtracker.ui.user.UserPreferencesUiState
import com.askein.gymtracker.ui.workout.history.WorkoutHistoryWithExercisesUiState
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
    private val curlsWeightsExerciseHistoryUiState = WeightsExerciseHistoryUiState(0, 0)
    private val dipsWeightsExerciseHistoryUiState = WeightsExerciseHistoryUiState(1, 1)
    private val firstWorkoutHistory = WorkoutHistoryWithExercisesUiState(
        0,
        1,
        LocalDate.now(),
        listOf(curlsWeightsExerciseHistoryUiState, dipsWeightsExerciseHistoryUiState)
    )
    private val secondWorkoutHistory = WorkoutHistoryWithExercisesUiState(
        1,
        1,
        LocalDate.now().minusDays(1),
        listOf(curlsWeightsExerciseHistoryUiState, dipsWeightsExerciseHistoryUiState)
    )
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
    private val editButton = rule.onNode(hasContentDescription("edit feature"))
    private val deleteButton = rule.onNode(hasContentDescription("delete feature"))
    private val editExercisesButton = rule.onNode(hasText("Edit Exercises"))

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
    }

    @Test
    fun rendersWorkoutWithNoExercises() {
        rule.setContent {
            WorkoutDetailsScreen(
                uiState = workoutNoExercises,
                navController = navController,
                exerciseNavigationFunction = { _, _ -> (Unit) },
                updateWorkoutFunction = { },
                deleteWorkoutFunction = { },
                chosenDate = null
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
                exerciseNavigationFunction = { _, _ -> (Unit) },
                updateWorkoutFunction = { },
                deleteWorkoutFunction = { },
                chosenDate = null
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
                exerciseNavigationFunction = { id, _ -> calledExerciseId = id },
                updateWorkoutFunction = { },
                deleteWorkoutFunction = { },
                chosenDate = null
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
                exerciseNavigationFunction = { _, _ -> (Unit) },
                updateWorkoutFunction = { },
                deleteWorkoutFunction = { },
                chosenDate = null
            )
        }

        editButton.performClick()

        rule.onNode(hasText("Update Test Workout Workout")).assertExists()
    }

    @Test
    fun clickingDeleteButtonOpensDeleteWorkoutDialog() {
        rule.setContent {
            WorkoutDetailsScreen(
                uiState = workoutWithExercises,
                navController = navController,
                exerciseNavigationFunction = { _, _ -> (Unit) },
                updateWorkoutFunction = { },
                deleteWorkoutFunction = { },
                chosenDate = null
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
                exerciseNavigationFunction = { _, _ -> (Unit) },
                updateWorkoutFunction = { },
                deleteWorkoutFunction = { },
                chosenDate = null
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
        for (i in 1..LocalDate.now().month.length(LocalDate.now().isLeapYear)) {
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
                exerciseNavigationFunction = { _, _ -> (Unit) },
                updateWorkoutFunction = { },
                deleteWorkoutFunction = { },
                chosenDate = null
            )
        }

        for (i in 1..LocalDate.now().month.length(LocalDate.now().isLeapYear)) {
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
            val userPreferencesUiState = UserPreferencesUiState()
            CompositionLocalProvider(LocalUserPreferences provides userPreferencesUiState) {
                WorkoutDetailsScreen(
                    uiState = workoutWithExercises,
                    navController = navController,
                    exerciseNavigationFunction = { _, _ -> (Unit) },
                    updateWorkoutFunction = { },
                    deleteWorkoutFunction = { },
                    chosenDate = null
                )
            }
        }

        val dayNode = rule.onNode(hasText(dayOfMonth.toString()))
        val closeButton = rule.onNode(hasContentDescription("Close"))
        val workoutsOnDay = rule.onNode(hasText("Workouts on", substring = true))
        dayNode.performClick()

        closeButton.assertExists()
        workoutsOnDay.assertExists()

        closeButton.performClick()

        closeButton.assertDoesNotExist()
        workoutsOnDay.assertDoesNotExist()
    }

    @Test
    fun rendersEditExerciseButtonInWorkoutDetailsScreen() {
        rule.setContent {
            WorkoutDetailsScreen(
                uiState = workoutWithExercises,
                navController = navController,
                exerciseNavigationFunction = { _, _ -> (Unit) },
                updateWorkoutFunction = { },
                deleteWorkoutFunction = { },
                chosenDate = null
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
                exerciseNavigationFunction = { _, _ -> (Unit) },
                updateWorkoutFunction = { },
                deleteWorkoutFunction = { },
                chosenDate = null
            )
        }

        editExercisesButton.performClick()

        rule.onNode(hasText("Workout Exercises"))
    }

    @Test
    fun rendersWorkoutHistoryDetailsWithChosenDate() {
        rule.setContent {
            val userPreferencesUiState = UserPreferencesUiState()
            CompositionLocalProvider(LocalUserPreferences provides userPreferencesUiState) {
                WorkoutDetailsScreen(
                    uiState = workoutWithExercises,
                    navController = navController,
                    exerciseNavigationFunction = { _, _ -> (Unit) },
                    updateWorkoutFunction = { },
                    deleteWorkoutFunction = { },
                    chosenDate = LocalDate.now()
                )
            }
        }

        val closeButton = rule.onNode(hasContentDescription("Close"))
        val workoutsOnDay = rule.onNode(hasText("Workouts on", substring = true))

        closeButton.assertExists()
        workoutsOnDay.assertExists()
    }

    @Test
    fun rendersWorkoutHistoryDetailsWithChosenDateClickingExerciseCallsExerciseNavFunction() {
        var exerciseId: Int? = null
        var exerciseDate: LocalDate? = null

        rule.setContent {
            val userPreferencesUiState = UserPreferencesUiState()
            CompositionLocalProvider(LocalUserPreferences provides userPreferencesUiState) {
                WorkoutDetailsScreen(
                    uiState = workoutWithExercises,
                    navController = navController,
                    exerciseNavigationFunction = { id, date ->
                        exerciseId = id
                        exerciseDate = date
                    },
                    updateWorkoutFunction = { },
                    deleteWorkoutFunction = { },
                    chosenDate = LocalDate.now()
                )
            }
        }

        rule.onAllNodesWithText("Curls")[1].performClick()

        assertThat(exerciseId, equalTo(0))
        assertThat(exerciseDate, equalTo(LocalDate.now()))
    }
}
