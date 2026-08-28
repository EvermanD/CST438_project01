package com.cst338.cst438_p1.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.cst338.cst438_p1.database.Favorite

@Dao
interface FavoriteDao {
    @Query("SELECT * FROM Favorite")
    fun getAllFavorites(): List<Favorite>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(favorite: Favorite): Long

    @Query("DELETE FROM Favorite " +
            "WHERE uid = :uid AND joke_id = :jokeId")
    suspend fun deleteFavorite(uid: Int, jokeId: String)
}