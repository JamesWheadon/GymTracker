package com.example.gymtracker.ui.workout.history.create.live

import androidx.activity.ComponentActivity
import androidx.compose.runtime.CompositionLocalProvider
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
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import com.example.gymtracker.ui.exercise.ExerciseUiState
import com.example.gymtracker.ui.user.LocalUserPreferences
import com.example.gymtracker.ui.user.UserPreferencesUiState
import com.example.gymtracker.ui.workout.details.WorkoutWithExercisesUiState
import org.hamcrest.CoreMatchers.equalTo
import org.junit.Rule
import org.junit.Test

class LiveRecordWorkoutKtTest {

    @get:Rule
    val rule = createAndroidComposeRule<ComponentActivity>()

    private val curlsExercise =
        ExerciseUiState(id = 0, name = "Curls", equipment = "Dumbbells", muscleGroup = "Biceps")
    private val dipsExercise =
        ExerciseUiState(id = 1, name = "Dips", equipment = "Bars", muscleGroup = "Triceps")
    private val pressExercise =
        ExerciseUiState(id = 2, name = "Press", equipment = "Bench", muscleGroup = "Pecs")
    private val cardioExercise = ExerciseUiState(name = "Treadmill")
    private val workoutWithExercises =
        WorkoutWithExercisesUiState(exercises = listOf(curlsExercise, dipsExercise, pressExercise))
    private val weightsWorkout = WorkoutWithExercisesUiState(exercises = listOf(curlsExercise))
    private val cardioWorkout = WorkoutWithExercisesUiState(exercises = listOf(cardioExercise))

    private val curlsExerciseName = rule.onNode(hasText(curlsExercise.name))
    private val dipsExerciseName = rule.onNode(hasText(dipsExercise.name))
    private val pressExerciseName = rule.onNode(hasText(pressExercise.name))
    private val startButtons = rule.onAllNodesWithText("Start")
    private val completedText = rule.onAllNodesWithText("Completed")
    private val repsField = rule.onNode(hasContentDescription("Reps"))
    private val restField = rule.onNode(hasContentDescription("Rest"))
    private val weightField = rule.onNode(hasContentDescription("Weight"))
    private val distanceField = rule.onNode(hasContentDescription("Distance"))
    private val finishExercise = rule.onNode(hasText("Finish Exercise"))
    private val finishWorkout = rule.onNode(hasText("Finish Workout"))

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
            LiveRecordWorkout(
                uiState = workoutWithExercises,
                saveFunction = {},
                finishFunction = {}
            )
        }

        curlsExerciseName.assertExists()
        dipsExerciseName.assertExists()
        pressExerciseName.assertExists()
        startButtons.assertCountEquals(3)
        completedText.assertCountEquals(0)
    }

    @Test
    fun rendersLiveRecordWorkoutCompleteExercise() {
        var saved = false
        rule.setContent {
            val userPreferencesUiState = UserPreferencesUiState()
            CompositionLocalProvider(LocalUserPreferences provides userPreferencesUiState) {
                LiveRecordWorkout(
                    uiState = workoutWithExercises,
                    saveFunction = { saved = true },
                    finishFunction = {}
                )
            }
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
        rule.onNode(hasText("Start") and hasAnySibling(hasContentDescription("Reps")))
            .performClick()

        finishExercise.performClick()

        rule.onAllNodesWithText("Start").assertCountEquals(2)
        rule.onAllNodesWithText("Completed").assertCountEquals(1)
        assertThat(saved, equalTo(true))
    }

    @Test
    fun rendersLiveRecordWorkoutCompleteWorkout() {
        var finished = false
        rule.setContent {
            LiveRecordWorkout(
                uiState = workoutWithExercises,
                saveFunction = { },
                finishFunction = { finished = true })
        }

        finishWorkout.performClick()

        assertThat(finished, equalTo(true))
    }

    @Test
    fun rendersLiveRecordWorkoutCancelExercise() {
        rule.setContent {
            val userPreferencesUiState = UserPreferencesUiState()
            CompositionLocalProvider(LocalUserPreferences provides userPreferencesUiState) {
                LiveRecordWorkout(
                    uiState = workoutWithExercises,
                    saveFunction = {},
                    finishFunction = {}
                )
            }
        }

        curlsExerciseName.assertExists()
        dipsExerciseName.assertExists()
        pressExerciseName.assertExists()
        startButtons.assertCountEquals(3)
        completedText.assertCountEquals(0)

        startButtons[0].performClick()
        rule.onNode(hasText("Cancel")).performClick()

        rule.onAllNodesWithText("Start").assertCountEquals(3)
        rule.onAllNodesWithText("Completed").assertCountEquals(0)
    }

    @Test
    fun rendersRecordWeightsExerciseForWeightsExercise() {
        rule.setContent {
            val userPreferencesUiState = UserPreferencesUiState()
            CompositionLocalProvider(LocalUserPreferences provides userPreferencesUiState) {
                LiveRecordWorkout(uiState = weightsWorkout, saveFunction = {}, finishFunction = {})
            }
        }

        startButtons[0].performClick()
        repsField.assertExists()
        distanceField.assertDoesNotExist()
    }

    @Test
    fun rendersRecordCardioExerciseForCardioExercise() {
        rule.setContent {
            val userPreferencesUiState = UserPreferencesUiState()
            CompositionLocalProvider(LocalUserPreferences provides userPreferencesUiState) {
                LiveRecordWorkout(uiState = cardioWorkout, saveFunction = {}, finishFunction = {})
            }
        }

        startButtons[0].performClick()
        distanceField.assertExists()
        repsField.assertDoesNotExist()
    }
}
