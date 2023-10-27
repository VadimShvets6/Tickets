package com.top1shvetsvadim1.jarvis.domain.repositories

import com.top1shvetsvadim1.jarvis.domain.models.HubMovieModel
import kotlinx.coroutines.flow.Flow

interface RepositoryTrending {

    suspend fun fetchTrendingMovies()

    suspend fun getTrendingMoviesPreview(): Flow<List<HubMovieModel>>
}