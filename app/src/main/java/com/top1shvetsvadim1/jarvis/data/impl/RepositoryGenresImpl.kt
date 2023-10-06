package com.top1shvetsvadim1.jarvis.data.impl

import com.top1shvetsvadim1.jarvis.data.locale_storage.genres.MovieGenreDB
import com.top1shvetsvadim1.jarvis.data.locale_storage.genres.MovieGenreDao
import com.top1shvetsvadim1.jarvis.data.service.MoviesApiService
import com.top1shvetsvadim1.jarvis.domain.repositories.RepositoryGenres
import javax.inject.Inject

class RepositoryGenresImpl @Inject constructor(
    private val remoteService: MoviesApiService,
    private val genreDao: MovieGenreDao
) : RepositoryGenres {

    override suspend fun fetchGenres() {
        val genres = remoteService.getGenreList().genres.map {
            MovieGenreDB(id = it.id, title = it.title)
        }
        genreDao.insertGenres(genres)
    }
}