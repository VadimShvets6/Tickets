package com.top1shvetsvadim1.jarvis.data.module

import com.google.gson.annotations.SerializedName


data class MoviesTrailersResponse(
    @SerializedName("results")
    val moviesTrailers: List<MoviesTrailersAPI>
)
data class MoviesTrailersAPI(
    @SerializedName("name")
    val name: String,
    @SerializedName("key")
    val keyVideo: String,
    @SerializedName("size")
    val size: Int
)