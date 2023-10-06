package com.top1shvetsvadim1.jarvis.domain.repositories

import com.top1shvetsvadim1.jarvis.domain.models.HubMovieModel
import kotlinx.coroutines.flow.Flow

interface RepositoryMoviesPopular {

    suspend fun fetchMoviePopular()

    suspend fun getListMoviePopularPreview(): Flow<List<HubMovieModel>>
}