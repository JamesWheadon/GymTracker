package com.askein.gymtracker.ui.workout.details

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
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.askein.gymtracker.ui.exercise.CalisthenicsExerciseDetails
import com.askein.gymtracker.ui.exercise.CardioExerciseDetails
import com.askein.gymtracker.ui.exercise.ExerciseUiState
import com.askein.gymtracker.ui.exercise.ExercisesScreenViewModel
import com.askein.gymtracker.ui.exercise.WeightsExerciseDetails
import com.askein.gymtracker.ui.exercise.create.ExerciseInformationForm
import com.askein.gymtracker.ui.theme.GymTrackerTheme
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
    val draggableExercises = remember { chosenExercises.toMutableStateList() }
    var newExerciseName: String? by remember { mutableStateOf(null) }
    var showCreateExercise by remember { mutableStateOf(false) }
    val unDraggableExercises = allExercises
        .filter { exercise -> !draggableExercises.contains(exercise) }
        .sortedBy { exercise -> exercise.name }

    if (newExerciseName != null) {
        val newExercise = unDraggableExercises.firstOrNull { it.name == newExerciseName }
        if (newExercise != null) {
            draggableExercises.add(newExercise)
            newExerciseName = null
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
                    SelectAndOrderWorkoutExercises(
                        draggableExercises = draggableExercises,
                        unDraggableExercises = unDraggableExercises,
                        modifier = Modifier.weight(1f)
                    )
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
private fun SelectAndOrderWorkoutExercises(
    draggableExercises: SnapshotStateList<ExerciseUiState>,
    unDraggableExercises: List<ExerciseUiState>,
    modifier: Modifier
) {
    val scrollState = rememberScrollState()

    var dragYOffset by remember { mutableFloatStateOf(0f) }
    var dragStartOffset by remember { mutableFloatStateOf(0f) }
    var dragIndex: Int? by remember { mutableStateOf(null) }
    var currentDraggedExercise: ExerciseUiState? by remember { mutableStateOf(null) }
    val draggableCards = remember { mutableStateMapOf<String, DraggableCardData>() }
    var columnBoundaries by remember { mutableStateOf(DraggableColumnBoundaries(0f, 0f)) }
    val scrollBoundary = 150f

    val onDragStart = { exercise: ExerciseUiState, startYOffset: Float ->
        currentDraggedExercise = exercise
        dragYOffset = draggableCards[exercise.name]?.topPosition ?: 0f
        dragStartOffset = dragYOffset + startYOffset + scrollState.value.toFloat()
    }
    val dragOffsetOnChange = { yOffset: Float ->
        dragYOffset += yOffset
        val draggingCardHeight = draggableCards[currentDraggedExercise?.name]!!.height / 2
        val exerciseAtDragLocation = draggableCards
            .minBy { card -> abs(dragYOffset.minus(card.value.topPosition) - draggingCardHeight) }
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
            val topScrollBound = abs(columnBoundaries.top) + scrollBoundary
            val bottomScrollBound =
                columnBoundaries.bottom - scrollBoundary - (scrollState.maxValue - scrollState.value) - draggableCards[currentDraggedExercise?.name]!!.height
            val scrollAmount = if (dragYOffset < topScrollBound) {
                maxOf(-scrollBoundary, -scrollState.value.toFloat())
            } else if (dragYOffset > bottomScrollBound) {
                minOf(scrollBoundary, (scrollState.maxValue - scrollState.value).toFloat())
            } else {
                0f
            }
            if (scrollAmount != 0f) {
                scrollState.animateScrollBy(scrollAmount)
            }
        }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(0.dp),
        modifier = modifier
            .padding(
                horizontal = 0.dp,
                vertical = 0.dp
            )
            .verticalScroll(scrollState)
            .onGloballyPositioned { location ->
                val top = location.positionInWindow().y
                columnBoundaries = DraggableColumnBoundaries(
                    top = top,
                    bottom = top + location.size.height
                )
            }
    ) {
        if (draggableExercises.isNotEmpty()) {
            DraggableExercisesList(
                exercises = draggableExercises,
                clickFunction = { exercise ->
                    draggableExercises.remove(exercise)
                    draggableCards.remove(exercise.name)
                },
                dragIndex = dragIndex,
                draggedCardHeight = draggableCards[currentDraggedExercise?.name]?.height
                    ?: 0,
                currentDraggedExercise = currentDraggedExercise,
                onDragStart = onDragStart,
                onPositioned = { exercise, cardData ->
                    draggableCards[exercise.name] = cardData
                },
                draggedCardYOffset = dragYOffset + scrollState.value.toFloat() - dragStartOffset,
                dragOffsetOnChange = dragOffsetOnChange,
                onDragFinished = onDragFinished
            )
        }
        if (unDraggableExercises.isNotEmpty()) {
            ExercisesList(
                exercises = unDraggableExercises
            ) { exercise ->
                draggableExercises.add(exercise)
            }
        }
    }
}

@Composable
fun DraggableExercisesList(
    exercises: List<ExerciseUiState>,
    clickFunction: (ExerciseUiState) -> Unit,
    dragIndex: Int?,
    draggedCardHeight: Int,
    currentDraggedExercise: ExerciseUiState?,
    onDragStart: (ExerciseUiState, Float) -> Unit,
    onPositioned: (ExerciseUiState, DraggableCardData) -> Unit,
    draggedCardYOffset: Float,
    dragOffsetOnChange: (Float) -> Unit,
    onDragFinished: () -> Unit
) {
    Text(
        text = stringResource(id = R.string.workout_exercises),
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
            DraggableAddRemoveExerciseCard(
                exercise = exercise,
                checked = true,
                yOffset = exerciseYOffset,
                clickFunction = clickFunction,
                onPositioned = onPositioned,
                onDragStart = onDragStart,
                dragOffsetOnChange = dragOffsetOnChange,
                onDragFinished = onDragFinished,
                currentlyDragging = true,
            )
        }
    }
}

@Composable
fun ExercisesList(
    exercises: List<ExerciseUiState>,
    clickFunction: (ExerciseUiState) -> Unit
) {
    Text(
        text = stringResource(id = R.string.available_exercises),
        style = MaterialTheme.typography.headlineLarge
    )
    exercises.forEach { exercise ->
        key(exercise.name) {
            AddRemoveExerciseCard(
                exercise = exercise,
                checked = false,
                clickFunction = clickFunction,
            )
        }
    }
}

@Composable
fun DraggableAddRemoveExerciseCard(
    exercise: ExerciseUiState,
    checked: Boolean,
    yOffset: Float,
    clickFunction: (ExerciseUiState) -> Unit,
    onPositioned: (ExerciseUiState, DraggableCardData) -> Unit,
    onDragStart: (ExerciseUiState, Float) -> Unit,
    dragOffsetOnChange: (Float) -> Unit,
    onDragFinished: () -> Unit,
    modifier: Modifier = Modifier,
    currentlyDragging: Boolean
) {
    var cardModifier = modifier
        .onGloballyPositioned { layoutCoordinates ->
            val cardHeight = layoutCoordinates.size.height
            onPositioned(
                exercise,
                DraggableCardData(cardHeight, layoutCoordinates.boundsInRoot().top)
            )
        }
        .pointerInput(exercise) {
            detectDragGesturesAfterLongPress(
                onDragStart = { offset ->
                    onDragStart(exercise, offset.y)
                    dragOffsetOnChange(offset.y)
                },
                onDrag = { change, dragAmount ->
                    change.consume()
                    dragOffsetOnChange(dragAmount.y)
                },
                onDragEnd = {
                    onDragFinished()
                },
                onDragCancel = {
                    onDragFinished()
                }
            )
        }
        .offset {
            IntOffset(0, yOffset.toInt())
        }
    if (currentlyDragging) {
        cardModifier = cardModifier
            .zIndex(1f)
            .graphicsLayer { alpha = 0.6f }
    }
    AddRemoveExerciseCard(
        checked = checked,
        exercise = exercise,
        clickFunction = clickFunction,
        modifier = cardModifier
    )
}

@Composable
private fun AddRemoveExerciseCard(
    checked: Boolean,
    exercise: ExerciseUiState,
    clickFunction: (ExerciseUiState) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
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
            when (exercise.type) {
                ExerciseType.WEIGHTS -> WeightsExerciseDetails(exercise = exercise)
                ExerciseType.CARDIO -> CardioExerciseDetails()
                ExerciseType.CALISTHENICS -> CalisthenicsExerciseDetails(exercise = exercise)
            }
        }
    }
}

data class DraggableCardData(val height: Int, val topPosition: Float)

data class DraggableColumnBoundaries(val top: Float, val bottom: Float)

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
