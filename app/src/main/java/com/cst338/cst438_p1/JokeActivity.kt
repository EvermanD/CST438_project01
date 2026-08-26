package com.cst338.cst438_p1

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.lifecycleScope
import com.cst338.cst438_p1.database.AppDatabase
import com.cst338.cst438_p1.ui.theme.AppTheme
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class JokeActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        val userIdKey = "CST438P1.UserId.Key"
//        val userId = intent.getIntExtra(userIdKey, -1)

        val sessionManager = SessionManager(this)
        val db = AppDatabase.getDatabase(this, lifecycleScope)
        val userDao = db.userDao()
        val jokeDao = db.jokeDao()
        val favoriteDao = db.favoriteDao()

        lifecycleScope.launch {
            val loggedInUserId: Int? = sessionManager.userId.first()

            if (loggedInUserId == null) {
                val intent = Intent(this@JokeActivity, JokeActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                }
                startActivity(intent)
                return@launch
            }

            setContent {
                AppTheme {
                    JokeScreen(
                        userId = loggedInUserId,
                        jokeDao = jokeDao,
                        favoriteDao = favoriteDao,
                        onBack = { finish() }
                    )
                }
            }
        }
    }
}
