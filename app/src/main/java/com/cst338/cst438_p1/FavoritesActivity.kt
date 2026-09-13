package com.cst338.cst438_p1

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import com.cst338.cst438_p1.database.AppDatabase
import com.cst338.cst438_p1.database.Joke
import com.cst338.cst438_p1.database.User
import com.cst338.cst438_p1.database.dao.FavoriteDao
import com.cst338.cst438_p1.database.dao.JokeDao
import com.cst338.cst438_p1.ui.theme.AppTheme
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@OptIn(androidx.compose.foundation.ExperimentalFoundationApi::class)
class FavoritesActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val db = AppDatabase.getDatabase(this, lifecycleScope)
        val userDao = db.userDao()
        val jokeDao = db.jokeDao()
        val favoriteDao = db.favoriteDao()

        // val userIdKey = "CST438P1.UserId.Key"
        // val loggedInUserId = intent.getIntExtra(userIdKey, -1)

        var user: User
        var jokes: List<Joke>

        val sessionManager = SessionManager(this)

        lifecycleScope.launch {
            val loggedInUserId: Int? = sessionManager.userId.first()

            if (loggedInUserId == null) {
                val intent = Intent(this@FavoritesActivity, LoginActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                }
                startActivity(intent)
                return@launch
            }

            //val uid: Int = loggedInUserId
            user = userDao.getUserById(loggedInUserId)!! // !! ?
            jokes = jokeDao.getJokeByUserId(loggedInUserId)

            setContent {
                AppTheme {
                    FavoriteScreen(user, jokes, jokeDao, favoriteDao)
                }
            }
        }
    }
}

@OptIn(
    ExperimentalMaterial3Api::class,
    androidx.compose.foundation.ExperimentalFoundationApi::class
)
@Composable
fun FavoriteScreen(user: User, jokes: List<Joke>, jokeDao: JokeDao, favoriteDao: FavoriteDao) {

    val context = LocalContext.current
    val userIdKey = "CST438P1.UserId.Key"
    val showDialog = remember { mutableStateOf(false) }
    val selectedJoke = remember { mutableStateOf<String?>(null) }
    val jokeList = remember { mutableStateOf(jokes) }
    val scope = rememberCoroutineScope()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text("Favorites") },
                navigationIcon = {
                    IconButton(onClick = {
                        val intent = Intent(context, HomeActivity::class.java)
                        intent.putExtra(userIdKey, user.uid)
                        context.startActivity(intent)
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = null
                        )
                    }
                },
                colors = topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            userScrollEnabled = true
        ) {
            for (i in jokeList.value) {
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .combinedClickable(
                                onClick = { },
                                onLongClick = {
                                    showDialog.value = true
                                    selectedJoke.value = i.jokeId
                                }
                            ),
                        border = BorderStroke(1.dp, color = Color.Black)
                    ) {
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            Box(
                                modifier = Modifier.fillMaxWidth(0.95f),
                                contentAlignment = Alignment.Center
                            )
                            {
                                Text(
                                    i.joke,
                                    textAlign = TextAlign.Center,
                                    color = Color.Black
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    if (showDialog.value) {
        BasicAlertDialog(
            onDismissRequest = {
                showDialog.value = false
                selectedJoke.value = null
            },
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                shape = MaterialTheme.shapes.large
            ) {
                Column(
                    modifier = Modifier.padding(24.dp)
                ) {
                    Text("Delete joke?")
                    Spacer(modifier = Modifier.height(24.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    )
                    {
                        TextButton(
                            onClick = {
                                showDialog.value = false
                                selectedJoke.value = null
                            }
                        ) {
                            Text("Dismiss")
                        }
                        TextButton(
                            onClick = {
                                selectedJoke.value?.let { jokeId ->
                                    scope.launch {
                                        favoriteDao.deleteFavorite(
                                            user.uid,
                                            jokeId
                                        )
                                        val updatedJokes = jokeDao.getJokeByUserId(user.uid)
                                        jokeList.value = updatedJokes
                                    }
                                }
                                selectedJoke.value = null
                                showDialog.value = false
                            },
                        ) {
                            Text("Confirm")
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FavoriteScreenPreview() {
//    AppTheme {
//        val jokes = listOf<Joke>(
//            Joke(
//                "uszdNZ8MRCd",
//                "My new thesaurus is terrible. In fact, it's so bad, I'd say it's terrible."
//            ),
//            Joke(
//                "KeqOmOZDYDd",
//                "Why is it so windy inside an arena? All those fans."
//            )
//        )
//        FavoriteScreen(
//            User(0, "User", "password"),
//            jokes,
//        )
//    }
}