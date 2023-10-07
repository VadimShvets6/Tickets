package com.top1shvetsvadim1.jarvis.domain.usecase.home

import com.top1shvetsvadim1.jarvis.domain.models.HomeHubModel
import com.top1shvetsvadim1.jarvis.domain.repositories.RepositoryMoviesNowPlaying
import com.top1shvetsvadim1.jarvis.domain.repositories.RepositoryMoviesPopular
import com.top1shvetsvadim1.jarvis.presentation.base.SuspendUseCaseNoParams
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

class GetHomeInfoScenario @Inject constructor(
    private val repositoryMoviesNowPlaying: RepositoryMoviesNowPlaying,
    private val repositoryMoviesPopular: RepositoryMoviesPopular
) : SuspendUseCaseNoParams<Flow<HomeHubModel>> {
    override suspend fun invoke(): Flow<HomeHubModel> {
        return combine(
            repositoryMoviesNowPlaying.getListMoviesNowPlayingPreview(),
            repositoryMoviesPopular.getListMoviePopularPreview()
        ) { nowPlayingList, popularList ->
            HomeHubModel(
                listNowPlaying = nowPlayingList,
                listPopularMovies = popularList
            )
        }
    }
}