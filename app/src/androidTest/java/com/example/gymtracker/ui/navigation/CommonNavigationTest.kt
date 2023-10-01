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
    private val deleteButton = rule.onNode(hasContentDescription("Delete feature"))

    @Test
    fun backButtonDoesNotExistsAndEditButtonDoesNotExistWhenDisabled() {
        rule.setContent { TopBar(text = "text test", backEnabled = false, editEnabled = false, deleteEnabled = false) }

        backButton.assertDoesNotExist()
        editButton.assertDoesNotExist()
        deleteButton.assertDoesNotExist()
    }

    @Test
    fun backButtonExistsWhenBackEnabled() {
        rule.setContent { TopBar(text = "text test", backEnabled = true, editEnabled = false, deleteEnabled = false) }

        backButton.assertExists()
        editButton.assertDoesNotExist()
        deleteButton.assertDoesNotExist()
    }

    @Test
    fun clickingBackButtonExecutesNavigateBack() {
        var clicked = false
        rule.setContent { TopBar(text = "text test", backEnabled = true, navigateBack = { clicked = true }, editEnabled = false, deleteEnabled = false) }

        backButton.performClick()

        assertThat(clicked, equalTo(true))
    }

    @Test
    fun editButtonExistsWhenEditEnabled() {
        rule.setContent { TopBar(text = "text test", backEnabled = false, editEnabled = true, deleteEnabled = false) }

        backButton.assertDoesNotExist()
        editButton.assertExists()
        deleteButton.assertDoesNotExist()
    }

    @Test
    fun clickingEditButtonExecutesEditFunction() {
        var clicked = false
        rule.setContent { TopBar(text = "text test", backEnabled = false, editEnabled = true, editFunction = { clicked = true }, deleteEnabled = false) }

        editButton.performClick()

        assertThat(clicked, equalTo(true))
    }

    @Test
    fun deleteButtonExistsWhenDeleteEnabled() {
        rule.setContent { TopBar(text = "text test", backEnabled = false, editEnabled = false, deleteEnabled = true) }

        backButton.assertDoesNotExist()
        editButton.assertDoesNotExist()
        deleteButton.assertExists()
    }

    @Test
    fun clickingDeleteButtonExecutesDeleteFunction() {
        var clicked = false
        rule.setContent { TopBar(text = "text test", backEnabled = false, editEnabled = false, deleteEnabled = true, deleteFunction = { clicked = true }) }

        deleteButton.performClick()

        assertThat(clicked, equalTo(true))
    }
}