package com.top1shvetsvadim1.jarvis.domain.repositories

import com.top1shvetsvadim1.jarvis.domain.models.HubMovieModel
import kotlinx.coroutines.flow.Flow

interface RepositoryMoviesTopRated {
    suspend fun fetchMovieTopRated()

    suspend fun getListMovieTopRatedPreview(): Flow<List<HubMovieModel>>
}