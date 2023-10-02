package com.top1shvetsvadim1.jarvis.data.impl

import android.util.Log
import com.top1shvetsvadim1.jarvis.data.service.MoviesApiService
import com.top1shvetsvadim1.jarvis.domain.repositories.RepositoryMoviesNowPlaying
import javax.inject.Inject

class RepositoryMoviesNowPlayingImpl @Inject constructor(
    private val remoteMovies: MoviesApiService
) : RepositoryMoviesNowPlaying {

    override suspend fun getListMoviesNowPlaying() {
        Log.d("deb", "${remoteMovies.getMoviesNowPlaying(1).moviesNowPlaying}")
    }
}