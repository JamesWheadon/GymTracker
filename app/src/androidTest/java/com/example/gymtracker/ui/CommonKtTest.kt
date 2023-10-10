package com.example.gymtracker.ui

import androidx.activity.ComponentActivity
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.hasAnySibling
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.hasParent
import androidx.compose.ui.test.hasScrollAction
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onParent
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.greaterThan
import org.hamcrest.Matchers.greaterThanOrEqualTo
import org.hamcrest.Matchers.lessThan
import org.junit.Rule
import org.junit.Test
import java.time.DayOfWeek
import java.time.Year
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale
import kotlin.math.abs

class CommonKtTest {

    private val months = listOf(
        "Jan",
        "Feb",
        "Mar",
        "Apr",
        "May",
        "Jun",
        "Jul",
        "Aug",
        "Sep",
        "Oct",
        "Nov",
        "Dec"
    )

    @get:Rule
    val rule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun shouldUpdateTextInTextField() {
        var enteredText = ""
        rule.setContent {
            ExerciseInformationField(
                label = "Test Field",
                value = "",
                onChange = { enteredText = it }
            )
        }

        val textField = rule.onNode(hasContentDescription("Test Field"))
        textField.performTextInput("Test text input")
        assertThat(enteredText, equalTo("Test text input"))
    }

    @Test
    fun shouldUpdateValueSelectedInDropdown() {
        var selected = ""
        rule.setContent {
            DropdownBox(
                options = listOf("first", "second"),
                onChange = { selected = it }
            )
        }

        val dropdownBox =
            rule.onNode(hasText("", substring = true) and !hasParent(hasScrollAction()))
        val firstOption = rule.onNode(hasText("first") and hasParent(hasScrollAction()))
        val secondOption = rule.onNode(hasText("second") and hasParent(hasScrollAction()))

        dropdownBox.assertTextEquals("first")
        firstOption.assertDoesNotExist()
        secondOption.assertDoesNotExist()

        dropdownBox.performClick()

        firstOption.assertExists()
        secondOption.assertExists()

        secondOption.performClick()

        firstOption.assertDoesNotExist()
        secondOption.assertDoesNotExist()
        dropdownBox.assertTextEquals("second")
        assertThat(selected, equalTo("second"))
    }

    @Test
    fun shouldRenderActionConfirmationCardWithTitleYesNoOptionsAndCloseButton() {
        rule.setContent {
            ActionConfirmation(
                actionTitle = "Test Title",
                confirmFunction = { },
                cancelFunction = { }
            )
        }

        rule.onNode(hasText("Test Title")).assertExists()
        rule.onNode(hasText("Yes")).assertExists()
        rule.onNode(hasText("No")).assertExists()
        rule.onNode(hasContentDescription("Close")).assertExists()
    }

    @Test
    fun actionConfirmationClickYesCallsConfirmFunction() {
        var yesClicked = false
        var noClicked = false
        rule.setContent {
            ActionConfirmation(
                actionTitle = "Test Title",
                confirmFunction = { yesClicked = true },
                cancelFunction = { noClicked = true }
            )
        }

        rule.onNode(hasText("Yes")).performClick()

        assertThat(yesClicked, equalTo(true))
        assertThat(noClicked, equalTo(false))
    }

    @Test
    fun actionConfirmationClickNoCallsCancelFunction() {
        var yesClicked = false
        var noClicked = false
        rule.setContent {
            ActionConfirmation(
                actionTitle = "Test Title",
                confirmFunction = { yesClicked = true },
                cancelFunction = { noClicked = true }
            )
        }

        rule.onNode(hasText("No")).performClick()

        assertThat(yesClicked, equalTo(false))
        assertThat(noClicked, equalTo(true))
    }

    @Test
    fun actionConfirmationClickCloseButtonCallsCancelFunction() {
        var yesClicked = false
        var noClicked = false
        rule.setContent {
            ActionConfirmation(
                actionTitle = "Test Title",
                confirmFunction = { yesClicked = true },
                cancelFunction = { noClicked = true }
            )
        }

        rule.onNode(hasContentDescription("Close")).performClick()

        assertThat(yesClicked, equalTo(false))
        assertThat(noClicked, equalTo(true))
    }

    @Test
    fun daysOfWeekRendersAllDaysWithNamesInShortForm() {
        rule.setContent {
            DaysOfWeek()
        }

        val expectedDays = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")

        for (day in expectedDays) {
            rule.onNode(hasText(day)).assertExists()
        }
    }

    @Test
    fun calendarMonthRendersButtonForEachDAyInMonthInAscendingOrder() {
        rule.setContent {
            CalendarMonth(
                month = 10,
                year = 2023,
                activeDays = listOf(),
                dayFunction = { }
            )
        }

        val positionTolerance = 3
        var previousPosition = Offset(0f, 0f)
        for (day in 1..31) {
            val dayButton = rule.onNode(hasText(day.toString()))
            val dayPosition =
                dayButton.fetchSemanticsNode().layoutInfo.coordinates.boundsInRoot().topLeft
            dayButton.assertExists()
            if (day != 1) {
                assertThat(
                    dayPosition.y,
                    greaterThanOrEqualTo(previousPosition.y - positionTolerance)
                )
                if (abs(dayPosition.y - previousPosition.y) < positionTolerance) {
                    assertThat(dayPosition.x, greaterThan(previousPosition.x))
                } else {
                    assertThat(dayPosition.x, equalTo(0F))
                }
            }
            previousPosition = dayPosition
        }
        rule.onAllNodesWithText("", substring = true).assertCountEquals(31)
    }

    @Test
    fun calendarWeekRendersOneButtonAtRightEdgeWhenStartDayIsSunday() {
        rule.setContent {
            CalendarWeek(
                startDate = 1,
                activeDays = listOf(),
                dayFunction = { },
                startDayOfWeek = DayOfWeek.SUNDAY
            )
        }

        val day = rule.onNode(hasText("1"))
        val dayNode = day.fetchSemanticsNode()
        val dayParent = day.onParent().fetchSemanticsNode()
        day.assertExists()
        rule.onAllNodesWithText("", substring = true).assertCountEquals(1)
        assertThat(
            dayNode.positionInRoot.x + dayNode.size.width,
            equalTo(dayParent.positionInRoot.x + dayParent.size.width)
        )
    }

    @Test
    fun calendarWeekRendersOneButtonAtLeftEdgeWhenEndDayIsMonday() {
        rule.setContent {
            CalendarWeek(
                startDate = 1,
                activeDays = listOf(),
                dayFunction = { },
                endDayOfWeek = DayOfWeek.MONDAY
            )
        }

        val day = rule.onNode(hasText("1"))
        val dayNode = day.fetchSemanticsNode()
        day.assertExists()
        rule.onAllNodesWithText("", substring = true).assertCountEquals(1)
        assertThat(dayNode.positionInRoot.x, equalTo(0F))
    }

    @Test
    fun calendarWeekRendersTwoButtonsInAscendingOrder() {
        rule.setContent {
            CalendarWeek(
                startDate = 1,
                activeDays = listOf(),
                dayFunction = { },
                startDayOfWeek = DayOfWeek.SATURDAY
            )
        }

        val firstDay = rule.onNode(hasText("1"))
        val secondDay = rule.onNode(hasText("2"))
        firstDay.assertExists()
        secondDay.assertExists()
        rule.onAllNodesWithText("", substring = true).assertCountEquals(2)
        assertThat(
            firstDay.fetchSemanticsNode().positionInRoot.x,
            lessThan(secondDay.fetchSemanticsNode().positionInRoot.x)
        )
    }

    @Test
    fun calendarWeekRendersSevenButtonsWhenNoLimitsApplied() {
        rule.setContent {
            CalendarWeek(
                startDate = 1,
                activeDays = listOf(),
                dayFunction = { }
            )
        }

        rule.onAllNodesWithText("", substring = true).assertCountEquals(7)
    }

    @Test
    fun calendarWeekRendersOneEnabledOneDisabledButton() {
        val clicked = mutableListOf<Int>()

        rule.setContent {
            CalendarWeek(
                startDate = 1,
                activeDays = listOf(1),
                dayFunction = { day -> clicked.add(day) },
                startDayOfWeek = DayOfWeek.SATURDAY
            )
        }

        val firstDay = rule.onNode(hasText("1"))
        val secondDay = rule.onNode(hasText("2"))
        firstDay.assertExists()
        secondDay.assertExists()

        firstDay.assertIsEnabled()
        secondDay.assertIsNotEnabled()

        firstDay.performClick()
        secondDay.performClick()

        assertThat(clicked.size, equalTo(1))
        assertThat(clicked[0], equalTo(1))
    }

    @Test
    fun monthPickerRendersCurrentMonthAndYearAndSearchButton() {
        rule.setContent {
            MonthPicker(
                yearMonthValue = YearMonth.of(2023, 10),
                yearMonthValueOnChange = { }
            )
        }

        val month = rule.onNode(hasText("October"))
        val year = rule.onNode(hasText("2023"))
        val searchButton = rule.onNode(hasContentDescription("Change Month"))
        val backArrow = rule.onNode(hasContentDescription("Previous Year"))

        month.assertExists()
        year.assertExists()
        searchButton.assertExists()
        backArrow.assertDoesNotExist()
    }

    @Test
    fun monthPickerClickingSearchButtonRendersMonthChoices() {
        rule.setContent {
            MonthPicker(
                yearMonthValue = YearMonth.of(2023, 10),
                yearMonthValueOnChange = { }
            )
        }

        val month = rule.onNode(hasText("October"))
        val year = rule.onNode(hasText("2023") and hasAnySibling(hasText("October")))
        val searchButton = rule.onNode(hasContentDescription("Change Month"))
        val backArrow = rule.onNode(hasContentDescription("Previous Year"))

        backArrow.assertDoesNotExist()

        searchButton.performClick()

        month.assertExists()
        year.assertExists()
        searchButton.assertExists()
        backArrow.assertExists()
    }

    @Test
    fun yearMonthOptionsRendersCorrectButtonsAndMonths() {
        rule.setContent {
            YearMonthOptions(
                yearMonthValue = YearMonth.of(2022, 10),
                yearMonthValueOnChange = { },
                closeFunction = { }
            )
        }

        val year = rule.onNode(hasText("2022"))
        val backArrow = rule.onNode(hasContentDescription("Previous Year"))
        val forwardArrow = rule.onNode(hasContentDescription("Next Year"))
        val closeButton = rule.onNode(hasContentDescription("Close"))

        year.assertExists()
        backArrow.assertExists()
        forwardArrow.assertExists()
        closeButton.assertExists()
        for (month in months) {
            rule.onNode(hasText(month)).assertExists()
        }
    }

    @Test
    fun yearMonthOptionsPreviousToCurrentYearDoesNotChangeYearPosition() {
        rule.setContent {
            YearMonthOptions(
                yearMonthValue = YearMonth.of(Year.now().value - 1, 10),
                yearMonthValueOnChange = { },
                closeFunction = { }
            )
        }

        val yearPrevious = rule.onNode(hasText((Year.now().value - 1).toString()))
        val yearNow = rule.onNode(hasText((Year.now().value).toString()))
        val backArrow = rule.onNode(hasContentDescription("Previous Year"))
        val forwardArrow = rule.onNode(hasContentDescription("Next Year"))
        val closeButton = rule.onNode(hasContentDescription("Close"))

        val yearPosition = yearPrevious.fetchSemanticsNode().positionInRoot

        forwardArrow.performClick()

        yearPrevious.assertDoesNotExist()
        yearNow.assertExists()
        backArrow.assertExists()
        forwardArrow.assertIsNotEnabled()
        forwardArrow.performClick()
        yearNow.assertExists()
        closeButton.assertExists()

        for (month in months) {
            rule.onNode(hasText(month)).assertExists()
        }

        assertThat(yearPosition, equalTo(yearNow.fetchSemanticsNode().positionInRoot))
    }

    @Test
    fun yearMonthOptionsClickingMonthExceptCurrentCallsYearMonthValueOnChange() {
        var chosenYearMonth = YearMonth.now()

        rule.setContent {
            YearMonthOptions(
                yearMonthValue = YearMonth.of(Year.now().value - 1, 10),
                yearMonthValueOnChange = { chosen -> chosenYearMonth = chosen },
                closeFunction = { }
            )
        }

        val year = rule.onNode(hasText((Year.now().value - 1).toString()))
        val backArrow = rule.onNode(hasContentDescription("Previous Year"))
        val closeButton = rule.onNode(hasContentDescription("Close"))

        backArrow.assertExists()
        year.assertExists()
        closeButton.assertExists()

        for (month in months) {
            val monthNode = rule.onNode(hasText(month))
            monthNode.assertExists()
            monthNode.performClick()
            if (month != "Oct") {
                assertThat(
                    chosenYearMonth.month.getDisplayName(TextStyle.SHORT, Locale.ENGLISH),
                    equalTo(month)
                )
                assertThat(chosenYearMonth.year, equalTo(Year.now().value - 1))
            } else {
                assertThat(chosenYearMonth, equalTo(null))
            }
            chosenYearMonth = null
        }
    }

    @Test
    fun yearMonthOptionsClickingMonthAllFutureMonthsDisabled() {
        var chosenYearMonth = YearMonth.now()

        rule.setContent {
            YearMonthOptions(
                yearMonthValue = YearMonth.now(),
                yearMonthValueOnChange = { chosen -> chosenYearMonth = chosen },
                closeFunction = { }
            )
        }

        val year = rule.onNode(hasText((Year.now().value).toString()))
        val backArrow = rule.onNode(hasContentDescription("Previous Year"))
        val closeButton = rule.onNode(hasContentDescription("Close"))

        backArrow.assertExists()
        year.assertExists()
        closeButton.assertExists()

        for (month in 1..12) {
            val monthNode = rule.onNode(hasText(months[month - 1]))
            monthNode.assertExists()
            monthNode.performClick()
            if (month < YearMonth.now().month.value) {
                assertThat(
                    chosenYearMonth.month.getDisplayName(TextStyle.SHORT, Locale.ENGLISH),
                    equalTo(months[month - 1])
                )
                assertThat(chosenYearMonth.year, equalTo(Year.now().value))
            } else {
                assertThat(chosenYearMonth, equalTo(null))
            }
            chosenYearMonth = null
        }
    }
}