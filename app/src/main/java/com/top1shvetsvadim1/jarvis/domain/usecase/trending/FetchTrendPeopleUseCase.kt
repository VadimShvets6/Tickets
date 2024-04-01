package com.top1shvetsvadim1.jarvis.domain.usecase.trending

import com.top1shvetsvadim1.jarvis.domain.repositories.RepositoryPeople
import com.top1shvetsvadim1.jarvis.presentation.base.SuspendUseCaseNoParams
import javax.inject.Inject

class FetchTrendPeopleUseCase @Inject constructor(
    private val repositoryPeople: RepositoryPeople
) : SuspendUseCaseNoParams<Unit> {
    override suspend fun invoke() {
        repositoryPeople.fetchTrendingPeople()
    }
}