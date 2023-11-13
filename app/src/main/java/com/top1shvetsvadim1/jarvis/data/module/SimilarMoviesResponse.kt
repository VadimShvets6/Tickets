package com.top1shvetsvadim1.jarvis.data.module

import com.google.gson.annotations.SerializedName

data class SimilarMoviesResponse(
    @SerializedName("results")
    val similarMovies: List<MoviesAPI>
)
