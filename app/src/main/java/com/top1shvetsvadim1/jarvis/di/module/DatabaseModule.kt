package com.top1shvetsvadim1.jarvis.di.module

import android.content.Context
import com.top1shvetsvadim1.jarvis.data.locale_storage.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase =
        AppDatabase.getInstance(context)

    @Provides
    @Singleton
    fun provideMoviesNowPlayingDao(appDatabase: AppDatabase) = appDatabase.moviesNowPlayingDao()

    @Provides
    @Singleton
    fun provideMoviesGenreDao(appDatabase: AppDatabase) = appDatabase.moviesGenreDao()

    @Provides
    @Singleton
    fun provideMoviesPopular(appDatabase: AppDatabase) = appDatabase.moviePopularDao()
}