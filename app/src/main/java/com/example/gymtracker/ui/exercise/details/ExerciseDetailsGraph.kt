package com.example.gymtracker.ui.exercise.details

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gymtracker.ui.DropdownBox
import com.example.gymtracker.ui.exercise.ExerciseDetailsUiState
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlin.math.abs
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.max

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

            drawAxis(canvasHeight, canvasWidth)

            labelYAxis(ySteps, yMin, yGradient, textMeasurer, yAxisSpace)

            val fontSize = labelXAxis(
                customFormatter, textMeasurer, startDate, canvasHeight, canvasWidth
            )

            val dataPoints = drawDataPoints(
                points,
                startDate,
                canvasWidth,
                currentDate,
                yMin,
                yGradient,
                yAxisSpace,
                lineColor
            )

            val selected =
                dataPoints.firstOrNull { point -> abs(point.first - tappedLocation.x) + abs(point.second - tappedLocation.y) < 20 }
            if (selected != null) {
                val dataPoint = points[dataPoints.indexOf(selected)]

                dataPointInformation(
                    yUnit,
                    dataPoint,
                    textMeasurer,
                    yLabel,
                    fontSize,
                    customFormatter,
                    tappedLocation,
                    canvasWidth,
                    canvasHeight,
                    selected,
                    lineColor
                )
            }
        }
    }
}

@Composable
fun GraphOptions(
    detailOptions: List<String>,
    detailOnChange: (String) -> Unit,
    timeOptions: List<String>,
    timeOnChange: (String) -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.End,
        modifier = Modifier.fillMaxWidth()
    ) {
        DropdownBox(
            options = detailOptions,
            onChange = { newDetail ->
                detailOnChange(newDetail)
            },
            modifier = Modifier
                .weight(1f)
        )
        Spacer(modifier = Modifier.width(16.dp))
        DropdownBox(
            options = timeOptions,
            onChange = { newTime ->
                timeOnChange(newTime)
            },
            modifier = Modifier
                .weight(1f)
        )
    }
}

fun getGraphDetails(
    uiState: ExerciseDetailsUiState,
    detail: String,
    detailOptions: List<String>
) = uiState.history!!.map { history ->
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
}

private fun DrawScope.drawAxis(
    canvasHeight: Float,
    canvasWidth: Float
) {
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
}

@OptIn(ExperimentalTextApi::class)
private fun DrawScope.labelYAxis(
    ySteps: Int,
    yMin: Double,
    yGradient: Int,
    textMeasurer: TextMeasurer,
    yAxisSpace: Float
) {
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
            topLeft = Offset(
                0F,
                (size.height - 75F) - (index * yAxisSpace) - textSize.height / 2
            ),
            style = TextStyle(fontSize = fontSize),
            size = Size(50F, yAxisSpace),
        )
    }
}

@OptIn(ExperimentalTextApi::class)
private fun DrawScope.labelXAxis(
    customFormatter: DateTimeFormatter?,
    textMeasurer: TextMeasurer,
    startDate: LocalDate,
    canvasHeight: Float,
    canvasWidth: Float
): TextUnit {
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
    return fontSize
}

private fun DrawScope.drawDataPoints(
    points: List<Pair<LocalDate, Double>>,
    startDate: LocalDate,
    canvasWidth: Float,
    currentDate: Long,
    yMin: Double,
    yGradient: Int,
    yAxisSpace: Float,
    lineColor: Color
): List<Pair<Float, Float>> {
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
    return dataPoints
}

@OptIn(ExperimentalTextApi::class)
private fun DrawScope.dataPointInformation(
    yUnit: String,
    dataPoint: Pair<LocalDate, Double>,
    textMeasurer: TextMeasurer,
    yLabel: String,
    fontSize: TextUnit,
    customFormatter: DateTimeFormatter?,
    tappedLocation: Offset,
    canvasWidth: Float,
    canvasHeight: Float,
    selected: Pair<Float, Float>,
    lineColor: Color
) {
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

    val textWidth = max(xLabelSize.size.width, yLabelSize.size.width) + max(
        xDataSize.size.width,
        yDataSize.size.width
    ) + 20F
    val textHeight = max(xLabelSize.size.height, yLabelSize.size.height) + max(
        xDataSize.size.height,
        yDataSize.size.height
    ) + 20F
    val topRowHeight = max(xLabelSize.size.height, xDataSize.size.height)
    val frontColumnWidth = max(xLabelSize.size.width, yLabelSize.size.width)

    val boxWidth = textWidth + 20F
    val boxHeight = textHeight + 20F
    val cornerRadius = 8.dp.toPx()

    val boxTopLeft =
        if (tappedLocation.x + 20F + boxWidth < canvasWidth && tappedLocation.y + 20F + boxHeight < canvasHeight) {
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
        topLeft = boxTopLeft.plus(
            Offset(
                15f + frontColumnWidth + 10f,
                15f + topRowHeight + 10f
            )
        )
    )
}
