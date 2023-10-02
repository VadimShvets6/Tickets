package com.top1shvetsvadim1.jarvis.data.module

import com.google.gson.annotations.SerializedName

data class MoviesNowPlayingAPI(
    @SerializedName("results")
    val moviesNowPlaying : List<MoviesAPI>
)