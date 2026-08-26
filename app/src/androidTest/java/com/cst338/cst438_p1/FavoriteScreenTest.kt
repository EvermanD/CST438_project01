package com.cst338.cst438_p1

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.cst338.cst438_p1.database.Joke
import com.cst338.cst438_p1.database.User
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class FavoriteScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Before
    fun setUp() {
        val jokes = listOf<Joke>(
            Joke(
                "h39UfibMJBd",
                "Did you hear about the cheese who saved the world? It was Legend-dairy!"
            ),
            Joke(
                "uszdNZ8MRCd",
                "My new thesaurus is terrible. In fact, it's so bad, I'd say it's terrible."
            )
        )

        composeTestRule.setContent {
            FavoriteScreen(
                User(1, "User", "password"),
                jokes
            )
        }
    }

    @Test
    fun testIfJokesPrinted() {
        //Test if both jokes are printed
        composeTestRule.onNodeWithText("Did you hear about", substring = true).assertExists()
        composeTestRule.onNodeWithText("My new thesaurus", substring = true).assertExists()
    }

}