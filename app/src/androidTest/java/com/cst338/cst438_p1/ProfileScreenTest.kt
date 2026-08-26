package com.cst338.cst438_p1

import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.cst338.cst438_p1.database.User
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ProfileScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Before
    fun setUp() {
        composeTestRule.setContent {
            ProfileScreen(
                User(1, "User", "password")
            )
        }
    }

    @Test
    fun testIfUsernameDisplayed() {
        composeTestRule.onNodeWithText("Hello", substring = true).assertTextEquals("Hello User!")
    }
}