package com.example.gymtracker.ui.exercise

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

        val dropdownBox = rule.onNode(hasText("", substring = true) and !hasParent(hasScrollAction()))
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
}