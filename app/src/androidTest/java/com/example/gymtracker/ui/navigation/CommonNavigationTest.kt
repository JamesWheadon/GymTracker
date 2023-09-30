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

    private val backButton = rule.onNode(hasContentDescription("Back Button"))
    private val editButton = rule.onNode(hasContentDescription("Edit feature"))

    @Test
    fun backButtonDoesNotExistsAndEditButtonDoesNotExistWhenDisabled() {
        rule.setContent { TopBar(text = "text test", backEnabled = false, editEnabled = false) }

        backButton.assertDoesNotExist()
        editButton.assertDoesNotExist()
    }

    @Test
    fun backButtonExistsWhenBackEnabled() {
        rule.setContent { TopBar(text = "text test", backEnabled = true, editEnabled = false) }

        backButton.assertExists()
    }

    @Test
    fun clickingBackButtonExecutesNavigateBack() {
        var clicked = false
        rule.setContent { TopBar(text = "text test", backEnabled = true, navigateBack = { clicked = true }, editEnabled = false) }

        backButton.performClick()

        assertThat(clicked, equalTo(true))
    }

    @Test
    fun editButtonExistsWhenEditEnabled() {
        rule.setContent { TopBar(text = "text test", backEnabled = false, editEnabled = true) }

        editButton.assertExists()
    }

    @Test
    fun clickingEditButtonExecutesEditFunction() {
        var clicked = false
        rule.setContent { TopBar(text = "text test", backEnabled = false, editEnabled = true, editFunction = { clicked = true }) }

        editButton.performClick()

        assertThat(clicked, equalTo(true))
    }
}