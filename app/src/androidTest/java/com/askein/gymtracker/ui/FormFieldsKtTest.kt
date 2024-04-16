package com.askein.gymtracker.ui

import androidx.activity.ComponentActivity
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.hasParent
import androidx.compose.ui.test.hasScrollAction
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.text.input.TextFieldValue
import com.askein.gymtracker.R
import com.askein.gymtracker.enums.FormTypes
import com.askein.gymtracker.getResourceString
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Rule
import org.junit.Test

class FormFieldsKtTest {

    @get:Rule
    val rule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun shouldUpdateTextInTextField() {
        var enteredText = ""
        rule.setContent {
            FormInformationField(
                label = R.string.close,
                value = "",
                onChange = { enteredText = it },
                formType = FormTypes.STRING,
            )
        }

        val textField = rule.onNode(hasContentDescription(getResourceString(R.string.close)))
        textField.performTextInput("Test text input")
        assertThat(enteredText, equalTo("Test text input"))
    }

    @Test
    fun shouldDisplayErrorMessageWhenError() {
        rule.setContent {
            FormInformationField(
                label = R.string.close,
                value = "",
                onChange = { },
                formType = FormTypes.INTEGER,
                error = true,
                errorMessage = R.string.default_error
            )
        }

        val errorMessage = rule.onNode(hasText(getResourceString(R.string.default_error)))
        errorMessage.assertExists()
    }

    @Test
    fun shouldOnlyAllowIntegersForIntegerForm() {
        var enteredText = ""
        rule.setContent {
            var localEnteredText by remember { mutableStateOf("") }
            FormInformationField(
                label = R.string.close,
                value = localEnteredText,
                onChange = {
                    enteredText = it
                    localEnteredText = it
                },
                formType = FormTypes.INTEGER
            )
        }

        val textField = rule.onNode(hasContentDescription(getResourceString(R.string.close)))
        textField.performTextInput("1")
        textField.performTextInput("a")
        textField.performTextInput("-")
        assertThat(enteredText, equalTo("1"))
    }

    @Test
    fun shouldOnlyAllowIntegersAndNegativeAtStartForIntegerForm() {
        var enteredText = ""
        rule.setContent {
            var localEnteredText by remember { mutableStateOf("") }
            FormInformationField(
                label = R.string.close,
                value = localEnteredText,
                onChange = {
                    enteredText = it
                    localEnteredText = it
                },
                formType = FormTypes.INTEGER
            )
        }

        val textField = rule.onNode(hasContentDescription(getResourceString(R.string.close)))
        textField.performTextInput("-")
        textField.performTextInput("1")
        textField.performTextInput("a")
        textField.performTextInput("-")
        assertThat(enteredText, equalTo("-1"))
    }

    @Test
    fun shouldOnlyAllowValidDecimalsAndNegativeAtStartForDecimalForm() {
        var enteredText = ""
        rule.setContent {
            var localEnteredText by remember { mutableStateOf("") }
            FormInformationField(
                label = R.string.close,
                value = localEnteredText,
                onChange = {
                    enteredText = it
                    localEnteredText = it
                },
                formType = FormTypes.DOUBLE
            )
        }

        val textField = rule.onNode(hasContentDescription(getResourceString(R.string.close)))
        textField.performTextInput("-")
        textField.performTextInput("1")
        textField.performTextInput("a")
        textField.performTextInput("-")
        textField.performTextInput(",")
        textField.performTextInput("b")
        textField.performTextInput("8")
        assertThat(enteredText, equalTo("-1.8"))
    }

    @Test
    fun shouldNotGiveSuggestionsForTextLessThanTwoCharacters() {
        var enteredText = TextFieldValue(text = "")
        rule.setContent {
            FormInformationFieldWithSuggestions(
                label = R.string.close,
                value = enteredText,
                onChange = { enteredText = it },
                suggestions = listOf("Biceps", "Bicycle", "Bismuth")
            )
        }

        val textField = rule.onNode(hasText("", substring = true) and !hasParent(hasScrollAction()))
        textField.performTextInput("B")
        assertThat(enteredText.text, equalTo("B"))

        rule.onNode(hasText("Biceps")).assertDoesNotExist()
        rule.onNode(hasText("Bicycle")).assertDoesNotExist()
        rule.onNode(hasText("Bismuth")).assertDoesNotExist()
    }

    @Test
    fun shouldGiveSuggestionsForTextTwoCharactersAndAboveThatMatchStart() {
        rule.setContent {
            var enteredText by remember { mutableStateOf(TextFieldValue(text = "")) }
            FormInformationFieldWithSuggestions(
                label = R.string.close,
                value = enteredText,
                onChange = { enteredText = it },
                suggestions = listOf("Biceps", "Bicycle", "Bismuth")
            )
        }

        val textField = rule.onNode(hasText("", substring = true) and !hasParent(hasScrollAction()))
        textField.performClick()
        textField.performTextInput("Bi")

        var biceps = rule.onNode(hasText("Biceps"))
        var bicycle = rule.onNode(hasText("Bicycle"))
        var bismuth = rule.onNode(hasText("Bismuth"))

        biceps.assertExists()
        bicycle.assertExists()
        bismuth.assertExists()

        textField.performTextInput("c")

        biceps = rule.onNode(hasText("Biceps"))
        bicycle = rule.onNode(hasText("Bicycle"))
        bismuth = rule.onNode(hasText("Bismuth"))

        biceps.assertExists()
        bicycle.assertExists()
        bismuth.assertDoesNotExist()
    }

    @Test
    fun clickingSuggestionsEntersSuggestionInToTextFieldValue() {
        var enteredText by mutableStateOf(TextFieldValue(text = ""))
        rule.setContent {
            FormInformationFieldWithSuggestions(
                label = R.string.close,
                value = enteredText,
                onChange = { enteredText = it },
                suggestions = listOf("Biceps", "Bicycle", "Bismuth")
            )
        }

        val textField = rule.onNode(hasText("", substring = true) and !hasParent(hasScrollAction()))
        textField.performClick()
        textField.performTextInput("Bi")

        var biceps = rule.onNode(hasText("Biceps"))

        biceps.performClick()

        assertThat(enteredText.text, equalTo("Biceps"))

        biceps = rule.onNode(hasText("Biceps") and hasParent(hasScrollAction()))
        val bicycle = rule.onNode(hasText("Bicycle"))
        val bismuth = rule.onNode(hasText("Bismuth"))

        biceps.assertExists()
        bicycle.assertDoesNotExist()
        bismuth.assertDoesNotExist()
    }

    @Test
    fun shouldOnlyDisplayFirstThreeSuggestionsAlphabetically() {
        rule.setContent {
            var enteredText by remember { mutableStateOf(TextFieldValue(text = "")) }
            FormInformationFieldWithSuggestions(
                label = R.string.close,
                value = enteredText,
                onChange = { enteredText = it },
                suggestions = listOf("Biceps", "Bicycle", "Bismuth", "Biscuits")
            )
        }

        val textField = rule.onNode(hasText("", substring = true) and !hasParent(hasScrollAction()))
        textField.performClick()
        textField.performTextInput("Bi")

        val biceps = rule.onNode(hasText("Biceps"))
        val bicycle = rule.onNode(hasText("Bicycle"))
        val bismuth = rule.onNode(hasText("Bismuth"))
        val biscuits = rule.onNode(hasText("Biscuits"))

        biceps.assertExists()
        bicycle.assertExists()
        bismuth.assertDoesNotExist()
        biscuits.assertExists()
    }

    @Test
    fun shouldRenderFormTimeField() {
        var minutes = ""
        var seconds = ""
        rule.setContent {
            FormTimeField(
                minutes = minutes,
                seconds = seconds,
                minutesOnChange = { minutes = it },
                secondsOnChange = { seconds = it }
            )
        }

        rule.onNode(hasContentDescription("Minutes")).assertExists()
        rule.onNode(hasContentDescription("Seconds")).assertExists()
    }

    @Test
    fun shouldUpdateStateWhenUserEntersValue() {
        var minutes = ""
        var seconds = ""
        rule.setContent {
            FormTimeField(
                minutes = "",
                seconds = "",
                minutesOnChange = { minutes = it },
                secondsOnChange = { seconds = it }
            )
        }

        rule.onNode(hasContentDescription("Minutes")).performTextInput("30")
        assertThat(minutes, equalTo("30"))
        rule.onNode(hasContentDescription("Seconds")).performTextInput("60")
        assertThat(seconds, equalTo("60"))
    }

    @Test
    fun shouldDisplayErrorWhenSecondsValueInvalid() {
        rule.setContent {
            FormTimeField(
                minutes = "",
                seconds = "60",
                minutesOnChange = { },
                secondsOnChange = { }
            )
        }

        rule.onNode(hasContentDescription("Seconds"))
            .assertTextContains("Value must be between 0 and 59")
    }

    @Test
    fun shouldUpdateValueSelectedInDropdown() {
        var selected = 0
        rule.setContent {
            DropdownBox(
                options = listOf(R.string.kilograms_display_name, R.string.pounds_display_name),
                onChange = { selected = it }
            )
        }

        val kilograms = getResourceString(R.string.kilograms_display_name)
        val pounds = getResourceString(R.string.pounds_display_name)

        val dropdownBox =
            rule.onNode(hasText("", substring = true) and !hasParent(hasScrollAction()))
        val firstOption = rule.onNode(hasText(kilograms) and hasParent(hasScrollAction()))
        val secondOption = rule.onNode(hasText(pounds) and hasParent(hasScrollAction()))

        dropdownBox.assertTextEquals(kilograms)
        firstOption.assertDoesNotExist()
        secondOption.assertDoesNotExist()

        dropdownBox.performClick()

        firstOption.assertExists()
        secondOption.assertExists()

        secondOption.performClick()

        firstOption.assertDoesNotExist()
        secondOption.assertDoesNotExist()
        dropdownBox.assertTextEquals(pounds)
        assertThat(selected, equalTo(R.string.pounds_display_name))
    }

    @Test
    fun shouldRenderDropdownWithSelectedChosenIfProvided() {
        rule.setContent {
            DropdownBox(
                options = listOf(
                    R.string.miles_display_name,
                    R.string.kilometers_display_name,
                    R.string.meters_display_name
                ),
                onChange = { },
                selected = R.string.meters_display_name
            )
        }

        rule.onNode(hasText(getResourceString(R.string.meters_display_name))).assertExists()
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
}
