package com.top1shvetsvadim1.jarvis.domain.repositories

import com.top1shvetsvadim1.jarvis.domain.models.HubGenresModel
import kotlinx.coroutines.flow.Flow

interface RepositoryGenres {
    suspend fun fetchGenres()

    suspend fun getAllListGenres() : Flow<List<HubGenresModel>>
}