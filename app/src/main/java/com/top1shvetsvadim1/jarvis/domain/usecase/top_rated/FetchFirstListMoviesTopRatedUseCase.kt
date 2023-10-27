package com.top1shvetsvadim1.jarvis.domain.usecase.top_rated

import com.top1shvetsvadim1.jarvis.domain.repositories.RepositoryMoviesTopRated
import com.top1shvetsvadim1.jarvis.presentation.base.SuspendUseCaseNoParams
import javax.inject.Inject

class FetchFirstListMoviesTopRatedUseCase @Inject constructor(
    private val repositoryMoviesTopRated: RepositoryMoviesTopRated
) : SuspendUseCaseNoParams<Unit> {
    override suspend fun invoke() {
        repositoryMoviesTopRated.fetchMovieTopRated()
    }
}