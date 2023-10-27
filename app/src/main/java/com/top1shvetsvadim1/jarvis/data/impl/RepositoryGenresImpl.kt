package com.top1shvetsvadim1.jarvis.data.impl

import com.top1shvetsvadim1.jarvis.data.locale_storage.genres.MovieGenreDB
import com.top1shvetsvadim1.jarvis.data.locale_storage.genres.MovieGenreDao
import com.top1shvetsvadim1.jarvis.data.service.MoviesApiService
import com.top1shvetsvadim1.jarvis.domain.models.HubGenresModel
import com.top1shvetsvadim1.jarvis.domain.repositories.RepositoryGenres
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
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

    override suspend fun getAllListGenres(): Flow<List<HubGenresModel>> {
        return genreDao.getListGenres().map { genres ->
            genres.map {
                HubGenresModel(id = it.id, title = it.title)
            }
        }
    }
}