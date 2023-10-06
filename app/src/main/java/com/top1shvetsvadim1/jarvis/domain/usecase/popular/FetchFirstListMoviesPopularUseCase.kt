package com.top1shvetsvadim1.jarvis.domain.usecase.popular

import com.top1shvetsvadim1.jarvis.domain.repositories.RepositoryMoviesPopular
import com.top1shvetsvadim1.jarvis.presentation.base.SuspendUseCaseNoParams
import javax.inject.Inject

class FetchFirstListMoviesPopularUseCase @Inject constructor(
    private val repositoryMoviesPopular: RepositoryMoviesPopular
) : SuspendUseCaseNoParams<Unit> {

    override suspend fun invoke() {
        repositoryMoviesPopular.fetchMoviePopular()
    }
}