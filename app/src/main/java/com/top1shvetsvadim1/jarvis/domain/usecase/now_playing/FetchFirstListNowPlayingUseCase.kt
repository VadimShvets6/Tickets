package com.top1shvetsvadim1.jarvis.domain.usecase.now_playing

import com.top1shvetsvadim1.jarvis.domain.repositories.RepositoryMoviesNowPlaying
import com.top1shvetsvadim1.jarvis.presentation.base.SuspendUseCaseNoParams
import com.top1shvetsvadim1.jarvis.presentation.base.UseCaseNoParams
import javax.inject.Inject

class FetchFirstListNowPlayingUseCase @Inject constructor(
    private val repositoryMoviesNowPlaying: RepositoryMoviesNowPlaying
): SuspendUseCaseNoParams<Unit>{

    override suspend fun invoke() {
        repositoryMoviesNowPlaying.fetchListMoviesNowPlaying()
    }
}