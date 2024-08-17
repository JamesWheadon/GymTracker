package com.askein.gymtracker.ui

import androidx.activity.ComponentActivity
import androidx.compose.material3.Text
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.performClick
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.junit.Rule
import org.junit.Test

class DialogScreenWrapperKtTest {

    @get:Rule
    val rule = createAndroidComposeRule<ComponentActivity>()

    private val closeButton = rule.onNode(hasContentDescription("Close"))

    @Test
    fun rendersDialogScreenWrapper() {
        rule.setContent {
            DialogScreenWrapper(onDismiss = { }) {
                Text(text = "Testing exists")
            }
        }

        closeButton.assertExists()
        rule.onNode(hasText("Testing exists")).assertExists()
    }

    @Test
    fun clickCloseButtonToDismiss() {
        var dismissed = false
        rule.setContent {
            DialogScreenWrapper(onDismiss = { dismissed = true }) {

            }
        }

        closeButton.performClick()

        MatcherAssert.assertThat(dismissed, CoreMatchers.equalTo(true))
    }
}