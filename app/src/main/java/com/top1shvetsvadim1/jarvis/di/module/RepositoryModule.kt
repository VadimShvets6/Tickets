package com.top1shvetsvadim1.jarvis.di.module

import com.top1shvetsvadim1.jarvis.data.impl.RepositoryGenresImpl
import com.top1shvetsvadim1.jarvis.data.impl.RepositoryMoviesNowPlayingImpl
import com.top1shvetsvadim1.jarvis.data.impl.RepositoryMoviesPopularImpl
import com.top1shvetsvadim1.jarvis.domain.repositories.RepositoryGenres
import com.top1shvetsvadim1.jarvis.domain.repositories.RepositoryMoviesNowPlaying
import com.top1shvetsvadim1.jarvis.domain.repositories.RepositoryMoviesPopular
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindRepositoryNowPlayn(impl: RepositoryMoviesNowPlayingImpl): RepositoryMoviesNowPlaying

    @Binds
    abstract fun bindRepositoryGenres(impl: RepositoryGenresImpl): RepositoryGenres

    @Binds
    abstract fun bindRepositoryPopular(impl: RepositoryMoviesPopularImpl): RepositoryMoviesPopular
}
