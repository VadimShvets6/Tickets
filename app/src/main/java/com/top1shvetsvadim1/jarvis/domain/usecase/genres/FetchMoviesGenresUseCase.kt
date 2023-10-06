package com.top1shvetsvadim1.jarvis.domain.usecase.genres

import com.top1shvetsvadim1.jarvis.domain.repositories.RepositoryGenres
import com.top1shvetsvadim1.jarvis.presentation.base.SuspendUseCaseNoParams
import javax.inject.Inject

class FetchMoviesGenresUseCase @Inject constructor(
    private val repositoryGenres: RepositoryGenres
) : SuspendUseCaseNoParams<Unit> {
    override suspend fun invoke() {
        repositoryGenres.fetchGenres()
    }
}