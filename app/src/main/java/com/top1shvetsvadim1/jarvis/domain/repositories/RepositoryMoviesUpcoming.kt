package com.top1shvetsvadim1.jarvis.domain.repositories

import com.top1shvetsvadim1.jarvis.domain.models.HubMovieModel
import kotlinx.coroutines.flow.Flow

interface RepositoryMoviesUpcoming {

    suspend fun fetchMoviesUpcoming()

    suspend fun getMoviesUpcomingListPreview(): Flow<List<HubMovieModel>>
}