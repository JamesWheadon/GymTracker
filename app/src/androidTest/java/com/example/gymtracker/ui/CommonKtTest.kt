package com.example.gymtracker.ui

import androidx.activity.ComponentActivity
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
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
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Rule
import org.junit.Test

class CommonKtTest {

    @get:Rule
    val rule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun shouldUpdateTextInTextField() {
        var enteredText = ""
        rule.setContent {
            FormInformationField(
                label = "Test Field",
                value = "",
                onChange = { enteredText = it },
                keyboardOptions = KeyboardOptions.Default
            )
        }

        val textField = rule.onNode(hasContentDescription("Test Field"))
        textField.performTextInput("Test text input")
        assertThat(enteredText, equalTo("Test text input"))
    }

    @Test
    fun shouldDisplayErrorMessageWhenError() {
        rule.setContent {
            FormInformationField(
                label = "Test Field",
                value = "",
                onChange = { },
                keyboardOptions = KeyboardOptions.Default,
                error = true,
                errorMessage = "Test error message"
            )
        }

        val errorMessage = rule.onNode(hasText("Test error message"))
        errorMessage.assertExists()
    }

    @Test
    fun shouldNotGiveSuggestionsForTextLessThanTwoCharacters() {
        var enteredText = TextFieldValue(text = "")
        rule.setContent {
            FormInformationFieldWithSuggestions(
                label = "Test Field",
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
        var enteredText by mutableStateOf(TextFieldValue(text = ""))

        rule.setContent {
            FormInformationFieldWithSuggestions(
                label = "Test Field",
                value = enteredText,
                onChange = { enteredText = it },
                suggestions = listOf("Biceps", "Bicycle", "Bismuth")
            )
        }

        val textField = rule.onNode(hasText("", substring = true) and !hasParent(hasScrollAction()))
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
                label = "Test Field",
                value = enteredText,
                onChange = { enteredText = it },
                suggestions = listOf("Biceps", "Bicycle", "Bismuth")
            )
        }

        val textField = rule.onNode(hasText("", substring = true) and !hasParent(hasScrollAction()))
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
        var enteredText by mutableStateOf(TextFieldValue(text = ""))
        rule.setContent {
            FormInformationFieldWithSuggestions(
                label = "Test Field",
                value = enteredText,
                onChange = { enteredText = it },
                suggestions = listOf("Biceps", "Bicycle", "Bismuth", "Biscuits")
            )
        }

        val textField = rule.onNode(hasText("", substring = true) and !hasParent(hasScrollAction()))
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
}
