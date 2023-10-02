package com.top1shvetsvadim1.jarvis.di.module

import com.top1shvetsvadim1.jarvis.data.impl.RepositoryMoviesNowPlayingImpl
import com.top1shvetsvadim1.jarvis.domain.repositories.RepositoryMoviesNowPlaying
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindRepositoryNowPlayn(impl: RepositoryMoviesNowPlayingImpl): RepositoryMoviesNowPlaying
}
