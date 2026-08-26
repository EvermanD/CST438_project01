package com.cst338.cst438_p1

import android.content.Context
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.cst338.cst438_p1.database.AppDatabase
import com.cst338.cst438_p1.database.User
import com.cst338.cst438_p1.database.dao.UserDao
import com.cst338.cst438_p1.ui.theme.AppTheme
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LoginscreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private lateinit var db: AppDatabase
    private lateinit var userDao: UserDao


    @Before
    fun setup() {
        Intents.init()

        val context = ApplicationProvider.getApplicationContext<Context>()

        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()
        userDao = db.userDao()

    }

    @After
    fun teardown() {
        Intents.release()
        db.close()
    }

    @Test
    fun signupButton() {
        composeTestRule.setContent {
            AppTheme {
                LoginScreen()
            }
        }

        composeTestRule.onNodeWithText("Sign up!").performClick()
        Intents.intended(hasComponent(SignupActivity::class.java.name))
    }

    @Test
    fun loginEmpty() {
        composeTestRule.setContent {
            AppTheme {
                LoginScreen()
            }
        }

        composeTestRule.onNodeWithTag("test_login_button").performClick()
        assert(Intents.getIntents().isEmpty())
    }

    @Test
    fun devSkip() {
        composeTestRule.setContent {
            AppTheme {
                LoginScreen()
            }
        }

        composeTestRule.onNodeWithText("Dev: Skip Login").performClick()
        composeTestRule.waitForIdle()
        Intents.intended(hasComponent(HomeActivity::class.java.name))
    }

    @Test
    fun logingWrongPassword() = runTest {
        composeTestRule.setContent {
            AppTheme {
                LoginScreen()
            }
        }
        userDao.insert(User(uid = 1, username = "testuser", password = "password123"))

        composeTestRule.onNodeWithText("Username").performTextInput("testuser")
        composeTestRule.onNodeWithText("password").performTextInput("wrong_pass")
        composeTestRule.onNodeWithTag("test_login_button").performClick()

        composeTestRule.waitForIdle()
        assert(Intents.getIntents().isEmpty())
    }


}


