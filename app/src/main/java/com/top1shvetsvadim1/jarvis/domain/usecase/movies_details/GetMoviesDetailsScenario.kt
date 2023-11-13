package com.top1shvetsvadim1.jarvis.domain.usecase.movies_details

import com.top1shvetsvadim1.jarvis.domain.models.MoviesDetailsModel
import com.top1shvetsvadim1.jarvis.domain.repositories.RepositoryMoviesDetails
import com.top1shvetsvadim1.jarvis.presentation.base.SuspendUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class GetMoviesDetailsScenario @Inject constructor(
    private val repositoryMoviesDetails: RepositoryMoviesDetails
) : SuspendUseCase<GetMoviesDetailsScenario.Params, Flow<MoviesDetailsModel>> {

    data class Params(val id: Int)

    @OptIn(ExperimentalCoroutinesApi::class)
    override suspend fun invoke(params: Params): Flow<MoviesDetailsModel> {
        val movieDetails = repositoryMoviesDetails.getMoviesById(params.id)
        return movieDetails.filterNotNull().flatMapLatest {
            kotlinx.coroutines.flow.combine(
                flowOf(null),
                repositoryMoviesDetails.getMoviesActorsById(params.id)
            ) { moviesDetails, actors ->
                MoviesDetailsModel(
                    details = it,
                    reviews = listOf(),
                    actors = actors,
                    moviesTrailers = listOf(),
                    similarMovies = listOf()
                )
            }
        }
    }
}