package com.top1shvetsvadim1.jarvis.data.locale_storage.top_rated

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface MoviesTopRatedDao {

    @Upsert
    fun upsertListMoviesTopRated(listTopRated: List<MoviesTopRatedDB>)

    @Upsert
    fun upsertMoviesTopRated(moviesTopRated: MoviesTopRatedDB)

    @Query("Select * from movies_top_rated order by voteAverage desc limit 10")
    fun getMoviesTopRatedPreview(): Flow<List<MoviesTopRatedDB>>
}