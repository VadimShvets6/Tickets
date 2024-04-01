package com.top1shvetsvadim1.jarvis.domain.usecase.movies_details

import com.top1shvetsvadim1.jarvis.domain.repositories.RepositoryMoviesDetails
import com.top1shvetsvadim1.jarvis.presentation.base.SuspendUseCase
import javax.inject.Inject

class FetchMovieDetailsScenario @Inject constructor(
    private val repositoryMoviesDetails: RepositoryMoviesDetails
) : SuspendUseCase<FetchMovieDetailsScenario.Params, Unit> {

    data class Params(val id: Int)

    override suspend fun invoke(params: Params) {
        repositoryMoviesDetails.fetchMoviesById(params.id)
    }
}