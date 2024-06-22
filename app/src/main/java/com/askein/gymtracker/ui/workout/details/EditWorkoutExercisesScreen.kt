package com.askein.gymtracker.ui.workout.details

import android.util.Log
import androidx.annotation.StringRes
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import com.askein.gymtracker.R
import com.askein.gymtracker.data.exercise.ExerciseType
import com.askein.gymtracker.ui.AppViewModelProvider
import com.askein.gymtracker.ui.exercise.ExerciseDetail
import com.askein.gymtracker.ui.exercise.ExerciseUiState
import com.askein.gymtracker.ui.exercise.ExercisesScreenViewModel
import com.askein.gymtracker.ui.exercise.create.ExerciseInformationForm
import com.askein.gymtracker.ui.theme.GymTrackerTheme
import kotlin.math.roundToInt

@Composable
fun EditWorkoutExercisesScreen(
    uiState: WorkoutWithExercisesUiState,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    exercisesViewModel: ExercisesScreenViewModel = viewModel(
        factory = AppViewModelProvider.Factory
    ),
    workoutExerciseCrossRefViewModel: WorkoutExerciseCrossRefViewModel = viewModel(
        factory = AppViewModelProvider.Factory
    )
) {
    var newExerciseName: String? by remember { mutableStateOf(null) }
    val chosenExercises = uiState.exercises
    val allExercises = exercisesViewModel.exerciseListUiState.collectAsState().value.exerciseList
    val remainingExercises = allExercises.filter { exercise ->
        !chosenExercises.contains(exercise)
    }
    if (newExerciseName != null) {
        val newExercise = allExercises.firstOrNull { exercise -> exercise.name == newExerciseName }
        if (newExercise != null) {
            workoutExerciseCrossRefViewModel.saveExerciseToWorkout(
                newExercise,
                uiState.toWorkoutUiState()
            )
            newExerciseName = null
        }
    }
    EditWorkoutExercisesScreen(
        chosenExercises = chosenExercises,
        remainingExercises = remainingExercises,
        selectFunction = { exercise ->
            workoutExerciseCrossRefViewModel.saveExerciseToWorkout(
                exercise,
                uiState.toWorkoutUiState()
            )
        },
        deselectFunction = { exercise ->
            workoutExerciseCrossRefViewModel.deleteExerciseFromWorkout(
                exercise,
                uiState.toWorkoutUiState()
            )
        },
        saveOrder = { exercises ->
            workoutExerciseCrossRefViewModel.updateExerciseOrderForWorkout(
                exercises,
                uiState.toWorkoutUiState()
            )
        },
        saveNewExercise = { exercise ->
            exercisesViewModel.saveExercise(exercise)
            newExerciseName = exercise.name
        },
        onDismiss = onDismiss,
        modifier = modifier
    )
}

@Composable
fun EditWorkoutExercisesScreen(
    chosenExercises: List<ExerciseUiState>,
    remainingExercises: List<ExerciseUiState>,
    selectFunction: (ExerciseUiState) -> Unit,
    deselectFunction: (ExerciseUiState) -> Unit,
    saveOrder: (List<ExerciseUiState>) -> Unit,
    saveNewExercise: (ExerciseUiState) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    var dragPosition by remember { mutableStateOf(Offset(0f, 0f)) }
    var dragIndex: Int? by remember { mutableStateOf(null) }
    var draggedCardHeight by remember { mutableIntStateOf(0) }
    var currentDraggedExercise: ExerciseUiState? by remember { mutableStateOf(null) }
    var showCreateExercise by remember { mutableStateOf(false) }
    val selectedPositions = remember { mutableStateListOf<Offset>() }
    val draggableExercises = remember { chosenExercises.toMutableStateList() }
    val unDraggableExercises = remember { remainingExercises.toMutableStateList() }
    val onDragStart = { exercise: ExerciseUiState, cardHeight: Int ->
        currentDraggedExercise = exercise
        draggedCardHeight = cardHeight
        dragPosition = selectedPositions[draggableExercises.indexOf(exercise)]
    }
    val dragOffsetOnChange = { offset: Offset ->
        dragPosition += offset
        dragIndex = selectedPositions.indexOf(selectedPositions.minBy {
            dragPosition.minus(it).getDistance()
        })
    }
    val onDragFinished = onDragFinished@{
        if (currentDraggedExercise == null) return@onDragFinished
        draggableExercises.remove(currentDraggedExercise)
        draggableExercises.add(dragIndex!!, currentDraggedExercise!!)
        currentDraggedExercise = null
    }

    Dialog(
        onDismissRequest = onDismiss
    ) {
        Box {
            Card(
                modifier = modifier
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(0.dp),
                    modifier = Modifier
                        .padding(
                            horizontal = 0.dp,
                            vertical = 40.dp
                        )
                        .verticalScroll(rememberScrollState())
                ) {
                    if (draggableExercises.isNotEmpty()) {
                        ExercisesList(
                            exercises = draggableExercises,
                            clickFunction = { exercise ->
                                draggableExercises.remove(exercise)
                                unDraggableExercises.add(exercise)
                                unDraggableExercises.sortBy { it.name }
                            },
                            listTitle = R.string.workout_exercises,
                            exercisesSelected = true,
                            dragIndex = dragIndex,
                            draggedCardHeight = draggedCardHeight,
                            currentDraggedExercise = currentDraggedExercise,
                            onDragStart = onDragStart,
                            onPositioned = { offset ->
                                if (!selectedPositions.contains(offset)) selectedPositions.add(
                                    offset
                                )
                            },
                            dragOffsetOnChange = dragOffsetOnChange,
                            onDragFinished = onDragFinished
                        )
                    }
                    if (unDraggableExercises.isNotEmpty()) {
                        ExercisesList(
                            exercises = unDraggableExercises,
                            clickFunction = { exercise ->
                                draggableExercises.add(exercise)
                                unDraggableExercises.remove(exercise)
                            },
                            listTitle = R.string.available_exercises
                        )
                    }
                    Button(onClick = { showCreateExercise = true }) {
                        Text(text = stringResource(id = R.string.create_exercise_title))
                    }
                    Button(onClick = {
                        updateWorkoutExercises(
                            startingExercises = chosenExercises,
                            finalExercises = draggableExercises,
                            selectFunction = selectFunction,
                            deselectFunction = deselectFunction,
                            saveOrder = saveOrder
                        )
                        onDismiss()
                    }) {
                        Text(text = stringResource(id = R.string.done))
                    }
                }
            }
            IconButton(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .offset((-8).dp, 8.dp),
                onClick = { onDismiss() }
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = stringResource(id = R.string.close)
                )
            }
        }
    }
    if (showCreateExercise) {
        Dialog(
            onDismissRequest = { showCreateExercise = false }
        ) {
            ExerciseInformationForm(
                formTitle = R.string.create_exercise_title,
                buttonText = R.string.create,
                onDismiss = { showCreateExercise = false },
                createFunction = saveNewExercise
            )
        }
    }
}

fun updateWorkoutExercises(
    startingExercises: List<ExerciseUiState>,
    finalExercises: List<ExerciseUiState>,
    selectFunction: (ExerciseUiState) -> Unit,
    deselectFunction: (ExerciseUiState) -> Unit,
    saveOrder: (List<ExerciseUiState>) -> Unit
) {
    finalExercises.filter { exercise -> !startingExercises.contains(exercise) }.forEach { exercise ->
        selectFunction(exercise)
    }
    startingExercises.filter { exercise -> !finalExercises.contains(exercise) }.forEach { exercise ->
        deselectFunction(exercise)
    }
    saveOrder(finalExercises)
}

@Composable
fun ExercisesList(
    exercises: List<ExerciseUiState>,
    clickFunction: (ExerciseUiState) -> Unit,
    @StringRes listTitle: Int,
    exercisesSelected: Boolean = false,
    dragIndex: Int? = null,
    draggedCardHeight: Int = 0,
    currentDraggedExercise: ExerciseUiState? = null,
    onDragStart: (ExerciseUiState, Int) -> Unit = { _, _ -> },
    onPositioned: (Offset) -> Unit = { },
    dragOffsetOnChange: (Offset) -> Unit = { },
    onDragFinished: () -> Unit = { }
) {
    Text(
        text = stringResource(id = listTitle),
        style = MaterialTheme.typography.headlineLarge
    )
    val startIndex = exercises.indexOf(currentDraggedExercise)
    exercises.forEachIndexed { index, exercise ->
        val yOffset = if (startIndex == -1 || dragIndex == null) {
            0
        } else if (index == startIndex) {
            null
        } else if (dragIndex < startIndex && index in dragIndex until startIndex) {
            draggedCardHeight
        } else if (dragIndex > startIndex && index in (startIndex + 1)..dragIndex) {
            -draggedCardHeight
        } else {
            0
        }
        AddRemoveExerciseCard(
            exercise = exercise,
            checked = exercisesSelected,
            yOffset = yOffset?.toFloat(),
            clickFunction = clickFunction,
            onPositioned = onPositioned,
            onDragStart = onDragStart,
            dragOffsetOnChange = dragOffsetOnChange,
            onDragFinished = onDragFinished,
        )
    }
}

@Composable
fun AddRemoveExerciseCard(
    exercise: ExerciseUiState,
    checked: Boolean,
    yOffset: Float?,
    clickFunction: (ExerciseUiState) -> Unit,
    onPositioned: (Offset) -> Unit,
    onDragStart: (ExerciseUiState, Int) -> Unit,
    dragOffsetOnChange: (Offset) -> Unit,
    onDragFinished: () -> Unit,
    modifier: Modifier = Modifier
) {
    val offsetX = remember { mutableFloatStateOf(0f) }
    val offsetY = remember { mutableFloatStateOf(0f) }
    var cardHeight by remember { mutableIntStateOf(0) }
    LaunchedEffect(exercise.name) {
        offsetX.floatValue = 0f
        offsetY.floatValue = 0f
    }
    val offset = IntOffset(offsetX.floatValue.roundToInt(), offsetY.floatValue.roundToInt())
    if (yOffset != null) {
        offsetY.floatValue = yOffset
    }
    Log.i(exercise.name, "${exercise.name}: $offset")
    var cardModifier = if (checked) {
        modifier
            .pointerInput(exercise) {
                detectDragGesturesAfterLongPress(
                    onDragStart = { offset ->
                        onDragStart(exercise, cardHeight)
                        dragOffsetOnChange(offset)
                    },
                    onDrag = { change, dragAmount ->
                        change.consume()
                        offsetX.floatValue = (offsetX.floatValue + dragAmount.x)
                        offsetY.floatValue = (offsetY.floatValue + dragAmount.y)
                        dragOffsetOnChange(dragAmount)
                    },
                    onDragEnd = {
                        offsetX.floatValue = 0f
                        offsetY.floatValue = 0f
                        onDragFinished()
                    },
                    onDragCancel = {
                        offsetX.floatValue = 0f
                        offsetY.floatValue = 0f
                        onDragFinished()
                    }
                )
            }
            .onGloballyPositioned { layoutCoordinates ->
                val rect = layoutCoordinates.boundsInRoot()
                cardHeight = layoutCoordinates.size.height
                onPositioned(rect.topLeft)
            }
    } else {
        modifier
    }
    if (checked){
        cardModifier = cardModifier.offset {
            offset
        }
    }
    if (offset != IntOffset.Zero) {
        cardModifier = cardModifier
            .zIndex(1f)
            .graphicsLayer { alpha = 0.6f }
    }
    Card(
        modifier = cardModifier
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            val checkboxContentDescription = if (checked) {
                stringResource(id = R.string.deselect_exercise, exercise.name)
            } else {
                stringResource(id = R.string.select_exercise, exercise.name)
            }
            Checkbox(
                checked = checked,
                onCheckedChange = { clickFunction(exercise) },
                modifier = Modifier.semantics {
                    contentDescription = checkboxContentDescription
                }
            )
            Text(
                text = exercise.name,
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.fillMaxWidth(0.45f),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            if (exercise.type == ExerciseType.WEIGHTS) {
                if (exercise.muscleGroup != "" || exercise.equipment != "") {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        if (exercise.muscleGroup != "") {
                            ExerciseDetail(
                                exerciseInfo = exercise.muscleGroup,
                                iconId = R.drawable.info_48px,
                                iconDescription = R.string.muscle_icon
                            )
                        }
                        if (exercise.equipment != "") {
                            ExerciseDetail(
                                exerciseInfo = exercise.equipment,
                                iconId = R.drawable.exercise_filled_48px,
                                iconDescription = R.string.equipment_icon
                            )
                        }
                    }
                } else {
                    ExerciseDetail(
                        exerciseInfo = stringResource(id = R.string.weights),
                        iconId = R.drawable.exercise_filled_48px,
                        iconDescription = R.string.equipment_icon,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            } else if (exercise.type == ExerciseType.CARDIO) {
                ExerciseDetail(
                    exerciseInfo = stringResource(id = R.string.cardio),
                    iconId = R.drawable.cardio_48dp,
                    iconDescription = R.string.cardio_icon,
                    modifier = Modifier.fillMaxWidth()
                )
            } else if (exercise.type == ExerciseType.CALISTHENICS) {
                if (exercise.muscleGroup != "") {
                    ExerciseDetail(
                        exerciseInfo = exercise.muscleGroup,
                        iconId = R.drawable.info_48px,
                        iconDescription = R.string.muscle_icon,
                        modifier = Modifier.fillMaxWidth()
                    )
                } else {
                    ExerciseDetail(
                        exerciseInfo = stringResource(id = R.string.calisthenics),
                        iconId = R.drawable.info_48px,
                        iconDescription = R.string.calisthenics_icon,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AddExerciseScreenPreview() {
    GymTrackerTheme(darkTheme = false) {
        EditWorkoutExercisesScreen(
            chosenExercises = listOf(ExerciseUiState(0, "Curls", "Biceps", "Dumbbells")),
            remainingExercises = listOf(
                ExerciseUiState(
                    1,
                    "Dips",
                    "Triceps",
                    "Dumbbells And Bars"
                ),
                ExerciseUiState(
                    1,
                    "Treadmill"
                ),
            ),
            selectFunction = { },
            deselectFunction = {},
            saveOrder = { },
            saveNewExercise = { },
            onDismiss = { }
        )
    }
}
