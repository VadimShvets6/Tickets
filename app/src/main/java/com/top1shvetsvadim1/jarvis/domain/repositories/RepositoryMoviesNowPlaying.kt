package com.top1shvetsvadim1.jarvis.domain.repositories

import com.top1shvetsvadim1.jarvis.domain.models.HubMovieModel
import kotlinx.coroutines.flow.Flow

interface RepositoryMoviesNowPlaying {

    suspend fun fetchListMoviesNowPlaying()

    suspend fun getListMoviesNowPlaying() : Flow<List<HubMovieModel>>

}