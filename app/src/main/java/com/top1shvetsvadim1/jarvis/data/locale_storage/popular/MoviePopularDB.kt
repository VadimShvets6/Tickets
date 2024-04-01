package com.top1shvetsvadim1.jarvis.data.locale_storage.popular

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.top1shvetsvadim1.jarvis.data.locale_storage.converts.IntTypeConverts

@Entity("movie_popular")
data class MoviePopularDB(
    @PrimaryKey val id: Int,
    val adult: Boolean,
    val backdropPath: String?,
    @TypeConverters(IntTypeConverts::class) val genreId: List<Int>,
    val originalLanguage: String,
    val originalTitle: String,
    val description: String,
    val popularity: Double,
    val posterPath: String?,
    val releaseDate: String,
    val title: String,
    val video: Boolean,
    val voteAverage: Double,
    val voteCount: Int,
    val isSave: Boolean,
)