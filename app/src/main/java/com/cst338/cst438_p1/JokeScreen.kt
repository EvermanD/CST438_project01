package com.cst338.cst438_p1

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.cst338.cst438_p1.api.RetrofitClient
import com.cst338.cst438_p1.database.Favorite
import com.cst338.cst438_p1.database.Joke
import com.cst338.cst438_p1.database.dao.FavoriteDao
import com.cst338.cst438_p1.database.dao.JokeDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JokeScreen(
    userId: Int,
    jokeDao: JokeDao,
    favoriteDao: FavoriteDao,
    onBack: () -> Unit
) {
    var isLoading by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }
    var jokeText by remember { mutableStateOf<String?>(null) }
    var jokeId by remember { mutableStateOf<String?>(null) }
    var favMsg by remember { mutableStateOf<String?>(null) }

    val scope = rememberCoroutineScope()

    suspend fun fetchJoke() {
        isLoading = true
        error = null
        favMsg = null

        try {
            val response = withContext(Dispatchers.IO) {
                RetrofitClient.dadJokeApi.getRandomJoke()
            }

            if (response.isSuccessful) {
                val body = response.body()
                jokeId = body?.id
                jokeText = body?.joke ?: "No joke in response."
            } else {
                error = "HTTP ${response.code()}: ${response.message()}"
            }
        } catch (e: Exception) {
            error = e.message ?: "Unknown error"
        } finally {
            isLoading = false
        }
    }

    LaunchedEffect(Unit) {
        fetchJoke()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                colors = topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                ),
                title = {
                    Text("Otter-ly Hilarious!")
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            when {
                isLoading -> Text("Loading…")
                error != null -> Text("Error: $error")
                jokeText != null -> Text(jokeText!!)
                else -> Text("No joke loaded.")
            }

            favMsg?.let { Text(it) }

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Button(onClick = onBack) { Text("Back") }

                Button(onClick = { scope.launch { fetchJoke() } }) {
                    Text("New Joke")
                }

                Button(
                    enabled = userId != -1 && jokeId != null && jokeText != null,
                    onClick = {
                        scope.launch {
                            val id = jokeId ?: return@launch
                            val text = jokeText ?: return@launch
                            val result = withContext(Dispatchers.IO) {
                                jokeDao.insert(Joke(jokeId = id, joke = text))
                                favoriteDao.insert(Favorite(uid = userId, jokeId = id))
                            }
                            favMsg = if (result == -1L) "Already in favorites"
                            else "Saved to favorites!"
                        }
                    }
                ) {
                    Text("Favorite")
                }
            }
        }
    }
}

