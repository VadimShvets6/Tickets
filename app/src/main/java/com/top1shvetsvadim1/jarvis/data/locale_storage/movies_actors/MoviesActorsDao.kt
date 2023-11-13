package com.top1shvetsvadim1.jarvis.data.locale_storage.movies_actors

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface MoviesActorsDao {

    @Upsert
    fun insertMovieActorsList(movieActorsList: List<MovieActorsDB>)

    @Upsert
    fun insertMovieActors(movieActors: MovieActorsDB)

    @Query("Select * from movies_actors where movieId=:id")
    fun getMovieActorsById(id: Int): Flow<List<MovieActorsDB>>
}