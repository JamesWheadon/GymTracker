package com.example.gymtracker.ui.workout.history.create

import androidx.activity.ComponentActivity
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.hasAnySibling
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.hasSetTextAction
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextClearance
import androidx.compose.ui.test.performTextInput
import com.example.gymtracker.data.exerciseHistory.weights.WeightsExerciseHistory
import com.example.gymtracker.data.exerciseHistory.weights.WeightsExerciseHistoryRepository
import com.example.gymtracker.data.workoutHistory.WorkoutHistory
import com.example.gymtracker.data.workoutHistory.WorkoutHistoryRepository
import com.example.gymtracker.ui.exercise.ExerciseUiState
import com.example.gymtracker.ui.exercise.history.ExerciseHistoryUiState
import com.example.gymtracker.ui.workout.details.WorkoutWithExercisesUiState
import com.example.gymtracker.ui.workout.history.WorkoutHistoryViewModel
import com.example.gymtracker.ui.workout.history.WorkoutHistoryWithExercisesUiState
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.containsString
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.not
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentCaptor
import org.mockito.Captor
import org.mockito.Mock
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import java.time.LocalDate


class RecordWorkoutHistoryScreenKtTest {

    @get:Rule
    val rule = createAndroidComposeRule<ComponentActivity>()

    @Mock
    private lateinit var mockWorkoutHistoryRepository: WorkoutHistoryRepository
    @Mock
    private lateinit var mockWeightsExerciseHistoryRepository: WeightsExerciseHistoryRepository

    @Captor
    lateinit var weightsExerciseHistoryCaptor: ArgumentCaptor<WeightsExerciseHistory>
    private lateinit var workoutHistoryViewModel: WorkoutHistoryViewModel

    private val curlsExercise = ExerciseUiState(1, "Curls", "Biceps", "Dumbbells")
    private val dipsExercise = ExerciseUiState(2, "Dips", "Triceps", "Bars")
    private val workoutWithExercises =
        WorkoutWithExercisesUiState(1, "Arms", listOf(curlsExercise, dipsExercise))
    private val integerErrorText = "Must be a positive number"
    private val decimalErrorText = "Must be a number"

    private val curlsExerciseTitle = rule.onNode(hasText("Curls"))
    private val dipsExerciseTitle = rule.onNode(hasText("Dips"))
    private val recordTitle = rule.onNode(hasText("Record Workout"))
    private val curlsCheckbox =
        rule.onNode(hasClickAction() and hasSetTextAction().not() and hasAnySibling(hasText("Curls")))
    private val setsField = rule.onNode(hasContentDescription("Sets"))
    private val repsField = rule.onNode(hasContentDescription("Reps"))
    private val weightField = rule.onNode(hasContentDescription("Weight"))
    private val unitField = rule.onNode(hasContentDescription("Units"))
    private val closeButton = rule.onNode(hasContentDescription("Close"))
    private val saveButton = rule.onNode(hasText("Save"))

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        workoutHistoryViewModel = WorkoutHistoryViewModel(
            mockWorkoutHistoryRepository,
            mockWeightsExerciseHistoryRepository
        )
    }

    @Test
    fun rendersRecordExerciseCard() {
        rule.setContent {
            RecordExerciseCard(
                exercise = curlsExercise,
                savedExerciseHistory = ExerciseHistoryUiState(),
                selectExerciseFunction = { true },
                deselectExerciseFunction = { true },
                errorStateChange = { _, _ -> },
                exerciseHistory = null
            )
        }

        curlsExerciseTitle.assertExists()
        curlsCheckbox.assertExists()
        setsField.assertDoesNotExist()
        repsField.assertDoesNotExist()
        weightField.assertDoesNotExist()
        unitField.assertDoesNotExist()
    }

    @Test
    fun recordExerciseCardClickingCheckboxRendersFormFields() {
        var selected = false
        var deselected = false
        rule.setContent {
            RecordExerciseCard(
                exercise = curlsExercise,
                savedExerciseHistory = ExerciseHistoryUiState(),
                selectExerciseFunction = {
                    selected = true
                    true
                },
                deselectExerciseFunction = {
                    deselected = true
                    true
                },
                errorStateChange = { _, _ -> },
                exerciseHistory = ExerciseHistoryUiState()
            )
        }

        curlsCheckbox.performClick()

        curlsExerciseTitle.assertExists()
        setsField.assertExists()
        repsField.assertExists()
        weightField.assertExists()
        unitField.assertExists()
        assertThat(selected, equalTo(true))
        assertThat(deselected, equalTo(false))

        curlsCheckbox.performClick()

        assertThat(deselected, equalTo(true))
    }

    @Test
    fun recordExerciseCardClickingCheckboxRendersFormFieldsWithErrors() {
        var error = false
        var id = -1

        rule.setContent {
            RecordExerciseCard(
                exercise = curlsExercise,
                savedExerciseHistory = ExerciseHistoryUiState(),
                selectExerciseFunction = { true },
                deselectExerciseFunction = { true },
                errorStateChange = { exerciseId, exerciseError ->
                    error = exerciseError
                    id = exerciseId
                                   },
                exerciseHistory = ExerciseHistoryUiState()
            )
        }
        curlsCheckbox.performClick()
        weightField.performTextClearance()

        setsField.assertTextContains(integerErrorText)
        repsField.assertTextContains(integerErrorText)
        weightField.assertTextContains(decimalErrorText)
        assertThat(error, equalTo(true))
        assertThat(id, equalTo(1))
    }

    @Test
    fun returnsFalseErrorStateWhenNoErrorsInForm() {
        var error = false
        var id = -1

        rule.setContent {
            RecordExerciseCard(
                exercise = curlsExercise,
                savedExerciseHistory = ExerciseHistoryUiState(),
                selectExerciseFunction = { true },
                deselectExerciseFunction = { true },
                errorStateChange = { exerciseId, exerciseError ->
                    error = exerciseError
                    id = exerciseId
                },
                exerciseHistory = ExerciseHistoryUiState()
            )
        }
        curlsCheckbox.performClick()
        setsField.performTextClearance()
        setsField.performTextInput("1")
        repsField.performTextClearance()
        repsField.performTextInput("1")

        assertTextDoesNotContain(setsField, integerErrorText)
        assertTextDoesNotContain(repsField, integerErrorText)
        assertTextDoesNotContain(weightField, decimalErrorText)
        assertThat(error, equalTo(false))
        assertThat(id, equalTo(1))
    }

    private fun assertTextDoesNotContain(
        semanticsNodeInteraction: SemanticsNodeInteraction, text: String
    ) {
        val config = semanticsNodeInteraction.fetchSemanticsNode().config
        assertThat(
            config[SemanticsProperties.Text].reduce { acc, string -> acc + string }.text
                    + config[SemanticsProperties.EditableText],
            not(containsString(text))
        )
    }

    @Test
    fun rendersRecordWorkoutHistoryScreen() {
        rule.setContent {
            RecordWorkoutHistoryScreen(
                uiState = workoutWithExercises,
                onDismiss = { }
            )
        }

        recordTitle.assertExists()
        curlsExerciseTitle.assertExists()
        dipsExerciseTitle.assertExists()
        closeButton.assertExists()
        saveButton.assertExists()
        rule.onAllNodes(hasClickAction()).assertCountEquals(4)
    }

    @Test
    fun recordWorkoutScreenButtonNotEnabledWhenErrorsInForm() {
        rule.setContent {
            RecordWorkoutHistoryScreen(
                uiState = workoutWithExercises,
                onDismiss = { }
            )
        }

        curlsCheckbox.performClick()

        setsField.assertExists()
        repsField.assertExists()
        weightField.assertExists()

        saveButton.assertIsNotEnabled()
    }

    @Test
    fun recordWorkoutScreenButtonEnabledWhenNoErrorsInForm() {
        rule.setContent {
            RecordWorkoutHistoryScreen(
                uiState = workoutWithExercises,
                onDismiss = { }
            )
        }

        curlsCheckbox.performClick()

        setsField.performTextClearance()
        setsField.performTextInput("1")
        repsField.performTextClearance()
        repsField.performTextInput("2")

        saveButton.assertIsEnabled()
    }

    @Test
    fun recordWorkoutScreenSaveButtonSavesCheckedExercises() = runBlocking {
        val workoutHistory = WorkoutHistory(0, 1, LocalDate.now())
        `when`(mockWorkoutHistoryRepository.insert(workoutHistory)).thenReturn(1L)

        var dismissed = false

        rule.setContent {
            RecordWorkoutHistoryScreen(
                uiState = workoutWithExercises,
                onDismiss = { dismissed = true },
                viewModel = workoutHistoryViewModel
            )
        }

        curlsCheckbox.performClick()

        setsField.performTextClearance()
        setsField.performTextInput("1")
        repsField.performTextClearance()
        repsField.performTextInput("2")

        saveButton.assertIsEnabled()

        saveButton.performClick()

        verify(mockWorkoutHistoryRepository).insert(workoutHistory)
        verify(mockWeightsExerciseHistoryRepository, times(1)).insertHistory(capture(weightsExerciseHistoryCaptor))
        assertThat(weightsExerciseHistoryCaptor.value.exerciseId, equalTo(1))
        assertThat(dismissed, equalTo(true))
    }

    @Test
    fun recordWorkoutScreenUpdateSaveButtonUpdatesCheckedExercises() = runBlocking {
        val workoutHistory = WorkoutHistory(1, 1, LocalDate.now())

        var dismissed = false

        rule.setContent {
            RecordWorkoutHistoryScreen(
                uiState = workoutWithExercises,
                onDismiss = { dismissed = true },
                viewModel = workoutHistoryViewModel,
                workoutHistory = WorkoutHistoryWithExercisesUiState(
                    workoutHistoryId = 1,
                    workoutId = 1,
                    exercises = listOf(ExerciseHistoryUiState(exerciseId = 1, sets = 1, reps = 1))
                ),
                titleText = "Update Workout"
            )
        }

        rule.onNode(hasText("Update Workout"))
        recordTitle.assertDoesNotExist()

        saveButton.assertIsEnabled()

        saveButton.performClick()

        verify(mockWorkoutHistoryRepository).update(workoutHistory)
        verify(mockWeightsExerciseHistoryRepository, times(1)).update(capture(weightsExerciseHistoryCaptor))
        assertThat(weightsExerciseHistoryCaptor.value.exerciseId, equalTo(1))
        assertThat(dismissed, equalTo(true))
    }

    @Test
    fun clickingXButtonCallsOnDismiss() = runBlocking {
        var dismissed = false

        rule.setContent {
            RecordWorkoutHistoryScreen(
                uiState = workoutWithExercises,
                onDismiss = { dismissed = true }
            )
        }

        closeButton.performClick()

        assertThat(dismissed, equalTo(true))
    }

    private fun <T> capture(argumentCaptor: ArgumentCaptor<T>): T = argumentCaptor.capture()
}
