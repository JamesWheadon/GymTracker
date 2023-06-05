package com.example.gymtracker.ui.exercise.details

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.gymtracker.R
import com.example.gymtracker.ui.AppViewModelProvider
import com.example.gymtracker.ui.exercise.ExerciseDetailsUiState
import com.example.gymtracker.ui.history.ExerciseHistoryUiState
import com.example.gymtracker.ui.theme.GymTrackerTheme
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlin.math.abs
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.max

@Composable
fun ExerciseDetailsScreen(
    modifier: Modifier = Modifier,
    viewModel: ExerciseDetailsViewModel = viewModel(
        factory = AppViewModelProvider.Factory
    )
) {
    val uiState = viewModel.uiState.collectAsState().value
    ExerciseDetailsScreen(
        uiState = uiState,
        modifier
    )
}


@Composable
fun ExerciseDetailsScreen(
    uiState: ExerciseDetailsUiState,
    modifier: Modifier = Modifier
) {
    val timeOptions = listOf("7 Days", "30 Days", "Past Year", "All Time")
    val detailOptions = listOf("Max Weight", "Max Reps", "Max Sets", "Total Weight")
    val currentDate = LocalDate.now()
    val optionsToSpans = mapOf<String, LocalDate>(
        Pair(timeOptions[0], currentDate.minusDays(7)),
        Pair(timeOptions[1], currentDate.minusDays(30)),
        Pair(timeOptions[2], LocalDate.of(currentDate.year, 1, 1)),
        Pair(
            timeOptions[3],
            uiState.history?.minBy { history -> history.date.toEpochDay() }?.date ?: currentDate
        ),
    )
    var detail by remember { mutableStateOf(detailOptions[0]) }
    var timeSpan by remember { mutableStateOf(optionsToSpans[timeOptions[0]]) }
    val customCardElevation = CardDefaults.cardElevation(
        defaultElevation = 16.dp
    )
    val bestWeight = uiState.history?.maxOf { history -> history.weight }
    val best = uiState.history
        ?.filter { history -> history.weight == bestWeight }
        ?.maxBy { history -> history.reps }
    val recent = uiState.history?.maxBy { history -> history.date.toEpochDay() }
    Card(
        modifier = modifier
            .padding(vertical = 10.dp, horizontal = 10.dp),
        elevation = customCardElevation
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp, horizontal = 16.dp)
        ) {
            Text(
                text = uiState.name,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.CenterHorizontally)
                    .padding(vertical = 16.dp, horizontal = 16.dp)
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                ExerciseDetail(
                    exerciseInfo = uiState.muscleGroup,
                    iconId = R.drawable.info_48px,
                    iconDescription = "exercise icon",
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                )
                ExerciseDetail(
                    exerciseInfo = uiState.equipment,
                    iconId = R.drawable.exercise_filled_48px,
                    iconDescription = "exercise icon",
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()
            ) {
                ExerciseDetail(
                    exerciseInfo = "${best?.weight} ${uiState.measurement} for ${best?.reps} reps",
                    iconId = R.drawable.trophy_48dp,
                    iconDescription = "exercise icon",
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                )
                ExerciseDetail(
                    exerciseInfo = "${recent?.weight} ${uiState.measurement} for ${recent?.reps} reps",
                    iconId = R.drawable.history_48px,
                    iconDescription = "exercise icon",
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                horizontalArrangement = Arrangement.End,
                modifier = Modifier.fillMaxWidth()
            ) {
                Spacer(modifier = Modifier.weight(2f))
                DropdownBox(
                    options = detailOptions,
                    onChange = { newDetail ->
                        detail = newDetail
                    },
                    modifier = Modifier
                        .weight(1f)
                        .background(
                            color = MaterialTheme.colorScheme.primary,
                            shape = RoundedCornerShape(8.dp)
                        )
                )
                Spacer(modifier = Modifier.width(16.dp))
                DropdownBox(
                    options = timeOptions,
                    onChange = { newSpan ->
                        timeSpan = optionsToSpans[newSpan]
                    },
                    modifier = Modifier
                        .weight(1f)
                        .background(
                            color = MaterialTheme.colorScheme.primary,
                            shape = RoundedCornerShape(8.dp)
                        )
                )
            }
            Graph(
                points = uiState.history!!.map { history ->
                    when (detail) {
                        detailOptions[0] -> {
                            Pair(
                                history.date,
                                history.weight
                            )
                        }
                        detailOptions[1] -> {
                            Pair(
                                history.date,
                                history.reps.toDouble()
                            )
                        }
                        detailOptions[2] -> {
                            Pair(
                                history.date,
                                history.sets.toDouble()
                            )
                        }
                        detailOptions[3] -> {
                            Pair(
                                history.date,
                                history.weight * history.reps * history.sets
                            )
                        }
                        else -> {
                            Pair(
                                history.date,
                                history.weight
                            )
                        }
                    }
                },
                startDate = timeSpan ?: currentDate,
                yLabel = detail,
                yUnit = if (detail == detailOptions[0]) uiState.measurement else ""
            )
        }
    }
}

@Composable
fun ExerciseDetail(
    exerciseInfo: String,
    iconId: Int,
    iconDescription: String,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
        modifier = modifier
    ) {
        Icon(
            painter = painterResource(id = iconId),
            contentDescription = iconDescription,
            tint = MaterialTheme.colorScheme.tertiary,
            modifier = Modifier.size(20.dp)
        )
        Text(
            text = exerciseInfo,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownBox(
    options: List<String>,
    onChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedOption by remember { mutableStateOf(options.first()) }

    Box(
        modifier = modifier
    ) {
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = {
                expanded = !expanded
            }
        ) {
            BasicTextField(
                value = selectedOption,
                onValueChange = { onChange(selectedOption) },
                readOnly = true,
                textStyle = MaterialTheme.typography.labelSmall.copy(textAlign = TextAlign.Center),
                singleLine = true,
                decorationBox = { innerTextField ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceAround,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp)
                    ) {
                        Spacer(modifier = Modifier.weight(0.5f))
                        innerTextField()
                        Spacer(modifier = Modifier.weight(0.5f))
                        Icon(
                            imageVector = Icons.Filled.ArrowDropDown,
                            contentDescription = null,
                            modifier = Modifier
                                .height(20.dp)
                                .aspectRatio(1f)
                                .rotate(if (expanded) 180f else 0f)
                        )
                    }
                },
                modifier = Modifier
                    .menuAnchor()
            )

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                options.forEach { item ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = item,
                                style = MaterialTheme.typography.labelSmall
                            )
                        },
                        onClick = {
                            selectedOption = item
                            expanded = false
                            onChange(item)
                        }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalTextApi::class)
@Composable
fun Graph(
    points: List<Pair<LocalDate, Double>>,
    startDate: LocalDate,
    yLabel: String,
    modifier: Modifier = Modifier,
    yUnit: String = ""
) {
    var tappedLocation by remember { mutableStateOf(Offset.Zero) }

    val customFormatter = DateTimeFormatter.ofPattern("dd/MM")
    val lineColor = MaterialTheme.colorScheme.primary
    val textMeasurer = rememberTextMeasurer()
    Box(
        modifier = modifier
            .padding(horizontal = 8.dp, vertical = 12.dp),
        contentAlignment = Alignment.Center
    ) {

        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1F)
                .pointerInput(Unit) {
                    detectTapGestures { offset ->
                        tappedLocation = offset
                    }
                }
        ) {
            val canvasWidth = size.width
            val canvasHeight = size.height
            val yValues = points.map { point -> point.second }
            val yMin = floor(yValues.min())
            val yMax = ceil(yValues.max())
            var yGradient = ceil((yMax - yMin) / 5).toInt()
            val ySteps: Int
            val yAxisSpace: Float
            if (yGradient == 0) {
                ySteps = 0
                yAxisSpace = 100F
                yGradient = 1
            } else {
                ySteps = ceil((yMax - yMin) / yGradient).toInt()
                yAxisSpace = (canvasHeight - 100F) / ySteps
            }

            val currentDate = LocalDate.now().toEpochDay()

            drawLine(
                color = Color.Black,
                start = Offset(50F, canvasHeight - 50F),
                end = Offset(canvasWidth, canvasHeight - 50F),
                strokeWidth = 2f
            )
            drawLine(
                color = Color.Black,
                start = Offset(50F, 0F),
                end = Offset(50F, canvasHeight - 50F),
                strokeWidth = 2f
            )

            (0..ySteps).forEach { index ->
                val labelValue = yMin + (index * yGradient)
                val label = if (labelValue.rem(1) == 0.0) {
                    labelValue.toInt().toString()
                } else {
                    labelValue.toString()
                }
                var fontSize = 14.sp
                var textSize: IntSize
                do {
                    fontSize = fontSize.times(0.9)
                    val textLayoutResult = textMeasurer.measure(
                        text = AnnotatedString(label),
                        style = TextStyle(fontSize = fontSize, textAlign = TextAlign.Center)
                    )
                    textSize = textLayoutResult.size
                } while (textSize.width > 40F)
                drawLine(
                    color = Color.Black,
                    start = Offset(40F, (size.height - 75F) - (index * yAxisSpace)),
                    end = Offset(50F, (size.height - 75F) - (index * yAxisSpace)),
                    strokeWidth = 2f
                )
                drawText(
                    textMeasurer = textMeasurer,
                    text = label,
                    style = TextStyle(fontSize = fontSize),
                    topLeft = Offset(
                        0F,
                        (size.height - 75F) - (index * yAxisSpace) - textSize.height / 2
                    ),
                    maxSize = IntSize(50, yAxisSpace.toInt())
                )
            }
            val date = LocalDate.now().format(customFormatter)
            var fontSize = 14.sp
            var textSize: IntSize
            do {
                fontSize = fontSize.times(0.9)
                val textLayoutResult = textMeasurer.measure(
                    text = AnnotatedString(date),
                    style = TextStyle(fontSize = fontSize, textAlign = TextAlign.Center)
                )
                textSize = textLayoutResult.size
            } while (textSize.height > 50F)

            drawText(
                textMeasurer = textMeasurer,
                text = startDate.format(customFormatter),
                style = TextStyle(fontSize = fontSize, textAlign = TextAlign.Center),
                topLeft = Offset(75F, canvasHeight - 50F)
            )

            drawText(
                textMeasurer = textMeasurer,
                text = LocalDate.ofEpochDay(
                    (LocalDate.now().toEpochDay() + startDate.toEpochDay()) / 2
                ).format(customFormatter),
                style = TextStyle(fontSize = fontSize, textAlign = TextAlign.Center),
                topLeft = Offset(25F + (canvasWidth - textSize.width) / 2, canvasHeight - 50F)
            )

            drawText(
                textMeasurer = textMeasurer,
                text = date,
                style = TextStyle(fontSize = fontSize, textAlign = TextAlign.Center),
                topLeft = Offset(canvasWidth - 25F - textSize.width, canvasHeight - 50F)
            )

            val dataPoints =
                points.filter { point -> !point.first.isBefore(startDate) }.map { point ->
                    Pair(
                        75F + (canvasWidth - 100F) * (point.first.toEpochDay() - startDate.toEpochDay()) / (currentDate - startDate.toEpochDay()),
                        ((size.height - 75F) - ((point.second - yMin) / yGradient * yAxisSpace)).toFloat()
                    )
                }.sortedBy { point -> point.first }

            val path = Path()
            path.moveTo(dataPoints[0].first, dataPoints[0].second)

            dataPoints.forEach { point ->
                drawCircle(
                    color = lineColor,
                    center = Offset(point.first, point.second),
                    radius = 10F
                )
                path.lineTo(point.first, point.second)
            }

            drawPath(
                path = path,
                color = lineColor,
                style = Stroke(width = 2f)
            )

            val selected =
                dataPoints.firstOrNull { point -> abs(point.first - tappedLocation.x) + abs(point.second - tappedLocation.y) < 20 }
            if (selected != null) {
                val dataPoint = points[dataPoints.indexOf(selected)]

                val xDataLabel = if (yUnit == "") {
                    dataPoint.second.toString()
                } else {
                    "${dataPoint.second} $yUnit"
                }

                val xLabelSize = textMeasurer.measure(
                    text = AnnotatedString(yLabel),
                    style = TextStyle(fontSize = fontSize, textAlign = TextAlign.Center)
                )

                val xDataSize = textMeasurer.measure(
                    text = AnnotatedString(xDataLabel),
                    style = TextStyle(fontSize = fontSize, textAlign = TextAlign.Center)
                )

                val yLabelSize = textMeasurer.measure(
                    text = AnnotatedString("Date"),
                    style = TextStyle(fontSize = fontSize, textAlign = TextAlign.Center)
                )

                val yDataSize = textMeasurer.measure(
                    text = AnnotatedString(dataPoint.first.format(customFormatter)),
                    style = TextStyle(fontSize = fontSize, textAlign = TextAlign.Center)
                )

                val textWidth = max(xLabelSize.size.width, yLabelSize.size.width) + max(xDataSize.size.width, yDataSize.size.width) + 20F
                val textHeight = max(xLabelSize.size.height, yLabelSize.size.height) + max(xDataSize.size.height, yDataSize.size.height) + 20F
                val topRowHeight = max(xLabelSize.size.height, xDataSize.size.height)
                val frontColumnWidth = max(xLabelSize.size.width, yLabelSize.size.width)

                val boxWidth = textWidth + 20F
                val boxHeight = textHeight + 20F
                val cornerRadius = 8.dp.toPx()

                val boxTopLeft = if (tappedLocation.x + 20F + boxWidth < canvasWidth && tappedLocation.y + 20F + boxHeight < canvasHeight) {
                    Offset(selected.first + 20F, selected.second + 20F)
                } else if (tappedLocation.x + 20F + boxWidth < canvasWidth) {
                    Offset(selected.first + 20F, selected.second - 20F - boxHeight)
                } else if (tappedLocation.y + 20F + boxHeight < canvasHeight) {
                    Offset(selected.first - 20F - boxWidth, selected.second + 20F)
                } else {
                    Offset(selected.first - 20F - boxWidth, selected.second - 20F - boxHeight)
                }

                drawRoundRect(
                    color = Color.White,
                    topLeft = boxTopLeft,
                    size = Size(boxWidth, boxHeight),
                    cornerRadius = CornerRadius(cornerRadius),
                    style = Fill
                )

                drawRoundRect(
                    color = lineColor,
                    topLeft = boxTopLeft,
                    size = Size(boxWidth, boxHeight),
                    cornerRadius = CornerRadius(cornerRadius),
                    style = Stroke(width = 4F)
                )

                drawText(
                    textMeasurer = textMeasurer,
                    text = yLabel,
                    style = TextStyle(fontSize = 10.sp, textAlign = TextAlign.Center),
                    topLeft = boxTopLeft.plus(Offset(15f, 15f))
                )

                drawText(
                    textMeasurer = textMeasurer,
                    text = "Date",
                    style = TextStyle(fontSize = 10.sp, textAlign = TextAlign.Center),
                    topLeft = boxTopLeft.plus(Offset(15f, 15f + topRowHeight + 10f))
                )

                drawText(
                    textMeasurer = textMeasurer,
                    text = xDataLabel,
                    style = TextStyle(fontSize = 10.sp, textAlign = TextAlign.Center),
                    topLeft = boxTopLeft.plus(Offset(15f + frontColumnWidth + 10f, 15f))
                )

                drawText(
                    textMeasurer = textMeasurer,
                    text = dataPoint.first.format(customFormatter),
                    style = TextStyle(fontSize = 10.sp, textAlign = TextAlign.Center),
                    topLeft = boxTopLeft.plus(Offset(15f + frontColumnWidth + 10f, 15f + topRowHeight + 10f))
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ItemDetailsScreenPreview() {
    GymTrackerTheme(darkTheme = false) {
        ExerciseDetailsScreen(
            uiState = ExerciseDetailsUiState(
                name = "Curls",
                muscleGroup = "Biceps",
                equipment = "Dumbbells",
                measurement = "kg",
                history = listOf(
                    ExerciseHistoryUiState(
                        id = 1,
                        weight = 13.0,
                        sets = 1,
                        reps = 2,
                        rest = 1,
                        date = LocalDate.now().minusDays(5)
                    ),
                    ExerciseHistoryUiState(
                        id = 1,
                        weight = 15.0,
                        sets = 1,
                        reps = 2,
                        rest = 1,
                        date = LocalDate.now().minusDays(3)
                    ),
                    ExerciseHistoryUiState(
                        id = 1,
                        weight = 10.0,
                        sets = 1,
                        reps = 1,
                        rest = 1,
                        date = LocalDate.now().minusDays(20)
                    ),
                    ExerciseHistoryUiState(
                        id = 1,
                        weight = 10.0,
                        sets = 1,
                        reps = 1,
                        rest = 1,
                        date = LocalDate.now()
                    )
                )
            )
        )
    }
}
