package com.top1shvetsvadim1.jarvis.data.module

import com.google.gson.annotations.SerializedName

data class MoviesCastResponse(
    @SerializedName("cast")
    val moviesCasts: List<ActorsAPI>
)

data class ActorsAPI(
    @SerializedName("adult")
    val adult: Boolean,
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("profile_path")
    val profilePath: String?,
    @SerializedName("cast_id")
    val castId: Int,
    @SerializedName("character")
    val characterName: String
)