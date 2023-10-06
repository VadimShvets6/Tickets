package com.top1shvetsvadim1.jarvis.data.module

import com.google.gson.annotations.SerializedName

data class GenreAPI(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val title: String
)