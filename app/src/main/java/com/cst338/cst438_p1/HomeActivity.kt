package com.cst338.cst438_p1

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.lifecycleScope
import com.cst338.cst438_p1.database.AppDatabase
import com.cst338.cst438_p1.database.User
import com.cst338.cst438_p1.ui.theme.AppTheme
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class HomeActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val db = AppDatabase.getDatabase(this, lifecycleScope)
        val userDao = db.userDao()
        val favoriteDao = db.favoriteDao()

//        val userIdKey = "CST438P1.UserId.Key"
//        val loggedInUserId = intent.getIntExtra(userIdKey, -1)

        val sessionManager = SessionManager(this)

        var user: User
        lifecycleScope.launch {
            val loggedInUserId: Int? = sessionManager.userId.first()

            if (loggedInUserId == null) {
                val intent = Intent(this@HomeActivity, LoginActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                }
                startActivity(intent)
                return@launch
            }

            user = userDao.getUserById(loggedInUserId)!!

            setContent {
                AppTheme {
                    HomeScreen(user, favoriteDao)
                }
            }
        }
    }
}

