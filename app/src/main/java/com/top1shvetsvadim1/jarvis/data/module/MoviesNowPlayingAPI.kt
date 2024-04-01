package com.top1shvetsvadim1.jarvis.data.module

import com.google.gson.annotations.SerializedName

data class MoviesResponseAPI(
    @SerializedName("results")
    val moviesList : List<MoviesAPI>
)

data class MoviesGenreAPI(
    @SerializedName("genres")
    val genres: GenreAPI
)