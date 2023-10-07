package com.top1shvetsvadim1.jarvis.domain.usecase.now_playing

import com.top1shvetsvadim1.jarvis.domain.models.HubMovieModel
import com.top1shvetsvadim1.jarvis.domain.repositories.RepositoryMoviesNowPlaying
import com.top1shvetsvadim1.jarvis.presentation.base.SuspendUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetMoviesNowPlayingUseCase @Inject constructor(
    private val repositoryMoviesNowPlaying: RepositoryMoviesNowPlaying
) : SuspendUseCase<Unit, Flow<List<HubMovieModel>>> {
    override suspend fun invoke(params: Unit): Flow<List<HubMovieModel>> {
        return repositoryMoviesNowPlaying.getListMoviesNowPlayingPreview()
    }
}