package com.top1shvetsvadim1.jarvis.data.impl

import com.top1shvetsvadim1.jarvis.data.locale_storage.genres.MovieGenreDao
import com.top1shvetsvadim1.jarvis.data.locale_storage.top_rated.MoviesTopRatedDB
import com.top1shvetsvadim1.jarvis.data.locale_storage.top_rated.MoviesTopRatedDao
import com.top1shvetsvadim1.jarvis.data.service.MoviesApiService
import com.top1shvetsvadim1.jarvis.domain.models.HubMovieModel
import com.top1shvetsvadim1.jarvis.domain.repositories.RepositoryMoviesTopRated
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

class RepositoryMoviesTopRatedImpl @Inject constructor(
    private val remoteSource: MoviesApiService,
    private val topRatedDao: MoviesTopRatedDao,
    private val genreDao: MovieGenreDao
) : RepositoryMoviesTopRated {

    override suspend fun fetchMovieTopRated() {
        val moviesList = remoteSource.getMoviesTopRated(1).moviesList
        topRatedDao.upsertListMoviesTopRated(
            moviesList.map { movie ->
                MoviesTopRatedDB(
                    id = movie.id,
                    adult = movie.adult,
                    backdropPath = movie.backdropPath,
                    genreId = movie.genreId,
                    originalLanguage = movie.originalLanguage,
                    originalTitle = movie.originalTitle,
                    description = movie.description,
                    popularity = movie.popularity,
                    posterPath = movie.posterPath,
                    releaseDate = movie.releaseDate,
                    title = movie.title,
                    video = movie.video,
                    voteAverage = movie.voteAverage,
                    voteCount = movie.voteCount,
                    isSave = false
                )
            }
        )
    }

    override suspend fun getListMovieTopRatedPreview(): Flow<List<HubMovieModel>> {
        return combine(
            topRatedDao.getMoviesTopRatedPreview(),
            genreDao.getListGenres()
        ) { movies, genres ->
            movies.map { movie ->
                HubMovieModel(
                    id = movie.id,
                    posterImage = movie.posterPath,
                    movieName = movie.title,
                    genres = genres.filter { genres -> genres.id in movie.genreId }.map { genre -> genre.title },
                    voteRated = movie.voteAverage,
                    releaseDate = movie.releaseDate
                )
            }
        }
    }
}