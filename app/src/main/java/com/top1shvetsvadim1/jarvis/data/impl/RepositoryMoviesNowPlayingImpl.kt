package com.top1shvetsvadim1.jarvis.data.impl

import com.top1shvetsvadim1.jarvis.data.locale_storage.genres.MovieGenreDao
import com.top1shvetsvadim1.jarvis.data.locale_storage.now_playing_storage.MovieNowPlayingDDModel
import com.top1shvetsvadim1.jarvis.data.locale_storage.now_playing_storage.MovieNowPlayingDao
import com.top1shvetsvadim1.jarvis.data.service.MoviesApiService
import com.top1shvetsvadim1.jarvis.domain.models.HubMovieModel
import com.top1shvetsvadim1.jarvis.domain.repositories.RepositoryMoviesNowPlaying
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

class RepositoryMoviesNowPlayingImpl @Inject constructor(
    private val remoteMovies: MoviesApiService,
    private val movieNowPlayingDao: MovieNowPlayingDao,
    private val genreDao: MovieGenreDao
) : RepositoryMoviesNowPlaying {

    override suspend fun fetchListMoviesNowPlaying() {
        val moviesNowPlayingList = remoteMovies.getMoviesNowPlaying(1).moviesNowPlaying
        movieNowPlayingDao.upsertMoviesNowPlayingList(
            moviesNowPlayingList.map { movie ->
                MovieNowPlayingDDModel(
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

    override suspend fun getListMoviesNowPlayingPreview(): Flow<List<HubMovieModel>> {
        return combine(
            movieNowPlayingDao.getMoviesNowPlayingPreview(),
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