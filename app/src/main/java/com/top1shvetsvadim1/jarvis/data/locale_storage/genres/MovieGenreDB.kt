package com.top1shvetsvadim1.jarvis.data.locale_storage.genres

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("genres")
data class MovieGenreDB(
    @PrimaryKey val id: Int,
    val title: String
)