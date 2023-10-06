package com.top1shvetsvadim1.jarvis.data.module

import com.google.gson.annotations.SerializedName


data class GenreResponse(
    @SerializedName("genres")
    val genres: List<GenreAPI>
)