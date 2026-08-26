package com.cst338.cst438_p1.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.cst338.cst438_p1.database.dao.FavoriteDao
import com.cst338.cst438_p1.database.dao.JokeDao
import com.cst338.cst438_p1.database.dao.UserDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [User::class, Joke::class, Favorite::class], version = 2)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun jokeDao(): JokeDao
    abstract fun favoriteDao(): FavoriteDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "database-name"
                ).addCallback(Callback(scope))
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private class Callback(
            private val scope: CoroutineScope
        ) : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    scope.launch(Dispatchers.IO) {
                        val userDao = database.userDao()
                        val jokeDao = database.jokeDao()
                        val favoriteDao = database.favoriteDao()

                        userDao.insert(User(1, "User", "password"))

                        jokeDao.insert(
                            Joke(
                                "h39UfibMJBd",
                                "Did you hear about the cheese who saved the world? It was Legend-dairy!"
                            )
                        )
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

                        favoriteDao.insert(Favorite(1, "h39UfibMJBd"))
                        favoriteDao.insert(Favorite(1, "uszdNZ8MRCd"))
                        favoriteDao.insert(Favorite(1, "lbU01DljGtc"))
                    }
                }
            }
            }
    }
}