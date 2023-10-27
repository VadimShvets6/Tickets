package com.top1shvetsvadim1.jarvis.domain.usecase.trending

import com.top1shvetsvadim1.jarvis.domain.repositories.RepositoryTrending
import com.top1shvetsvadim1.jarvis.presentation.base.SuspendUseCaseNoParams
import javax.inject.Inject

class FetchTrendMoviesUseCase @Inject constructor(
    private val repositoryTrending: RepositoryTrending
) : SuspendUseCaseNoParams<Unit> {
    override suspend fun invoke() {
        repositoryTrending.fetchTrendingMovies()
    }
}