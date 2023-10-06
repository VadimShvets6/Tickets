package com.top1shvetsvadim1.jarvis.data.locale_storage.genres

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieGenreDao {

    @Upsert
    fun insertGenres(genres: List<MovieGenreDB>)

    @Query("Select * from genres")
    fun getListGenres(): Flow<List<MovieGenreDB>>
}