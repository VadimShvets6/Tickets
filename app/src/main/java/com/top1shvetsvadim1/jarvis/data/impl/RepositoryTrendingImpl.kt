package com.top1shvetsvadim1.jarvis.data.impl

import com.top1shvetsvadim1.jarvis.data.locale_storage.genres.MovieGenreDao
import com.top1shvetsvadim1.jarvis.data.locale_storage.trending.MovieTrendDB
import com.top1shvetsvadim1.jarvis.data.locale_storage.trending.MovieTrendingDao
import com.top1shvetsvadim1.jarvis.data.service.MoviesApiService
import com.top1shvetsvadim1.jarvis.domain.models.HubMovieModel
import com.top1shvetsvadim1.jarvis.domain.repositories.RepositoryTrending
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

class RepositoryTrendingImpl @Inject constructor(
    private val remoteSource: MoviesApiService,
    private val trendingDao: MovieTrendingDao,
    private val genresDao: MovieGenreDao
) : RepositoryTrending {

    override suspend fun fetchTrendingMovies() {
        val moviesList = remoteSource.getTrendingMovies().moviesList
        trendingDao.upsertMoviesTrendList(
            moviesList.map { movie ->
                MovieTrendDB(
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

    override suspend fun getTrendingMoviesPreview(): Flow<List<HubMovieModel>> {
        return combine(
            trendingDao.getTrendMoviesListPreview(),
            genresDao.getListGenres()
        ) { movies, genres ->
            movies.map { movie ->
                HubMovieModel(
                    id = movie.id,
                    posterImage = movie.posterPath,
                    movieName = movie.title,
                    genres = genres.filter { genre -> genre.id in movie.genreId }.map { genre -> genre.title },
                    voteRated = movie.voteAverage,
                    releaseDate = movie.releaseDate
                )
            }
        }
    }
}