package com.askein.gymtracker.ui.exercise.details

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.performClick
import com.askein.gymtracker.R
import com.askein.gymtracker.helper.getResourceString
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Rule
import org.junit.Test

class GraphDetailsAndTimesKtTest {

    @get:Rule
    val rule = createAndroidComposeRule<ComponentActivity>()

    private val detailOptions = listOf(
        R.string.max_reps,
        R.string.max_time,
        R.string.max_sets,
        R.string.total_reps,
        R.string.total_time
    )
    private val timeOptions = listOf(
        R.string.seven_days,
        R.string.thirty_days,
        R.string.past_year,
        R.string.all_time
    )

    @Test
    fun shouldRenderGraphOptions() {
        rule.setContent {
            GraphOptions(
                detailOptions = detailOptions,
                detailOnChange = { },
                timeOptions = timeOptions,
                timeOnChange = { }
            )
        }

        rule.onNode(hasText(getResourceString(R.string.max_reps))).assertExists()
        rule.onNode(hasText(getResourceString(R.string.seven_days))).assertExists()
        detailOptions.filter { option -> option != R.string.max_reps }
            .forEach { option ->
                rule.onNode(hasText(getResourceString(option))).assertDoesNotExist()
            }
        timeOptions.filter { option -> option != R.string.seven_days }
            .forEach { option ->
                rule.onNode(hasText(getResourceString(option))).assertDoesNotExist()
            }
    }

    @Test
    fun clickingDetailOptionOpensDropdownToChooseDetail() {
        var chosenDetail = 0
        rule.setContent {
            GraphOptions(
                detailOptions = detailOptions,
                detailOnChange = { chosen -> chosenDetail = chosen },
                timeOptions = timeOptions,
                timeOnChange = { }
            )
        }

        rule.onNode(hasText(getResourceString(R.string.max_reps))).performClick()
        rule.onNode(hasText(getResourceString(R.string.max_sets))).performClick()

        assertThat(chosenDetail, equalTo(R.string.max_sets))
    }

    @Test
    fun clickingTimeOptionOpensDropdownToChooseTime() {
        var chosenTime = 0
        rule.setContent {
            GraphOptions(
                detailOptions = detailOptions,
                detailOnChange = { },
                timeOptions = timeOptions,
                timeOnChange = { chosen -> chosenTime = chosen }
            )
        }

        rule.onNode(hasText(getResourceString(R.string.seven_days))).performClick()
        rule.onNode(hasText(getResourceString(R.string.thirty_days))).performClick()

        assertThat(chosenTime, equalTo(R.string.thirty_days))
    }
}
