package com.top1shvetsvadim1.jarvis.data.impl

import com.top1shvetsvadim1.jarvis.data.locale_storage.trending.PeopleTrendDB
import com.top1shvetsvadim1.jarvis.data.locale_storage.trending.PeopleTrendingDao
import com.top1shvetsvadim1.jarvis.data.service.MoviesApiService
import com.top1shvetsvadim1.jarvis.domain.models.HubPeopleModel
import com.top1shvetsvadim1.jarvis.domain.repositories.RepositoryPeople
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class RepositoryPeopleImpl @Inject constructor(
    private val remoteSource: MoviesApiService,
    private val peopleDao: PeopleTrendingDao
) : RepositoryPeople {

    override suspend fun fetchTrendingPeople() {
        val listPeople = remoteSource.getTrendingPeople().listPeople.map {
            PeopleTrendDB(
                id = it.id,
                adult = it.adult,
                name = it.name,
                originalName = it.originalName,
                popularity = it.popularity,
                profilePatch = it.profilePath
            )
        }
        peopleDao.upsertTrendingPeopleList(listPeople)
    }

    override suspend fun getTrendingPeopleListPreview(): Flow<List<HubPeopleModel>> {
        return peopleDao.getPeopleTrendingListPreview().map {
            it.map { people ->
                HubPeopleModel(
                    id = people.id,
                    profilePath = people.profilePatch ?: "",
                    popularity = people.popularity,
                    name = people.name
                )
            }
        }
    }
}