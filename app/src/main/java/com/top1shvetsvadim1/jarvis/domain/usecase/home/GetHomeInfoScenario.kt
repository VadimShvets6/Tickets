package com.top1shvetsvadim1.jarvis.domain.usecase.home

import com.top1shvetsvadim1.jarvis.domain.models.HomeHubModel
import com.top1shvetsvadim1.jarvis.domain.repositories.RepositoryMoviesNowPlaying
import com.top1shvetsvadim1.jarvis.presentation.base.SuspendUseCaseNoParams
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class GetHomeInfoScenario @Inject constructor(
    private val repositoryMoviesNowPlaying: RepositoryMoviesNowPlaying
) : SuspendUseCaseNoParams<Flow<HomeHubModel>> {
    override suspend fun invoke(): Flow<HomeHubModel> {
        return combine(
            repositoryMoviesNowPlaying.getListMoviesNowPlaying(),
            flowOf(null)
        ) { nowPlayingList, stub ->
            HomeHubModel(
                listNowPlaying = nowPlayingList
            )
        }
    }
}