package com.top1shvetsvadim1.jarvis.data.locale_storage.trending

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieTrendingDao {

    @Upsert
    fun upsertMoviesTrendList(moviesList: List<MovieTrendDB>)

    @Query("Select * from movies_trending order by popularity desc limit 10")
    fun getTrendMoviesListPreview(): Flow<List<MovieTrendDB>>
}