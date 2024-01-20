package com.example.gymtracker.ui.workout.history.create.live

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.hasAnySibling
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.navigation.NavHostController
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import com.example.gymtracker.data.exerciseHistory.ExerciseHistoryRepository
import com.example.gymtracker.data.workout.WorkoutRepository
import com.example.gymtracker.data.workoutExerciseCrossRef.WorkoutExerciseCrossRefRepository
import com.example.gymtracker.data.workoutHistory.WorkoutHistoryRepository
import com.example.gymtracker.ui.exercise.ExerciseUiState
import com.example.gymtracker.ui.workout.details.WorkoutWithExercisesUiState
import org.hamcrest.CoreMatchers.equalTo
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class LiveRecordWorkoutKtTest {

    @Mock
    private lateinit var navController: NavHostController
    @Mock
    private lateinit var workoutHistoryRepository: WorkoutHistoryRepository
    @Mock
    private lateinit var exerciseHistoryRepository: ExerciseHistoryRepository
    @Mock
    private lateinit var workoutRepository: WorkoutRepository
    @Mock
    private lateinit var workoutExerciseCrossRefRepository: WorkoutExerciseCrossRefRepository

    @get:Rule
    val rule = createAndroidComposeRule<ComponentActivity>()

    private val curlsExercise = ExerciseUiState(id = 0, name = "Curls")
    private val dipsExercise = ExerciseUiState(id = 1, name = "Dips")
    private val pressExercise = ExerciseUiState(id = 2, name = "Press")
    private val workoutWithExercises =
        WorkoutWithExercisesUiState(exercises = listOf(curlsExercise, dipsExercise, pressExercise))

    private val curlsExerciseName = rule.onNode(hasText(curlsExercise.name))
    private val dipsExerciseName = rule.onNode(hasText(dipsExercise.name))
    private val pressExerciseName = rule.onNode(hasText(pressExercise.name))
    private val startButtons = rule.onAllNodesWithText("Start")
    private val completedText = rule.onAllNodesWithText("Completed")
    private val repsField = rule.onNode(hasContentDescription("Reps"))
    private val restField = rule.onNode(hasContentDescription("Rest"))
    private val weightField = rule.onNode(hasContentDescription("Weight"))
    private val finishExercise = rule.onNode(hasText("Finish Exercise"))

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun rendersLiveRecordExerciseCardNotComplete() {
        rule.setContent {
            LiveRecordWorkoutExerciseCard(exercise = curlsExercise, completed = false)
        }

        curlsExerciseName.assertExists()
        startButtons.assertCountEquals(1)
        completedText.assertCountEquals(0)
    }

    @Test
    fun rendersLiveRecordExerciseCardComplete() {
        rule.setContent {
            LiveRecordWorkoutExerciseCard(exercise = curlsExercise, completed = true)
        }

        curlsExerciseName.assertExists()
        startButtons.assertCountEquals(0)
        completedText.assertCountEquals(1)
    }

    @Test
    fun rendersLiveRecordExerciseCardNotCompleteRecordingTrue() {
        rule.setContent {
            LiveRecordWorkoutExerciseCard(
                exercise = curlsExercise,
                completed = false,
                recording = true
            )
        }

        curlsExerciseName.assertExists()
        startButtons.assertCountEquals(1)
        completedText.assertCountEquals(0)
        startButtons[0].assertIsNotEnabled()
    }

    @Test
    fun rendersLiveRecordExerciseCardNotCompleteClickStart() {
        var started = false

        rule.setContent {
            LiveRecordWorkoutExerciseCard(
                exercise = curlsExercise,
                completed = false,
                startFunction = { started = true })
        }

        curlsExerciseName.assertExists()
        startButtons.assertCountEquals(1)
        completedText.assertCountEquals(0)
        startButtons[0].assertIsEnabled()
        startButtons[0].performClick()
        assertThat(started, equalTo(true))
    }

    @Test
    fun rendersLiveRecordWorkout() {
        rule.setContent {
            LiveRecordWorkout(uiState = workoutWithExercises, saveFunction = {})
        }

        curlsExerciseName.assertExists()
        dipsExerciseName.assertExists()
        pressExerciseName.assertExists()
        startButtons.assertCountEquals(3)
        completedText.assertCountEquals(0)
    }

    @Test
    fun rendersLiveRecordWorkoutCompleteExercise() {
        rule.setContent {
            LiveRecordWorkout(uiState = workoutWithExercises, saveFunction = {})
        }

        curlsExerciseName.assertExists()
        dipsExerciseName.assertExists()
        pressExerciseName.assertExists()
        startButtons.assertCountEquals(3)
        completedText.assertCountEquals(0)

        startButtons[0].performClick()

        repsField.performClick()
        repsField.performTextInput("5")
        restField.performClick()
        restField.performTextInput("15")
        weightField.performClick()
        weightField.performTextInput("13.0")
        rule.onNode(hasText("Start") and hasAnySibling(hasContentDescription("Reps"))).performClick()

        finishExercise.performClick()

        rule.onAllNodesWithText("Start").assertCountEquals(2)
        rule.onAllNodesWithText("Completed").assertCountEquals(1)
    }
}
