package com.cst338.cst438_p1.database

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(tableName = "Favorite", primaryKeys = ["uid", "joke_id"])
data class Favorite (
    @ColumnInfo val uid: Int,
    @ColumnInfo(name = "joke_id") val jokeId: String
)