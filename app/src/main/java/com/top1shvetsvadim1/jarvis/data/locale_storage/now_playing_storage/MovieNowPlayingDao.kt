package com.top1shvetsvadim1.jarvis.data.locale_storage.now_playing_storage

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieNowPlayingDao {

    @Upsert
    fun upsertMoviesNowPlayingList(nowPlayingList: List<MovieNowPlayingDDModel>)

    @Query("Select * from movies_now_playing order by popularity desc limit 10")
    fun getMoviesNowPlayingPreview(): Flow<List<MovieNowPlayingDDModel>>
}