package com.top1shvetsvadim1.jarvis.data.locale_storage.popular

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface MoviePopularDao {

    @Upsert
    fun upsertListMoviePopular(movieList: List<MoviePopularDB>)

    @Upsert
    fun upsertMoviePopular(moviePopular: MoviePopularDB)

    @Query("Select * from movie_popular order by voteCount desc limit 10")
    fun getMoviePopularPreview(): Flow<List<MoviePopularDB>>
}