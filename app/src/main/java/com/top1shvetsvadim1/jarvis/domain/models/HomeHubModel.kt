package com.top1shvetsvadim1.jarvis.domain.models

data class HomeHubModel(
    val movies: HomeMoviesModel,
    val genres: HomeGenresModel,
    val people: HomePeopleModel
)

data class HomeMoviesModel(
    val listNowPlaying: List<HubMovieModel>,
    val listPopularMovies: List<HubMovieModel>,
    val listTopRated: List<HubMovieModel>,
    val listUpcoming: List<HubMovieModel>,
    val listTrendMovies: List<HubMovieModel>,
)

data class HomeGenresModel(
    val listGenres: List<HubGenresModel>
)

data class HomePeopleModel(
    val listTrendPeople: List<HubPeopleModel>
)