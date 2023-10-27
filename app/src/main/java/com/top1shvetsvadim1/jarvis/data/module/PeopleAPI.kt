package com.top1shvetsvadim1.jarvis.data.module

import com.google.gson.annotations.SerializedName

data class PeopleAPI(
    @SerializedName("adult")
    val adult: Boolean,
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("original_name")
    val originalName: String,
    @SerializedName("popularity")
    val popularity: Double,
    @SerializedName("profile_path")
    val profilePath: String
)

data class PeopleResponse(
    @SerializedName("results")
    val listPeople: List<PeopleAPI>
)