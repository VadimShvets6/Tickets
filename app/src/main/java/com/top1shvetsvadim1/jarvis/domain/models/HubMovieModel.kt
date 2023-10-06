package com.top1shvetsvadim1.jarvis.domain.models

data class HubMovieModel(
    val posterImage: String,
    val movieName: String,
    val genres: List<String>,
    val voteRated: Double,
    val releaseDate: String
)