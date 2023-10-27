package com.top1shvetsvadim1.jarvis.domain.repositories

import com.top1shvetsvadim1.jarvis.domain.models.HubPeopleModel
import kotlinx.coroutines.flow.Flow

interface RepositoryPeople {

    suspend fun fetchTrendingPeople()

    suspend fun getTrendingPeopleListPreview(): Flow<List<HubPeopleModel>>
}