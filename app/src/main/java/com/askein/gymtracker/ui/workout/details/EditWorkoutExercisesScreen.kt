package com.askein.gymtracker.ui.workout.details

import androidx.annotation.StringRes
import androidx.compose.foundation.gestures.animateScrollBy
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
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInWindow
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
import kotlinx.coroutines.launch
import kotlin.math.abs

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
    val allExercises = exercisesViewModel.exerciseListUiState.collectAsState().value.exerciseList
    EditWorkoutExercisesScreen(
        chosenExercises = uiState.exercises,
        allExercises = allExercises,
        saveWorkoutExercises = { exercises ->
            workoutExerciseCrossRefViewModel.saveExercisesForWorkout(
                exercises,
                uiState
            )
        },
        saveNewExercise = { exercise ->
            exercisesViewModel.saveExercise(exercise)
        },
        onDismiss = onDismiss,
        modifier = modifier
    )
}

@Composable
fun EditWorkoutExercisesScreen(
    chosenExercises: List<ExerciseUiState>,
    allExercises: List<ExerciseUiState>,
    saveWorkoutExercises: (List<ExerciseUiState>) -> Unit,
    saveNewExercise: (ExerciseUiState) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    val coroutineScope = rememberCoroutineScope()

    var dragYOffset by remember { mutableFloatStateOf(0f) }
    var dragStartYOffset by remember { mutableFloatStateOf(0f) }
    var dragStartScrollState by remember { mutableFloatStateOf(0f) }
    var dragIndex: Int? by remember { mutableStateOf(null) }
    var draggedCardHeight by remember { mutableIntStateOf(0) }
    var currentDraggedExercise: ExerciseUiState? by remember { mutableStateOf(null) }
    var showCreateExercise by remember { mutableStateOf(false) }
    val cardYOffsets = remember { mutableStateMapOf<String, Float>() }
    val draggableExercises = remember { chosenExercises.toMutableStateList() }
    var newExerciseName: String? by remember { mutableStateOf(null) }
    var columnTop by remember { mutableFloatStateOf(0f) }
    var columnBottom by remember { mutableFloatStateOf(0f) }
    val unDraggableExercises =
        allExercises.filter { exercise -> !draggableExercises.contains(exercise) }
            .sortedBy { exercise -> exercise.name }

    if (newExerciseName != null) {
        val newExercise = unDraggableExercises.firstOrNull { it.name == newExerciseName }
        if (newExercise != null) {
            draggableExercises.add(newExercise)
            newExerciseName = null
        }
    }

    val onDragStart = { exercise: ExerciseUiState, cardHeight: Int, startYOffset: Float ->
        currentDraggedExercise = exercise
        draggedCardHeight = cardHeight
        dragYOffset = cardYOffsets[exercise.name] ?: 0f
        dragStartYOffset = (cardYOffsets[exercise.name] ?: 0f) + startYOffset
        dragStartScrollState = scrollState.value.toFloat()
    }
    val dragOffsetOnChange = { yOffset: Float ->
        dragYOffset += yOffset
        val exerciseAtDragLocation = cardYOffsets
            .minBy { card -> abs(dragYOffset.minus(card.value) - draggedCardHeight / 2) }
            .key
        dragIndex = draggableExercises.indexOfFirst { exercise ->
            exercise.name == exerciseAtDragLocation
        }
    }
    val onDragFinished = onDragFinished@{
        if (currentDraggedExercise == null) return@onDragFinished
        draggableExercises.remove(currentDraggedExercise)
        draggableExercises.add(dragIndex!!, currentDraggedExercise!!)
        currentDraggedExercise = null
    }

    LaunchedEffect(dragYOffset) {
        if (!scrollState.isScrollInProgress) {
            val scrollBoundary = 150f
            val topScrollBound = abs(columnTop) + scrollBoundary
            val bottomScrollBound = columnBottom - scrollBoundary - (scrollState.maxValue - scrollState.value) - draggedCardHeight
            val scrollAmount = if (dragYOffset < topScrollBound) {
                maxOf(-scrollBoundary, -scrollState.value.toFloat())
            } else if (dragYOffset > bottomScrollBound) {
                minOf(scrollBoundary, (scrollState.maxValue - scrollState.value).toFloat())
            } else {
                0f
            }
            if (scrollAmount != 0f) {
                coroutineScope.launch {
                    scrollState.animateScrollBy(scrollAmount)
                }
            }
        }
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
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(0.dp),
                        modifier = Modifier
                            .weight(1f)
                            .padding(
                                horizontal = 0.dp,
                                vertical = 0.dp
                            )
                            .verticalScroll(scrollState)
                            .onGloballyPositioned { location ->
                                columnTop = location.positionInWindow().y
                                columnBottom = location.positionInWindow().y + location.size.height
                            }
                    ) {
                        if (draggableExercises.isNotEmpty()) {
                            ExercisesList(
                                exercises = draggableExercises,
                                clickFunction = { exercise ->
                                    draggableExercises.remove(exercise)
                                    cardYOffsets.remove(exercise.name)
                                },
                                listTitle = R.string.workout_exercises,
                                exercisesSelected = true,
                                dragIndex = dragIndex,
                                draggedCardHeight = draggedCardHeight,
                                currentDraggedExercise = currentDraggedExercise,
                                onDragStart = onDragStart,
                                onPositioned = { exercise, offset ->
                                    cardYOffsets[exercise.name] = offset.y
                                },
                                draggedCardYOffset = dragYOffset - dragStartYOffset + (scrollState.value.toFloat() - dragStartScrollState),
                                dragOffsetOnChange = dragOffsetOnChange,
                                onDragFinished = onDragFinished
                            )
                        }
                        if (unDraggableExercises.isNotEmpty()) {
                            ExercisesList(
                                exercises = unDraggableExercises,
                                clickFunction = { exercise ->
                                    draggableExercises.add(exercise)
                                },
                                listTitle = R.string.available_exercises
                            )
                        }
                    }
                    Button(
                        onClick = { showCreateExercise = true }
                    ) {
                        Text(text = stringResource(id = R.string.create_exercise_title))
                    }
                    Button(
                        onClick = {
                            saveWorkoutExercises(draggableExercises)
                            onDismiss()
                        }
                    ) {
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
                createFunction = { newExercise ->
                    newExerciseName = newExercise.name
                    saveNewExercise(newExercise)
                }
            )
        }
    }
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
    onDragStart: (ExerciseUiState, Int, Float) -> Unit = { _, _, _ -> },
    onPositioned: (ExerciseUiState, Offset) -> Unit = { _, _ -> },
    draggedCardYOffset: Float = 0f,
    dragOffsetOnChange: (Float) -> Unit = { },
    onDragFinished: () -> Unit = { }
) {
    Text(
        text = stringResource(id = listTitle),
        style = MaterialTheme.typography.headlineLarge
    )
    val startIndex = exercises.indexOf(currentDraggedExercise)
    exercises.forEachIndexed { index, exercise ->
        val exerciseYOffset = if (startIndex == -1 || dragIndex == null) {
            0f
        } else if (index == startIndex) {
            draggedCardYOffset
        } else if (dragIndex < startIndex && index in dragIndex until startIndex) {
            draggedCardHeight.toFloat()
        } else if (dragIndex > startIndex && index in (startIndex + 1)..dragIndex) {
            -draggedCardHeight.toFloat()
        } else {
            0f
        }
        key(exercise.name) {
            AddRemoveExerciseCard(
                exercise = exercise,
                checked = exercisesSelected,
                clickFunction = clickFunction,
                onPositioned = onPositioned,
                yOffset = exerciseYOffset,
                onDragStart = onDragStart,
                dragOffsetOnChange = dragOffsetOnChange,
                onDragFinished = onDragFinished,
            )
        }
    }
}

@Composable
fun AddRemoveExerciseCard(
    exercise: ExerciseUiState,
    checked: Boolean,
    yOffset: Float,
    clickFunction: (ExerciseUiState) -> Unit,
    onPositioned: (ExerciseUiState, Offset) -> Unit,
    onDragStart: (ExerciseUiState, Int, Float) -> Unit,
    dragOffsetOnChange: (Float) -> Unit,
    onDragFinished: () -> Unit,
    modifier: Modifier = Modifier
) {
    var cardHeight by remember { mutableIntStateOf(0) }
    var dragging by remember {
        mutableStateOf(false)
    }
    var cardModifier = if (checked) {
        modifier
            .pointerInput(exercise) {
                detectDragGesturesAfterLongPress(
                    onDragStart = { offset ->
                        dragging = true
                        onDragStart(exercise, cardHeight, offset.y)
                        dragOffsetOnChange(offset.y)
                    },
                    onDrag = { change, dragAmount ->
                        change.consume()
                        dragOffsetOnChange(dragAmount.y)
                    },
                    onDragEnd = {
                        dragging = false
                        onDragFinished()
                    },
                    onDragCancel = {
                        dragging = false
                        onDragFinished()
                    }
                )
            }
            .onGloballyPositioned { layoutCoordinates ->
                val rect = layoutCoordinates.boundsInRoot()
                cardHeight = layoutCoordinates.size.height
                onPositioned(exercise, rect.topLeft)
            }
    } else {
        modifier
    }
    if (checked) {
        cardModifier = cardModifier.offset {
            IntOffset(0, yOffset.toInt())
        }
    }
    if (dragging) {
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
            allExercises = listOf(
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
            saveWorkoutExercises = { },
            saveNewExercise = { },
            onDismiss = { }
        )
    }
}
