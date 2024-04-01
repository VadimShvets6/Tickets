package com.top1shvetsvadim1.jarvis.data.impl

import com.top1shvetsvadim1.jarvis.data.locale_storage.genres.MovieGenreDao
import com.top1shvetsvadim1.jarvis.data.locale_storage.upcoming.MovieUpcomingDB
import com.top1shvetsvadim1.jarvis.data.locale_storage.upcoming.MoviesUpcomingDao
import com.top1shvetsvadim1.jarvis.data.service.MoviesApiService
import com.top1shvetsvadim1.jarvis.domain.models.HubMovieModel
import com.top1shvetsvadim1.jarvis.domain.repositories.RepositoryMoviesUpcoming
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

class RepositoryMoviesUpcomingImpl @Inject constructor(
    private val remoteSource: MoviesApiService,
    private val upcomingDao: MoviesUpcomingDao,
    private val genreDao: MovieGenreDao
) : RepositoryMoviesUpcoming {

    override suspend fun fetchMoviesUpcoming() {
        val movieList = remoteSource.getMoviesUpcoming(1).moviesList.map { movie ->
            MovieUpcomingDB(
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
        upcomingDao.upsertMoviesUpcoming(movieList)
    }

    override suspend fun getMoviesUpcomingListPreview(): Flow<List<HubMovieModel>> {
        return combine(
            upcomingDao.getMoviesUpcomingListPreview(),
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