package com.top1shvetsvadim1.jarvis.data.locale_storage.trending

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface PeopleTrendingDao {

    @Upsert
    fun upsertTrendingPeopleList(listPeople: List<PeopleTrendDB>)

    @Query("Select * from people_trends order by popularity desc limit 10")
    fun getPeopleTrendingListPreview(): Flow<List<PeopleTrendDB>>
}