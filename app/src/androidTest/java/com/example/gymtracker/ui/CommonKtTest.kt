package com.example.gymtracker.ui

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.hasParent
import androidx.compose.ui.test.hasScrollAction
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
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
}