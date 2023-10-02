package com.top1shvetsvadim1.jarvis.domain.usecase.now_playing

import com.top1shvetsvadim1.jarvis.data.module.MoviesAPI
import com.top1shvetsvadim1.jarvis.domain.repositories.RepositoryMoviesNowPlaying
import com.top1shvetsvadim1.jarvis.presentation.base.SuspendUseCase
import com.top1shvetsvadim1.jarvis.presentation.base.SuspendUseCaseNoParams
import javax.inject.Inject

class GetMoviesNowPlayingUseCase @Inject constructor(
    private val repositoryMoviesNowPlaying: RepositoryMoviesNowPlaying
): SuspendUseCaseNoParams<Unit> {
    override suspend fun invoke() {
        repositoryMoviesNowPlaying.getListMoviesNowPlaying()
    }
}