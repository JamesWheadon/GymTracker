package com.askein.gymtracker.ui.exercise.details

import android.icu.text.DateFormat
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.askein.gymtracker.R
import com.askein.gymtracker.ui.theme.GymTrackerTheme
import java.time.LocalDate
import java.time.ZoneId
import java.util.Date
import kotlin.math.abs
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.max

private const val X_OFFSET = 100F
private const val Y_OFFSET = 50F
private const val FIRST_POINT = 25F

@Composable
fun Graph(
    points: List<Pair<LocalDate, Double>>,
    startDate: LocalDate,
    yLabel: String,
    modifier: Modifier = Modifier,
    yUnit: String = ""
) {
    var tappedLocation by remember { mutableStateOf(Offset.Zero) }
    val customFormatter = DateFormat.getDateInstance(DateFormat.SHORT, LocalConfiguration.current.locales[0])
    val lineColour = MaterialTheme.colorScheme.primary
    val textAxisColour = MaterialTheme.colorScheme.onSurface
    val boxColour = MaterialTheme.colorScheme.background
    val textMeasurer = rememberTextMeasurer()
    val dateText = stringResource(id = R.string.date)
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
                yAxisSpace = (canvasHeight - X_OFFSET) / ySteps
            }

            val currentDate = LocalDate.now().toEpochDay()

            drawAxis(
                canvasHeight = canvasHeight,
                canvasWidth = canvasWidth,
                axisColour = textAxisColour
            )

            labelYAxis(
                ySteps = ySteps,
                yMin = yMin,
                yGradient = yGradient,
                textMeasurer = textMeasurer,
                yAxisSpace = yAxisSpace,
                yUnit = yUnit,
                textColour = textAxisColour
            )

            val fontSize = labelXAxis(
                customFormatter = customFormatter,
                textMeasurer = textMeasurer,
                startDate = startDate,
                canvasHeight = canvasHeight,
                canvasWidth = canvasWidth,
                textColour = textAxisColour
            )

            val dataPoints = drawDataPoints(
                points = points,
                startDate = startDate,
                canvasWidth = canvasWidth,
                currentDate = currentDate,
                yMin = yMin,
                yGradient = yGradient,
                yAxisSpace = yAxisSpace,
                lineColor = lineColour
            )

            val selected =
                dataPoints.firstOrNull { point -> abs(point.first - tappedLocation.x) + abs(point.second - tappedLocation.y) < 20 }
            if (selected != null) {
                val dataPoint = points[dataPoints.indexOf(selected)]
                dataPointInformation(
                    yUnit = yUnit,
                    dataPoint = dataPoint,
                    textMeasurer = textMeasurer,
                    yLabel = yLabel,
                    fontSize = fontSize,
                    customFormatter = customFormatter,
                    tappedLocation = tappedLocation,
                    canvasWidth = canvasWidth,
                    canvasHeight = canvasHeight,
                    selected = selected,
                    lineColor = lineColour,
                    textColour = textAxisColour,
                    backgroundColour = boxColour,
                    dateText = dateText
                )
            }
        }
    }
}

private fun DrawScope.drawAxis(
    canvasHeight: Float,
    canvasWidth: Float,
    axisColour: Color
) {
    drawLine(
        color = axisColour,
        start = Offset(X_OFFSET, canvasHeight - Y_OFFSET),
        end = Offset(canvasWidth, canvasHeight - Y_OFFSET),
        strokeWidth = 2f
    )
    drawLine(
        color = axisColour,
        start = Offset(X_OFFSET, 0F),
        end = Offset(X_OFFSET, canvasHeight - Y_OFFSET),
        strokeWidth = 2f
    )
}

private fun DrawScope.labelYAxis(
    ySteps: Int,
    yMin: Double,
    yGradient: Int,
    textMeasurer: TextMeasurer,
    yAxisSpace: Float,
    yUnit: String,
    textColour: Color
) {
    val textSizeFloat = 90F
    (0..ySteps).forEach { index ->
        val labelValue = yMin + (index * yGradient)
        val label = if (labelValue.rem(1) == 0.0) {
            "${labelValue.toInt()} $yUnit"
        } else {
            "$labelValue $yUnit"
        }
        var fontSize = 14.sp
        var textSize: IntSize
        do {
            fontSize = fontSize.times(0.9)
            val textLayoutResult = textMeasurer.measure(
                text = AnnotatedString(label),
                style = TextStyle(
                    fontSize = fontSize,
                    textAlign = TextAlign.Center,
                    color = textColour
                )
            )
            textSize = textLayoutResult.size
        } while (textSize.width > textSizeFloat)
        drawLine(
            color = textColour,
            start = Offset(
                textSizeFloat,
                (size.height - Y_OFFSET - FIRST_POINT) - (index * yAxisSpace)
            ),
            end = Offset(X_OFFSET, (size.height - Y_OFFSET - FIRST_POINT) - (index * yAxisSpace)),
            strokeWidth = 2f
        )
        drawText(
            textMeasurer = textMeasurer,
            text = label,
            topLeft = Offset(
                0F,
                (size.height - Y_OFFSET - FIRST_POINT) - (index * yAxisSpace) - textSize.height / 2
            ),
            style = TextStyle(fontSize = fontSize, color = textColour),
            size = Size(textSizeFloat, yAxisSpace),
        )
    }
}

private fun DrawScope.labelXAxis(
    customFormatter: DateFormat,
    textMeasurer: TextMeasurer,
    startDate: LocalDate,
    canvasHeight: Float,
    canvasWidth: Float,
    textColour: Color
): TextUnit {
    val date = customFormatter.format(LocalDate.now().toDate())
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
        text = customFormatter.format(startDate.toDate()),
        style = TextStyle(fontSize = fontSize, textAlign = TextAlign.Center, color = textColour),
        topLeft = Offset(X_OFFSET, canvasHeight - Y_OFFSET)
    )

    drawText(
        textMeasurer = textMeasurer,
        text = customFormatter.format(LocalDate.ofEpochDay(
            (LocalDate.now().toEpochDay() + startDate.toEpochDay()) / 2
        ).toDate()),
        style = TextStyle(fontSize = fontSize, textAlign = TextAlign.Center, color = textColour),
        topLeft = Offset(
            X_OFFSET + (canvasWidth - X_OFFSET - textSize.width) / 2,
            canvasHeight - Y_OFFSET
        )
    )

    drawText(
        textMeasurer = textMeasurer,
        text = date,
        style = TextStyle(fontSize = fontSize, textAlign = TextAlign.Center, color = textColour),
        topLeft = Offset(canvasWidth - FIRST_POINT - textSize.width, canvasHeight - Y_OFFSET)
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
    val xWidth = currentDate - startDate.toEpochDay()
    val dataPoints =
        points.map { point ->
            Pair(
                X_OFFSET + FIRST_POINT + (canvasWidth - X_OFFSET - 2 * FIRST_POINT) * (point.first.toEpochDay() - startDate.toEpochDay()) / xWidth,
                ((size.height - Y_OFFSET - FIRST_POINT) - ((point.second - yMin) / yGradient * yAxisSpace)).toFloat()
            )
        }.sortedBy { point -> point.first }

    if (dataPoints.isNotEmpty()) {
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
    }
    return dataPoints
}

private fun DrawScope.dataPointInformation(
    yUnit: String,
    dataPoint: Pair<LocalDate, Double>,
    textMeasurer: TextMeasurer,
    yLabel: String,
    fontSize: TextUnit,
    customFormatter: DateFormat,
    tappedLocation: Offset,
    canvasWidth: Float,
    canvasHeight: Float,
    selected: Pair<Float, Float>,
    lineColor: Color,
    backgroundColour: Color,
    textColour: Color,
    dateText: String
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
        text = AnnotatedString(customFormatter.format(dataPoint.first.toDate())),
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
        color = backgroundColour,
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
        style = TextStyle(fontSize = 10.sp, textAlign = TextAlign.Center, color = textColour),
        topLeft = boxTopLeft.plus(Offset(15f, 15f))
    )

    drawText(
        textMeasurer = textMeasurer,
        text = dateText,
        style = TextStyle(fontSize = 10.sp, textAlign = TextAlign.Center, color = textColour),
        topLeft = boxTopLeft.plus(Offset(15f, 15f + topRowHeight + 10f))
    )

    drawText(
        textMeasurer = textMeasurer,
        text = xDataLabel,
        style = TextStyle(fontSize = 10.sp, textAlign = TextAlign.Center, color = textColour),
        topLeft = boxTopLeft.plus(Offset(15f + frontColumnWidth + 10f, 15f))
    )

    drawText(
        textMeasurer = textMeasurer,
        text = customFormatter.format(dataPoint.first.toDate()),
        style = TextStyle(fontSize = 10.sp, textAlign = TextAlign.Center, color = textColour),
        topLeft = boxTopLeft.plus(
            Offset(
                15f + frontColumnWidth + 10f,
                15f + topRowHeight + 10f
            )
        )
    )
}

fun LocalDate.toDate(): Date = Date.from(this.atStartOfDay(ZoneId.systemDefault()).toInstant())

@Preview(showBackground = true)
@Composable
fun GraphPreview() {
    GymTrackerTheme(darkTheme = false) {
        Graph(
            points = listOf(
                Pair(LocalDate.now(), 10.0),
                Pair(LocalDate.now().minusDays(3), 30.0),
                Pair(LocalDate.now().minusDays(5), 20.0)
            ),
            startDate = LocalDate.now().minusDays(7),
            yLabel = "Time",
            yUnit = "mins"
        )
    }
}

@Preview(locale = "de", showBackground = true)
@Composable
fun GraphPreviewGerman() {
    GymTrackerTheme(darkTheme = false) {
        Graph(
            points = listOf(
                Pair(LocalDate.now(), 10.0),
                Pair(LocalDate.now().minusDays(3), 30.0),
                Pair(LocalDate.now().minusDays(5), 20.0)
            ),
            startDate = LocalDate.now().minusDays(7),
            yLabel = "Time",
            yUnit = "mins"
        )
    }
}
