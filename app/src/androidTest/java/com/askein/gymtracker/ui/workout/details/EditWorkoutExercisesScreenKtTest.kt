//package com.askein.gymtracker.ui.workout.details
//
//import androidx.activity.ComponentActivity
//import androidx.compose.ui.test.assertIsOff
//import androidx.compose.ui.test.assertIsOn
//import androidx.compose.ui.test.hasContentDescription
//import androidx.compose.ui.test.hasText
//import androidx.compose.ui.test.junit4.createAndroidComposeRule
//import androidx.compose.ui.test.performClick
//import com.askein.gymtracker.R
//import com.askein.gymtracker.data.exercise.ExerciseRepository
//import com.askein.gymtracker.data.workoutExerciseCrossRef.WorkoutExerciseCrossRefRepository
//import com.askein.gymtracker.helper.getResourceString
//import com.askein.gymtracker.ui.exercise.ExerciseUiState
//import com.askein.gymtracker.ui.exercise.ExercisesScreenViewModel
//import com.askein.gymtracker.ui.exercise.toExercise
//import kotlinx.coroutines.flow.flowOf
//import org.hamcrest.CoreMatchers.equalTo
//import org.hamcrest.MatcherAssert.assertThat
//import org.junit.Before
//import org.junit.Rule
//import org.junit.Test
//import org.mockito.Mock
//import org.mockito.Mockito.`when`
//import org.mockito.MockitoAnnotations
//
//class EditWorkoutExercisesScreenKtTest {
//
//    @get:Rule
//    val rule = createAndroidComposeRule<ComponentActivity>()
//
//    private val curlsExerciseUiState = ExerciseUiState(0, "Curls", "Biceps", "Dumbbells")
//    private val dipsExerciseUiState = ExerciseUiState(1, "Dips", "Triceps", "Dumbbells And Bars")
//
//    @Mock
//    private lateinit var exerciseRepository: ExerciseRepository
//
//    @Mock
//    private lateinit var workoutExerciseCrossRefRepository: WorkoutExerciseCrossRefRepository
//
//    private lateinit var exerciseViewModel: ExercisesScreenViewModel
//    private lateinit var workoutExerciseCrossRefViewModel: WorkoutExerciseCrossRefViewModel
//
//    private val curlsExercise = rule.onNode(hasText("Curls"))
//    private val curlsMuscle = rule.onNode(hasText("Biceps"))
//    private val curlsEquipment = rule.onNode(hasText("Dumbbells"))
//    private val dipsExercise = rule.onNode(hasText("Dips"))
//    private val dipsMuscle = rule.onNode(hasText("Triceps"))
//    private val dipsEquipment = rule.onNode(hasText("Dumbbells And Bars"))
//    private val workoutExercisesTitles = rule.onNode(hasText("Workout Exercises"))
//    private val availableExercisesTitles = rule.onNode(hasText("Available Exercises"))
//    private val addWorkoutExercisesCloseButton = rule.onNode(hasContentDescription("Close"))
//    private val doneButton = rule.onNode(hasText("Done"))
//
//    @Before
//    fun setUp() {
//        MockitoAnnotations.openMocks(this)
//
//        `when`(exerciseRepository.getAllExercisesStream()).thenReturn(
//            flowOf(
//                listOf(
//                    curlsExerciseUiState.toExercise()
//                )
//            )
//        )
//        `when`(exerciseRepository.getAllMuscleGroupsStream()).thenReturn(
//            flowOf(
//                listOf(
//                    curlsExerciseUiState.muscleGroup
//                )
//            )
//        )
//        `when`(exerciseRepository.getAllExerciseNames()).thenReturn(
//            flowOf(
//                listOf(
//                    curlsExerciseUiState.name
//                )
//            )
//        )
//
//        exerciseViewModel = ExercisesScreenViewModel(exerciseRepository)
//        workoutExerciseCrossRefViewModel =
//            WorkoutExerciseCrossRefViewModel(workoutExerciseCrossRefRepository)
//    }
//
//    @Test
//    fun rendersAddRemoveExerciseCardWithBoxChecked() {
//        rule.setContent {
//            AddRemoveExerciseCard(
//                exercise = curlsExerciseUiState,
//                checked = true,
//                yOffset = 0f,
//                clickFunction = { },
//                onPositioned = { },
//                onDragStart = { _, _ -> },
//                dragOffsetOnChange = { },
//                onDragFinished = { }
//            )
//        }
//
//        curlsExercise.assertExists()
//        curlsMuscle.assertExists()
//        curlsEquipment.assertExists()
//        val checkBox = rule.onNode(hasContentDescription("Deselect Curls"))
//        checkBox.assertExists()
//        checkBox.assertIsOn()
//    }
//
//    @Test
//    fun rendersAddRemoveExerciseCardWithBoxUnchecked() {
//        rule.setContent {
//            AddRemoveExerciseCard(
//                exercise = curlsExerciseUiState,
//                checked = false,
//                yOffset = 0f,
//                clickFunction = { },
//                onPositioned = { },
//                onDragStart = { _, _ -> },
//                dragOffsetOnChange = { },
//                onDragFinished = { }
//            )
//        }
//
//        curlsExercise.assertExists()
//        curlsMuscle.assertExists()
//        curlsEquipment.assertExists()
//        val checkBox = rule.onNode(hasContentDescription("Select Curls"))
//        checkBox.assertExists()
//        checkBox.assertIsOff()
//    }
//
//    @Test
//    fun clickingAddRemoveExerciseCarCheckBoxCallsClickFunction() {
//        var clicked = false
//
//        rule.setContent {
//            AddRemoveExerciseCard(
//                exercise = curlsExerciseUiState,
//                checked = false,
//                yOffset = 0f,
//                clickFunction = { clicked = true },
//                onPositioned = { },
//                onDragStart = { _, _ -> },
//                dragOffsetOnChange = { },
//                onDragFinished = { }
//            )
//        }
//
//        curlsExercise.assertExists()
//        curlsMuscle.assertExists()
//        curlsEquipment.assertExists()
//        val checkBox = rule.onNode(hasContentDescription("Select Curls"))
//        checkBox.assertExists()
//        checkBox.assertIsOff()
//
//        checkBox.performClick()
//        checkBox.assertIsOff()
//        assertThat(clicked, equalTo(true))
//    }
//
//    @Test
//    fun rendersExerciseList() {
//        rule.setContent {
//            ExercisesList(
//                exercises = listOf(curlsExerciseUiState, dipsExerciseUiState),
//                clickFunction = { },
//                listTitle = R.string.workout_exercises,
//                exercisesSelected = true
//            )
//        }
//
//        val curlsCheckBox = rule.onNode(hasContentDescription("Deselect Curls"))
//        val dipsCheckBox = rule.onNode(hasContentDescription("Deselect Dips"))
//
//        rule.onNode(hasText(getResourceString(R.string.workout_exercises))).assertExists()
//        curlsExercise.assertExists()
//        curlsMuscle.assertExists()
//        curlsEquipment.assertExists()
//        curlsCheckBox.assertExists()
//        curlsCheckBox.assertIsOn()
//        dipsExercise.assertExists()
//        dipsMuscle.assertExists()
//        dipsEquipment.assertExists()
//        dipsCheckBox.assertExists()
//        dipsCheckBox.assertIsOn()
//    }
//
//    @Test
//    fun rendersAddWorkoutExercisesScreenWithNoExercises() {
//        rule.setContent {
//            EditWorkoutExercisesScreen(
//                chosenExercises = listOf(),
//                allExercises = listOf(),
//                selectFunction = { },
//                deselectFunction = { },
//                saveOrder = { },
//                saveNewExercise = { },
//                onDismiss = { }
//            )
//        }
//
//        workoutExercisesTitles.assertDoesNotExist()
//        availableExercisesTitles.assertDoesNotExist()
//        addWorkoutExercisesCloseButton.assertExists()
//        doneButton.assertExists()
//    }
//
//    @Test
//    fun rendersAddWorkoutExercisesScreenWithNoWorkoutExercises() {
//        rule.setContent {
//            EditWorkoutExercisesScreen(
//                chosenExercises = listOf(),
//                allExercises = listOf(curlsExerciseUiState),
//                selectFunction = { },
//                deselectFunction = { },
//                saveOrder = { },
//                saveNewExercise = { },
//                onDismiss = { }
//            )
//        }
//
//        workoutExercisesTitles.assertDoesNotExist()
//        availableExercisesTitles.assertExists()
//        addWorkoutExercisesCloseButton.assertExists()
//        curlsExercise.assertExists()
//    }
//
//    @Test
//    fun rendersAddWorkoutExercisesScreenWithNoAvailableExercises() {
//        rule.setContent {
//            EditWorkoutExercisesScreen(
//                chosenExercises = listOf(curlsExerciseUiState),
//                allExercises = listOf(),
//                selectFunction = { },
//                deselectFunction = { },
//                saveOrder = { },
//                saveNewExercise = { },
//                onDismiss = { }
//            )
//        }
//
//        workoutExercisesTitles.assertExists()
//        availableExercisesTitles.assertDoesNotExist()
//        addWorkoutExercisesCloseButton.assertExists()
//        curlsExercise.assertExists()
//    }
//
//    @Test
//    fun rendersAddWorkoutExercisesScreenWithAvailableExercisesAndChosenExercises() {
//        rule.setContent {
//            EditWorkoutExercisesScreen(
//                chosenExercises = listOf(curlsExerciseUiState),
//                allExercises = listOf(dipsExerciseUiState),
//                selectFunction = { },
//                deselectFunction = { },
//                saveOrder = { },
//                saveNewExercise = { },
//                onDismiss = { }
//            )
//        }
//
//        workoutExercisesTitles.assertExists()
//        availableExercisesTitles.assertExists()
//        addWorkoutExercisesCloseButton.assertExists()
//        curlsExercise.assertExists()
//        dipsExercise.assertExists()
//    }
//
//    @Test
//    fun addWorkoutExercisesScreenClickingCheckBoxesCallsFunctions() {
//        var selected: String? = null
//        var deselected: String? = null
//
//        rule.setContent {
//            EditWorkoutExercisesScreen(
//                chosenExercises = listOf(curlsExerciseUiState),
//                allExercises = listOf(dipsExerciseUiState),
//                selectFunction = { exerciseUiState -> deselected = exerciseUiState.name },
//                deselectFunction = { exerciseUiState -> selected = exerciseUiState.name },
//                saveOrder = { },
//                saveNewExercise = { },
//                onDismiss = { }
//            )
//        }
//
//        val curlsCheckBox = rule.onNode(hasContentDescription("Deselect Curls"))
//        val dipsCheckBox = rule.onNode(hasContentDescription("Select Dips"))
//
//        workoutExercisesTitles.assertExists()
//        availableExercisesTitles.assertExists()
//        addWorkoutExercisesCloseButton.assertExists()
//        curlsExercise.assertExists()
//        dipsExercise.assertExists()
//
//        curlsCheckBox.performClick()
//        dipsCheckBox.performClick()
//        doneButton.performClick()
//
//        assertThat(selected, equalTo(curlsExerciseUiState.name))
//        assertThat(deselected, equalTo(dipsExerciseUiState.name))
//    }
//
//    @Test
//    fun addWorkoutExercisesScreenClickingCloseButtonCallsOnDismiss() {
//        var dismissed = false
//
//        rule.setContent {
//            EditWorkoutExercisesScreen(
//                chosenExercises = listOf(curlsExerciseUiState),
//                allExercises = listOf(dipsExerciseUiState),
//                selectFunction = { },
//                deselectFunction = { },
//                saveOrder = { },
//                saveNewExercise = { },
//                onDismiss = { dismissed = true }
//            )
//        }
//
//        addWorkoutExercisesCloseButton.assertExists()
//        addWorkoutExercisesCloseButton.performClick()
//        assertThat(dismissed, equalTo(true))
//    }
//
//    @Test
//    fun addWorkoutExercisesScreenClickingDoneButtonCallsOnDismiss() {
//        var dismissed = false
//
//        rule.setContent {
//            EditWorkoutExercisesScreen(
//                chosenExercises = listOf(curlsExerciseUiState),
//                allExercises = listOf(dipsExerciseUiState),
//                selectFunction = { },
//                deselectFunction = { },
//                saveOrder = { },
//                saveNewExercise = { },
//                onDismiss = { dismissed = true }
//            )
//        }
//
//        doneButton.performClick()
//        assertThat(dismissed, equalTo(true))
//    }
//}
