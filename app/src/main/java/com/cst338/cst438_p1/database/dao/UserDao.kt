package com.cst338.cst438_p1.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Insert
import androidx.room.Delete
import com.cst338.cst438_p1.database.User

@Dao
interface UserDao {
    @Query("SELECT * FROM users WHERE username = :username")
    suspend fun getUser(username: String): User?

    @Query("SELECT * FROM users WHERE uid = :id")
    suspend fun getUserById(id: Int): User?

    @Query("SELECT * FROM USERS")
    fun getAll(): List<User>

    @Query("SELECT * FROM users WHERE uid IN (:userIds)")
    fun loadAllByIds(userIds: IntArray): List<User>

    @Query("SELECT * FROM users WHERE username LIKE :first AND " +
            "password LIKE :last LIMIT 1")
    fun findByUserName(first: String, last: String): User
    @Insert
    fun insertAll(vararg users: User)

    @Delete
    fun delete(user: User)

//    @Delete
//    fun deleteJoke(joke: Joke)

    @Insert
    suspend fun insert(user: User)

}