package com.top1shvetsvadim1.jarvis.data.locale_storage.trending

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("people_trends")
data class PeopleTrendDB(
    @PrimaryKey val id: Int,
    val adult: Boolean,
    val name: String,
    val originalName: String,
    val popularity: Double,
    val profilePatch: String?
)