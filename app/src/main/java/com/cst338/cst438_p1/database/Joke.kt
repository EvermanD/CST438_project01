package com.cst338.cst438_p1.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Joke")
data class Joke (
    @PrimaryKey
    @ColumnInfo(name = "joke_id") val jokeId: String,
    @ColumnInfo(name = "joke") val joke: String
)