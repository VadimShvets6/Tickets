package com.top1shvetsvadim1.jarvis.domain.usecase.upcoming

import com.top1shvetsvadim1.jarvis.domain.repositories.RepositoryMoviesUpcoming
import com.top1shvetsvadim1.jarvis.presentation.base.SuspendUseCaseNoParams
import javax.inject.Inject

class FetchFirstListMoviesUpcomingUseCase @Inject constructor(
    private val repositoryMoviesUpcoming: RepositoryMoviesUpcoming
) : SuspendUseCaseNoParams<Unit> {

    override suspend fun invoke() {
        repositoryMoviesUpcoming.fetchMoviesUpcoming()
    }
}