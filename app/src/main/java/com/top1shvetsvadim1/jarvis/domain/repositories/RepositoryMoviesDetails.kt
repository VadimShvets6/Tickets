package com.top1shvetsvadim1.jarvis.domain.repositories

import com.top1shvetsvadim1.jarvis.domain.models.ActorsModel
import com.top1shvetsvadim1.jarvis.domain.models.MovieDetails
import kotlinx.coroutines.flow.Flow

interface RepositoryMoviesDetails {

    suspend fun fetchMoviesById(id: Int)

    suspend fun getMoviesById(id: Int): Flow<MovieDetails>

    suspend fun getMoviesActorsById(id: Int) : Flow<List<ActorsModel>>
}