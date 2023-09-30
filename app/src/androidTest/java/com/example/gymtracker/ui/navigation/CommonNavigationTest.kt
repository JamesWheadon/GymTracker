package com.example.gymtracker.ui.navigation

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.performClick
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Rule
import org.junit.Test

class CommonNavigationTest {

    @get:Rule
    val rule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun backButtonExistsWhenBackEnabled() {
        rule.setContent { TopBar(text = "text test", backEnabled = true) }

        rule.onNode(hasContentDescription("Back Button")).assertExists()
    }

    @Test
    fun backButtonDoesNotExistsWhenBackDisabled() {
        rule.setContent { TopBar(text = "text test", backEnabled = false) }

        rule.onNode(hasContentDescription("Back Button")).assertDoesNotExist()
    }

    @Test
    fun clickingBackButtonExecutesBackButton() {
        var clicked = false
        rule.setContent { TopBar(text = "text test", backEnabled = true, navigateBack = { clicked = true }) }

        rule.onNode(hasContentDescription("Back Button")).performClick()

        assertThat(clicked, equalTo(true))
    }
}