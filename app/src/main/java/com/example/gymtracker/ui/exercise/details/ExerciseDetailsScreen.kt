package com.example.gymtracker.ui.exercise.details

import androidx.compose.foundation.Canvas
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
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
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.Date
import kotlin.math.ceil
import kotlin.math.floor

private const val DAYS_TO_MILLIS = 86400000L

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
    val customCardElevation = CardDefaults.cardElevation(
        defaultElevation = 16.dp
    )
    val bestWeight = uiState.history?.maxOf { history -> history.weight }
    val best = uiState.history
        ?.filter { history -> history.weight == bestWeight }
        ?.maxBy { history -> history.reps }
    val recent = uiState.history?.maxBy { history -> history.date.time }
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
                    exerciseInfo = "${best?.weight} kg for ${best?.reps} reps",
                    iconId = R.drawable.trophy_48dp,
                    iconDescription = "exercise icon",
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                )
                ExerciseDetail(
                    exerciseInfo = "${recent?.weight} kg for ${recent?.reps} reps",
                    iconId = R.drawable.history_48px,
                    iconDescription = "exercise icon",
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Graph(
                points = uiState.history!!.map { history ->
                    Pair(
                        history.date.time,
                        history.weight
                    )
                }
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

@OptIn(ExperimentalTextApi::class)
@Composable
fun Graph(
    points: List<Pair<Long, Double>>,
    modifier: Modifier = Modifier
) {
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
        ) {
            val canvasWidth = size.width
            val canvasHeight = size.height
            val weight = points.map { point -> point.second }
            val weightMin = floor(weight.min())
            val weightMax = ceil(weight.max())
            val weightGradient = ceil((weightMax - weightMin) / 5).toInt()
            val steps = ceil((weightMax - weightMin) / weightGradient).toInt()
            val yAxisSpace = (canvasHeight - 100F) / steps

            // Draw x-axis
            drawLine(
                color = Color.Black,
                start = Offset(50F, canvasHeight - 50F),
                end = Offset(canvasWidth, canvasHeight - 50F),
                strokeWidth = 2f
            )

            // Draw y-axis
            drawLine(
                color = Color.Black,
                start = Offset(50F, 0F),
                end = Offset(50F, canvasHeight - 50F),
                strokeWidth = 2f
            )

            (0..steps).forEach { index ->
                val labelValue = weightMin + (index * weightGradient)
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
                    topLeft = Offset(0F, (size.height - 75F) - (index * yAxisSpace) - textSize.height / 2),
                    maxSize = IntSize(50, yAxisSpace.toInt())
                )
            }

            val sectionWidth = (canvasWidth - 100F) / 7
            (0 until 7).forEach { index ->
                val date = LocalDate.now().minusDays(index.toLong()).format(customFormatter)
                drawLine(
                    color = Color.Black,
                    start = Offset(75F + sectionWidth * (6 - index), canvasHeight - 50F),
                    end = Offset(75F + sectionWidth * (6 - index), canvasHeight - 40F),
                    strokeWidth = 2f
                )
                var fontSize = 14.sp
                var textSize: IntSize
                do {
                    fontSize = fontSize.times(0.9)
                    val textLayoutResult = textMeasurer.measure(
                        text = AnnotatedString(date),
                        style = TextStyle(fontSize = fontSize, textAlign = TextAlign.Center)
                    )
                    textSize = textLayoutResult.size
                } while (textSize.width > sectionWidth)
                drawText(
                    textMeasurer = textMeasurer,
                    text = date,
                    style = TextStyle(fontSize = fontSize, textAlign = TextAlign.Center),
                    topLeft = Offset(75F + sectionWidth * (6 - index) + (sectionWidth - textSize.width) / 2, canvasHeight - 50F),
                    maxSize = IntSize((canvasWidth - 100F / 7).toInt(), 50)
                )
            }
            drawLine(
                color = Color.Black,
                start = Offset(canvasWidth - 25F, canvasHeight - 50F),
                end = Offset(canvasWidth - 25F, canvasHeight - 40F),
                strokeWidth = 2f
            )

            val dataPoints = points.map { point -> Pair(
                75F + sectionWidth * ((point.first - (LocalDate.now().minusDays(7).atStartOfDay().toEpochSecond(ZoneOffset.MIN) * 1000)) / DAYS_TO_MILLIS),
                ((size.height - 75F) - ((point.second - weightMin) / weightGradient * yAxisSpace)).toFloat()
            ) }.sortedBy { point -> point.first }

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
                history = listOf(
                    ExerciseHistoryUiState(
                        id = 1,
                        weight = 13.0,
                        sets = 1,
                        reps = 2,
                        rest = 1,
                        date = Date.from(Instant.now().minusSeconds(60 * 60 * 24 * 5))
                    ),
                    ExerciseHistoryUiState(
                        id = 1,
                        weight = 15.0,
                        sets = 1,
                        reps = 2,
                        rest = 1,
                        date = Date.from(Instant.now().minusSeconds(60 * 60 * 24 * 3))
                    ),
                    ExerciseHistoryUiState(
                        id = 1,
                        weight = 10.0,
                        sets = 1,
                        reps = 1,
                        rest = 1,
                        date = Date()
                    )
                )
            )
        )
    }
}
