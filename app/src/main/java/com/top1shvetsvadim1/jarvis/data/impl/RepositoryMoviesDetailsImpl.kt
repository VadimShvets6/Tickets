package com.top1shvetsvadim1.jarvis.data.impl

import com.top1shvetsvadim1.jarvis.data.locale_storage.genres.MovieGenreDao
import com.top1shvetsvadim1.jarvis.data.locale_storage.movies_actors.MovieActorsDB
import com.top1shvetsvadim1.jarvis.data.locale_storage.movies_actors.MoviesActorsDao
import com.top1shvetsvadim1.jarvis.data.locale_storage.movies_details.MoviesDetailsDB
import com.top1shvetsvadim1.jarvis.data.locale_storage.movies_details.MoviesDetailsDao
import com.top1shvetsvadim1.jarvis.data.locale_storage.movies_details.ProductCompaniesDB
import com.top1shvetsvadim1.jarvis.data.module.ActorsAPI
import com.top1shvetsvadim1.jarvis.data.module.MovieDetailsAPI
import com.top1shvetsvadim1.jarvis.data.service.MoviesApiService
import com.top1shvetsvadim1.jarvis.domain.models.ActorsModel
import com.top1shvetsvadim1.jarvis.domain.models.MovieDetails
import com.top1shvetsvadim1.jarvis.domain.models.ProductCompaniesModel
import com.top1shvetsvadim1.jarvis.domain.repositories.RepositoryMoviesDetails
import com.top1shvetsvadim1.jarvis.presentation.utils.extentions.toImageUrl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RepositoryMoviesDetailsImpl @Inject constructor(
    private val remoteSource: MoviesApiService,
    private val moviesDetailsDao: MoviesDetailsDao,
    private val moviesActorsDao: MoviesActorsDao,
    private val genresDao: MovieGenreDao
) : RepositoryMoviesDetails {

    override suspend fun fetchMoviesById(id: Int) {
        withContext(Dispatchers.IO) {
            val moviesDetails = async {
                remoteSource.getMoviesDetailsById(id)
            }
            val moviesActors = async {
                remoteSource.getMovieActorsById(id).moviesCasts
            }
            insertMoviesDetailsInDB(moviesDetails.await())
            insertMovieActorsDB(id, moviesActors.await())
        }
    }

    private fun insertMoviesDetailsInDB(moviesDetails: MovieDetailsAPI) {
        moviesDetailsDao.insertMoviesDetails(
            MoviesDetailsDB(
                adult = moviesDetails.adult,
                budged = moviesDetails.budged,
                genreId = moviesDetails.genres.map { it.id },
                id = moviesDetails.id,
                overview = moviesDetails.overview,
                popularity = moviesDetails.popularity,
                posterPath = moviesDetails.posterPath,
                productionCompanies = moviesDetails.productionCompanies.map {
                    ProductCompaniesDB(
                        id = it.id,
                        logoPath = it.logoPath,
                        companiesName = it.companiesName
                    )
                },
                productionCountries = moviesDetails.productionCountries.firstOrNull()?.nameCountry,
                releaseDate = moviesDetails.releaseDate,
                revenue = moviesDetails.revenue,
                runtime = moviesDetails.runtime,
                status = moviesDetails.status,
                title = moviesDetails.title,
                voteAverage = moviesDetails.voteAverage,
                voteCount = moviesDetails.voteCount,
                backdropPath = moviesDetails.backdropPath,
                originalTitle = moviesDetails.originalTitle,
                originalLanguage = moviesDetails.originalLanguage
            )
        )
    }

    private fun insertMovieActorsDB(movieId: Int, movieActors: List<ActorsAPI>) {
        moviesActorsDao.insertMovieActorsList(
            movieActors.map {
                MovieActorsDB(
                    movieId = movieId,
                    adult = it.adult,
                    actorId = it.id,
                    name = it.name,
                    profilePath = it.profilePath,
                    castId = it.castId,
                    characterName = it.characterName
                )
            }
        )
    }

    override suspend fun getMoviesById(id: Int): Flow<MovieDetails> {
        return combine(
            moviesDetailsDao.getMovieDetailsById(id).filterNotNull(),
            genresDao.getListGenres()
        ) { movieDetails, genresId ->
            MovieDetails(
                budged = movieDetails.budged,
                adult = movieDetails.adult,
                genres = genresId.filter { genres -> genres.id in movieDetails.genreId }.map { genre -> genre.title },
                id = movieDetails.id,
                overview = movieDetails.overview,
                popularity = movieDetails.popularity,
                posterPath = movieDetails.posterPath?.toImageUrl(),
                productionCompanies = movieDetails.productionCompanies.map {
                    ProductCompaniesModel(
                        id = it.id,
                        logoPath = it.logoPath,
                        companiesName = it.companiesName
                    )
                },
                releaseDate = movieDetails.releaseDate,
                revenue = movieDetails.revenue,
                runtime = movieDetails.runtime,
                status = movieDetails.status,
                title = movieDetails.title,
                voteAverage = movieDetails.voteAverage,
                voteCount = movieDetails.voteCount,
                backdropPath = movieDetails.backdropPath?.toImageUrl(),
                countryOfOrigin = movieDetails.productionCountries,
                originalTitle = movieDetails.originalTitle,
                originalLanguage = movieDetails.originalLanguage
            )
        }
    }

    override suspend fun getMoviesActorsById(id: Int): Flow<List<ActorsModel>> {
        return moviesActorsDao.getMovieActorsById(id).map {
            it.map { actor ->
                ActorsModel(
                    adult = actor.adult,
                    id = actor.actorId,
                    name = actor.name,
                    profilePath = actor.profilePath,
                    castId = actor.castId,
                    characterName = actor.characterName
                )
            }
        }
    }
}