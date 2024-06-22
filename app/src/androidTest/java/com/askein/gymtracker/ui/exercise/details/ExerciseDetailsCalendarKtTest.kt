package com.askein.gymtracker.ui.exercise.details

import androidx.activity.ComponentActivity
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.performClick
import com.askein.gymtracker.enums.DistanceUnits
import com.askein.gymtracker.enums.WeightUnits
import com.askein.gymtracker.ui.exercise.ExerciseUiState
import com.askein.gymtracker.ui.exercise.history.state.CardioExerciseHistoryUiState
import com.askein.gymtracker.ui.exercise.history.state.WeightsExerciseHistoryUiState
import com.askein.gymtracker.ui.user.LocalUserPreferences
import com.askein.gymtracker.ui.user.UserPreferencesUiState
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
    private val chooseMonthButton = rule.onNode(hasContentDescription("change month"))
    private val previousYearButton = rule.onNode(hasContentDescription("previous year"))
    private val nextYearButton = rule.onNode(hasContentDescription("next year"))
    private val date = rule.onNode(
        hasText(
            "Exercises on ${
                LocalDate.now().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG))
            }"
        )
    )
    private val sets = rule.onNode(hasText("Sets: 1"))
    private val reps = rule.onNode(hasText("Reps: 2"))
    private val weight = rule.onNode(hasText("Weight: 13.00 kg"))
    private val poundsWeight = rule.onNode(hasText("Weight: 28.66 lb"))
    private val rest = rule.onNode(hasText("Rest time: 1 s"))
    private val time = rule.onNode(hasText("Time: 10:00"))
    private val distance = rule.onNode(hasText("Distance: 100.00 km"))
    private val distanceMeters = rule.onNode(hasText("Distance: 100000.00 m"))
    private val calories = rule.onNode(hasText("Calories: 2500 kcal"))
    private val deleteButton = rule.onNode(hasContentDescription("Delete history"))

    private val weightsExerciseHistory = WeightsExerciseHistoryUiState(
        id = 1,
        exerciseId = 1,
        date = LocalDate.now(),
        weight = listOf(13.0),
        sets = 1,
        reps = listOf(2),
        rest = 1
    )
    private val cardioExerciseHistory = CardioExerciseHistoryUiState(
        id = 1,
        exerciseId = 2,
        date = LocalDate.now(),
        minutes = 10,
        seconds = 0,
        distance = 100.0,
        calories = 2500
    )
    private val exercise = ExerciseDetailsUiState(
        exercise = ExerciseUiState(
            name = NAME,
            muscleGroup = MUSCLE_GROUP,
            equipment = EQUIPMENT,
        ),
        weightsHistory = listOf(weightsExerciseHistory),
        cardioHistory = listOf(cardioExerciseHistory)
    )

    @Test
    fun rendersExerciseHistoryCalendarWithOneActiveDay() {
        rule.setContent {
            ExerciseHistoryCalendar(
                uiState = exercise,
                chosenDate = null
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
                uiState = exercise,
                chosenDate = null
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
            val userPreferencesUiState = UserPreferencesUiState()
            CompositionLocalProvider(LocalUserPreferences provides userPreferencesUiState) {
                ExerciseHistoryCalendar(
                    uiState = exercise,
                    chosenDate = null
                )
            }
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
            val userPreferencesUiState = UserPreferencesUiState()
            CompositionLocalProvider(LocalUserPreferences provides userPreferencesUiState) {
                ExerciseHistoryCalendar(
                    uiState = exercise,
                    chosenDate = null
                )
            }
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
    fun rendersExerciseHistoryCalendarWithChosenDateSelected() {
        rule.setContent {
            val userPreferencesUiState = UserPreferencesUiState()
            CompositionLocalProvider(LocalUserPreferences provides userPreferencesUiState) {
                ExerciseHistoryCalendar(
                    uiState = exercise,
                    chosenDate = LocalDate.now()
                )
            }
        }

        date.assertExists()
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
    fun rendersWeightsExerciseHistoryDetailsCard() {
        var delete = false

        rule.setContent {
            val userPreferencesUiState = UserPreferencesUiState()
            CompositionLocalProvider(LocalUserPreferences provides userPreferencesUiState) {
                WeightsExerciseHistoryDetails(
                    exerciseHistory = weightsExerciseHistory,
                    deleteFunction = { delete = true },
                    editEnabled = true
                )
            }
        }

        sets.assertExists()
        reps.assertExists()
        weight.assertExists()
        rest.assertExists()
        deleteButton.assertExists()

        deleteButton.performClick()

        assertThat(delete, equalTo(true))
    }

    @Test
    fun rendersWeightsExerciseHistoryDetailsCardWithoutDeleteButton() {
        rule.setContent {
            val userPreferencesUiState = UserPreferencesUiState()
            CompositionLocalProvider(LocalUserPreferences provides userPreferencesUiState) {
                WeightsExerciseHistoryDetails(
                    exerciseHistory = weightsExerciseHistory,
                    deleteFunction = { },
                    editEnabled = false
                )
            }
        }

        sets.assertExists()
        reps.assertExists()
        weight.assertExists()
        rest.assertExists()
        deleteButton.assertDoesNotExist()
    }

    @Test
    fun rendersWeightsExerciseHistoryDetailsCardWithPoundsWeight() {
        rule.setContent {
            val userPreferencesUiState = UserPreferencesUiState(
                defaultWeightUnit = WeightUnits.POUNDS
            )
            CompositionLocalProvider(LocalUserPreferences provides userPreferencesUiState) {
                WeightsExerciseHistoryDetails(
                    exerciseHistory = weightsExerciseHistory,
                    deleteFunction = { },
                    editEnabled = false
                )
            }
        }

        sets.assertExists()
        reps.assertExists()
        poundsWeight.assertExists()
        rest.assertExists()
        deleteButton.assertDoesNotExist()
    }

    @Test
    fun rendersCardioExerciseHistoryDetailsCard() {
        var delete = false

        rule.setContent {
            val userPreferencesUiState = UserPreferencesUiState()
            CompositionLocalProvider(LocalUserPreferences provides userPreferencesUiState) {
                CardioExerciseHistoryDetails(
                    exerciseHistory = cardioExerciseHistory,
                    deleteFunction = { delete = true },
                    editEnabled = true
                )
            }
        }

        time.assertExists()
        distance.assertExists()
        calories.assertExists()
        deleteButton.assertExists()

        deleteButton.performClick()

        assertThat(delete, equalTo(true))
    }

    @Test
    fun rendersCardioExerciseHistoryDetailsCardWithoutDeleteButton() {
        rule.setContent {
            val userPreferencesUiState = UserPreferencesUiState()
            CompositionLocalProvider(LocalUserPreferences provides userPreferencesUiState) {
                CardioExerciseHistoryDetails(
                    exerciseHistory = cardioExerciseHistory,
                    deleteFunction = { },
                    editEnabled = false
                )
            }
        }

        time.assertExists()
        distance.assertExists()
        calories.assertExists()
        deleteButton.assertDoesNotExist()
    }

    @Test
    fun rendersCardioExerciseHistoryDetailsCardWithMetersDistance() {
        rule.setContent {
            val userPreferencesUiState = UserPreferencesUiState(
                defaultDistanceUnit = DistanceUnits.METERS
            )
            CompositionLocalProvider(LocalUserPreferences provides userPreferencesUiState) {
                CardioExerciseHistoryDetails(
                    exerciseHistory = cardioExerciseHistory,
                    deleteFunction = { },
                    editEnabled = false
                )
            }
        }

        time.assertExists()
        distanceMeters.assertExists()
        calories.assertExists()
        deleteButton.assertDoesNotExist()
    }
}
