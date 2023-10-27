package com.top1shvetsvadim1.jarvis.domain.models

data class HubMovieModel(
    val id: Int,
    val posterImage: String,
    val movieName: String,
    val genres: List<String>,
    val voteRated: Double,
    val releaseDate: String
)

data class HubGenresModel(
    val id: Int,
    val title: String
)