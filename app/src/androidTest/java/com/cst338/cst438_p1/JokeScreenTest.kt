package com.cst338.cst438_p1

import android.content.Context
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.cst338.cst438_p1.database.AppDatabase
import com.cst338.cst438_p1.database.dao.FavoriteDao
import com.cst338.cst438_p1.database.dao.JokeDao
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class JokeScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private lateinit var db: AppDatabase
    private lateinit var favoriteDao: FavoriteDao
    private lateinit var jokeDao: JokeDao

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()

        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()
        favoriteDao = db.favoriteDao()
        jokeDao = db.jokeDao()

        composeTestRule.setContent {
            JokeScreen(
                userId = 1,
                favoriteDao = favoriteDao,
                jokeDao = jokeDao,
                onBack = { }
            )
        }
    }

    @Test
    fun jokeScreen_showsButtons() {
        composeTestRule.onNodeWithText("Back").assertIsDisplayed()
        composeTestRule.onNodeWithText("New Joke").assertIsDisplayed()
        composeTestRule.onNodeWithText("Favorite").assertIsDisplayed()
    }
}
