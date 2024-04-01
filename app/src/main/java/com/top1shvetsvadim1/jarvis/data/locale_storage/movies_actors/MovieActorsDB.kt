package com.top1shvetsvadim1.jarvis.data.locale_storage.movies_actors

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("movies_actors")
data class MovieActorsDB(
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    val movieId: Int,
    val adult: Boolean,
    val actorId: Int,
    val name: String,
    val profilePath: String?,
    val castId: Int,
    val characterName: String
)
