package com.cst338.cst438_p1

import android.content.Context
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.cst338.cst438_p1.database.AppDatabase
import com.cst338.cst438_p1.database.User
import com.cst338.cst438_p1.database.dao.UserDao
import com.cst338.cst438_p1.ui.theme.AppTheme
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNull
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class SignupScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    private lateinit var db: AppDatabase
    private lateinit var userDao: UserDao

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()

        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()
        userDao = db.userDao()

    }

    @After
    fun teardown() {
        db.close()
    }

    @Test
    fun signupPass() = runTest {
        var signupCheck = false

        composeTestRule.setContent {
            AppTheme {
                SignupScreen(onSignupComplete = { signupCheck = true })
            }
        }
        composeTestRule.onNodeWithText("Enter new username").performTextInput("new_user")
        composeTestRule.onNodeWithText("Enter new password").performTextInput("secure_pass")

        composeTestRule.onNodeWithText("Complete Sign Up").performClick()
        composeTestRule.waitUntil { signupCheck }

        assert(signupCheck)
    }

    @Test
    fun signupDupUser() = runTest {
        userDao.insert(User(uid = 1, username = "existing_user", password = "password"))

        composeTestRule.setContent {
            AppTheme {
                SignupScreen(onSignupComplete = { })
            }
        }

        composeTestRule.onNodeWithText("Enter new username").performTextInput("existing_user")
        composeTestRule.onNodeWithText("Enter new password").performTextInput("password123")
        composeTestRule.onNodeWithText("Complete Sign Up").performClick()

        composeTestRule.waitForIdle()

        assertNull(userDao.getUserById(2))
        assertEquals(1, userDao.getAll().size)
    }

    @Test
    fun signupEmpty() {
        var signupCheck = false

        composeTestRule.setContent {
            AppTheme {
                SignupScreen(onSignupComplete = { signupCheck = true })
            }
        }
        composeTestRule.onNodeWithText("Complete Sign Up").performClick()
        composeTestRule.waitForIdle()
        assert(!signupCheck)
    }

    @Test
    fun signupUi() {
        composeTestRule.setContent {
            AppTheme {
                SignupScreen(onSignupComplete = {})
            }
        }
        composeTestRule.onNodeWithText("Create account").assertExists()
        composeTestRule.onNodeWithText("Enter new username").assertExists()
        composeTestRule.onNodeWithText("Enter new password").assertExists()
        composeTestRule.onNodeWithText("Complete Sign Up").assertExists()
    }
}