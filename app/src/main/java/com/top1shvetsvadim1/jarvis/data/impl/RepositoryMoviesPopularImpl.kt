package com.top1shvetsvadim1.jarvis.data.impl

import android.util.Log
import com.top1shvetsvadim1.jarvis.data.locale_storage.genres.MovieGenreDao
import com.top1shvetsvadim1.jarvis.data.locale_storage.popular.MoviePopularDB
import com.top1shvetsvadim1.jarvis.data.locale_storage.popular.MoviePopularDao
import com.top1shvetsvadim1.jarvis.data.service.MoviesApiService
import com.top1shvetsvadim1.jarvis.domain.models.HubMovieModel
import com.top1shvetsvadim1.jarvis.domain.repositories.RepositoryMoviesPopular
import com.top1shvetsvadim1.jarvis.presentation.utils.CurrentUser
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

class RepositoryMoviesPopularImpl @Inject constructor(
    private val remoteMovies: MoviesApiService,
    private val popularDao: MoviePopularDao,
    private val genreDao: MovieGenreDao
) : RepositoryMoviesPopular {

    override suspend fun fetchMoviePopular() {
        val moviesList = remoteMovies.getMoviesPopular(1).moviesList
        popularDao.upsertListMoviePopular(
            moviesList.map { movie ->
                MoviePopularDB(
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

    override suspend fun getListMoviePopularPreview(): Flow<List<HubMovieModel>> {
        return combine(
            popularDao.getMoviePopularPreview(),
            genreDao.getListGenres()
        ) { movies, genres ->
            movies.map {
                HubMovieModel(
                    id = it.id,
                    posterImage = it.posterPath,
                    movieName = it.title,
                    genres = genres.filter { genres -> genres.id in it.genreId }.map { genre -> genre.title },
                    voteRated = it.voteAverage,
                    releaseDate = it.releaseDate
                )
            }
        }
    }
}