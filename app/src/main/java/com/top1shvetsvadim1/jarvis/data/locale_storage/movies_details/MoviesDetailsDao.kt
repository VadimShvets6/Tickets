package com.top1shvetsvadim1.jarvis.data.locale_storage.movies_details

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface MoviesDetailsDao {

    @Upsert
    fun insertMoviesDetails(moviesDetailsDB: MoviesDetailsDB)

    @Query("Select * from movies_details where id=:id limit 1")
    fun getMovieDetailsById(id: Int) : Flow<MoviesDetailsDB?>
}