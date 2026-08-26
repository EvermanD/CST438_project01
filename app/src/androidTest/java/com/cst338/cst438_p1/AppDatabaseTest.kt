package com.cst338.cst438_p1

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.cst338.cst438_p1.database.AppDatabase
import com.cst338.cst438_p1.database.Favorite
import com.cst338.cst438_p1.database.Joke
import com.cst338.cst438_p1.database.User
import com.cst338.cst438_p1.database.dao.FavoriteDao
import com.cst338.cst438_p1.database.dao.JokeDao
import com.cst338.cst438_p1.database.dao.UserDao
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class
AppDatabaseTest {
    private lateinit var db: AppDatabase
    private lateinit var userDao: UserDao
    private lateinit var jokeDao: JokeDao
    private lateinit var favoriteDao: FavoriteDao


    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()
        userDao = db.userDao()
        jokeDao = db.jokeDao()
        favoriteDao = db.favoriteDao()
    }

    @After
    fun closeDb() {
        db.close()
    }

    @Test
    fun getUserByIdTest() = runTest {
        val user = User(1, "username", "password")
        userDao.insert(user)
        assertEquals(user, userDao.getUserById(1))
    }

    @Test
    fun getUserByUsernameTest() = runTest {
        val user = User(1, "username", "password")
        userDao.insert(user)
        assertEquals(user, userDao.getUser("username"))
    }

    @Test
    fun getJokeByUserIdTest() = runTest {
        jokeDao.insert(
            Joke(
                "h39UfibMJBd",
                "Did you hear about the cheese who saved the world? It was Legend-dairy!"
            )
        )

        favoriteDao.insert(Favorite(1, "h39UfibMJBd"))

        assertEquals("h39UfibMJBd", jokeDao.getJokeByUserId(1).get(0).jokeId)

        jokeDao.insert(
            Joke(
                "uszdNZ8MRCd",
                "My new thesaurus is terrible. In fact, it's so bad, I'd say it's terrible."
            )
        )
        jokeDao.insert(
            Joke(
                "lbU01DljGtc",
                "I couldn't get a reservation at the library. They were completely booked."
            )
        )

        favoriteDao.insert(Favorite(1, "uszdNZ8MRCd"))
        favoriteDao.insert(Favorite(1, "lbU01DljGtc"))

        val jokes = jokeDao.getJokeByUserId(1)
        assertEquals(3, jokes.size)
    }


}