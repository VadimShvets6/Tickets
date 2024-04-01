package com.top1shvetsvadim1.jarvis.data.locale_storage.upcoming

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface MoviesUpcomingDao {

    @Upsert
    fun upsertMoviesUpcoming(listMovies: List<MovieUpcomingDB>)

    @Query("Select * from movies_upcoming order by releaseDate asc limit 10")
    fun getMoviesUpcomingListPreview(): Flow<List<MovieUpcomingDB>>
}