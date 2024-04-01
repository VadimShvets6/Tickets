package com.top1shvetsvadim1.jarvis.domain.models

data class MoviesDetailsModel(
    val details: MovieDetails,
    val reviews: List<MovieReviewsModel>,
    val actors: List<ActorsModel>,
    val moviesTrailers: List<MoviesTrailersModel>,
    val similarMovies: List<HubMovieModel>,
    val external : MoviesExternalModel
)

data class MovieDetails(
    val adult: Boolean,
    val backdropPath: String?,
    val budged: Long,
    val genres: List<String>,
    val id: Int,
    val overview: String,
    val popularity: Double,
    val posterPath: String?,
    val productionCompanies: List<ProductCompaniesModel>,
    val releaseDate: String,
    val revenue: Long,
    val runtime: Int,
    val status: String,
    val title: String,
    val voteAverage: Double,
    val voteCount: Int,
    val countryOfOrigin: String?,
    val originalTitle: String,
    val originalLanguage: String
)

data class ProductCompaniesModel(
    val id: Int,
    val logoPath: String?,
    val companiesName: String,
)

data class MovieReviewsModel(
    val author: String,
    val authorDetails: AuthorDetailsModel,
    val content: String,
    val createdAt: String
)

data class AuthorDetailsModel(
    val name: String?,
    val username: String,
    val avatarPath: String,
    val rating: Int?
)

data class ActorsModel(
    val adult: Boolean,
    val id: Int,
    val name: String,
    val profilePath: String?,
    val castId: Int,
    val characterName: String
)

data class MoviesTrailersModel(
    val name: String,
    val keyVideo: String,
    val size: Int
)

data class MoviesExternalModel(
    val id: Int,
    val imdbId: String,
    val wikidataId: String,
    val facebookId: String,
    val instagramId: String,
    val twitterId: String,
)
