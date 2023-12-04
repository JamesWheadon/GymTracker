package com.example.gymtracker.ui.exercise.details

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.performClick
import com.example.gymtracker.ui.exercise.ExerciseDetailsUiState
import com.example.gymtracker.ui.exercise.history.ExerciseHistoryUiState
import com.example.gymtracker.ui.exercise.toExerciseUiState
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Rule
import org.junit.Test
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.time.format.TextStyle
import java.util.Locale

private const val NAME = "Curls"
private const val MUSCLE_GROUP = "Biceps"
private const val EQUIPMENT = "Dumbbells"

class ExerciseDetailsCalendarKtTest {

    @get:Rule
    val rule = createAndroidComposeRule<ComponentActivity>()

    private val currentMonth = YearMonth.now()

    private val month = rule.onNode(
        hasText(
            "${
                currentMonth.month.getDisplayName(
                    TextStyle.FULL,
                    Locale.ENGLISH
                )
            } ${currentMonth.year}"
        )
    )
    private val chooseMonthButton = rule.onNode(hasContentDescription("Change Month"))
    private val previousYearButton = rule.onNode(hasContentDescription("Previous Year"))
    private val nextYearButton = rule.onNode(hasContentDescription("Next Year"))
    private val date = rule.onNode(
        hasText(
            "Exercises on ${
                LocalDate.now().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG))
            }"
        )
    )
    private val sets = rule.onNode(hasText("Sets: 1"))
    private val reps = rule.onNode(hasText("Reps: 2"))
    private val weight = rule.onNode(hasText("Weight: 13.0kg"))
    private val rest = rule.onNode(hasText("Rest time: 1"))
    private val deleteButton = rule.onNode(hasContentDescription("Delete history"))

    private val exerciseHistory = ExerciseHistoryUiState(1, 1, 13.0, 1, 2, 1, LocalDate.now())
    private val exercise = ExerciseDetailsUiState(
        name = NAME,
        muscleGroup = MUSCLE_GROUP,
        equipment = EQUIPMENT,
        history = listOf(exerciseHistory)
    )

    @Test
    fun rendersExerciseHistoryCalendarWithOneActiveDay() {
        rule.setContent {
            ExerciseHistoryCalendar(
                uiState = exercise
            )
        }

        month.assertExists()
        chooseMonthButton.assertExists()

        for (day in DayOfWeek.values().map { it.getDisplayName(TextStyle.SHORT, Locale.ENGLISH) }) {
            rule.onNode(hasText(day)).assertExists()
        }

        for (dayValue in 1..currentMonth.month.length(currentMonth.isLeapYear)) {
            val dayNode = rule.onNode(hasText(dayValue.toString()))
            dayNode.assertExists()
            if (dayValue == LocalDate.now().dayOfMonth) {
                dayNode.assertIsEnabled()
            } else {
                dayNode.assertIsNotEnabled()
            }
        }
    }

    @Test
    fun clickingDropDownArrowOpensOptionsToChangeMonth() {
        rule.setContent {
            ExerciseHistoryCalendar(
                uiState = exercise
            )
        }

        chooseMonthButton.performClick()

        previousYearButton.assertIsEnabled()
        nextYearButton.assertIsNotEnabled()

        previousYearButton.performClick()

        previousYearButton.assertIsEnabled()
        nextYearButton.assertIsEnabled()

        rule.onNode(hasText("Sep")).performClick()

        rule.onNode(hasText("September ${currentMonth.year - 1}")).assertExists()
        month.assertDoesNotExist()
    }

    @Test
    fun clickingActiveDayRendersHistoryDetailsAndClickingExerciseOpensEdit() {
        rule.setContent {
            ExerciseHistoryCalendar(
                uiState = exercise
            )
        }

        rule.onNode(hasText(LocalDate.now().dayOfMonth.toString())).performClick()

        date.assertExists()
        sets.assertExists()
        reps.assertExists()
        weight.assertExists()
        rest.assertExists()
        deleteButton.assertExists()

        sets.performClick()

        rule.onNode(hasText("Update Curls Workout")).assertExists()
    }

    @Test
    fun clickingDeleteIconOnHistoryDetailsOpensActionConfirmationForDeletion() {
        rule.setContent {
            ExerciseHistoryCalendar(
                uiState = exercise
            )
        }

        rule.onNode(hasText(LocalDate.now().dayOfMonth.toString())).performClick()

        date.assertExists()
        sets.assertExists()
        reps.assertExists()
        weight.assertExists()
        rest.assertExists()
        deleteButton.assertExists()

        deleteButton.performClick()

        rule.onNode(hasText("Do you want to delete this exercise?")).assertExists()
    }

    @Test
    fun rendersHistoryDetailsCard() {
        var delete = false

        rule.setContent {
            HistoryDetails(
                exerciseHistory = exerciseHistory,
                exercise = exercise.toExerciseUiState(),
                deleteFunction = { delete = true }
            )
        }

        sets.assertExists()
        reps.assertExists()
        weight.assertExists()
        rest.assertExists()
        deleteButton.assertExists()

        deleteButton.performClick()

        rule.onNode(hasText("Do you want to delete this exercise?")).assertExists()

        rule.onNode(hasText("Yes")).performClick()

        assertThat(delete, equalTo(true))
    }

    @Test
    fun rendersHistoryDetailsCardWithoutDeleteButton() {
        rule.setContent {
            HistoryDetails(
                exerciseHistory = exerciseHistory,
                exercise = exercise.toExerciseUiState(),
                deleteFunction = { },
                editEnabled = false
            )
        }

        sets.assertExists()
        reps.assertExists()
        weight.assertExists()
        rest.assertExists()
        deleteButton.assertDoesNotExist()
    }
}
