package com.example.gymtracker.ui.exercise.details

import androidx.activity.ComponentActivity
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onParent
import org.junit.Rule
import org.junit.Test
import java.time.LocalDate

private const val TEST_TAG = "Exercise Details Graph"

class ExerciseDetailsGraphKtTest {

    @get:Rule
    val rule = createAndroidComposeRule<ComponentActivity>()

    private val graphCanvas = rule.onNode(hasTestTag(TEST_TAG)).onParent()

    @Test
    fun rendersCanvasForGraph() {
        rule.setContent {
            Graph(
                points = listOf(Pair(LocalDate.now(), 1.0)),
                startDate = LocalDate.now(),
                yLabel = "test",
                modifier = Modifier.testTag(TEST_TAG)
            )
        }

        graphCanvas.assertExists()
    }

    @Test
    fun rendersGraphOptions() {
        rule.setContent {
            GraphOptions(
                detailOptions = listOf("1", "2"),
                detailOnChange = {},
                timeOptions = listOf("a", "b"),
                timeOnChange = {}
            )
        }

        rule.onNode(hasText("1")).assertExists()
        rule.onNode(hasText("a")).assertExists()
    }
}
