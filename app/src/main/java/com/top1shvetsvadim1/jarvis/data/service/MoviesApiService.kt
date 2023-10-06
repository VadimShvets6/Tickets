package com.top1shvetsvadim1.jarvis.data.service

import com.top1shvetsvadim1.jarvis.data.module.GenreResponse
import com.top1shvetsvadim1.jarvis.data.module.MoviesResponseAPI
import com.top1shvetsvadim1.jarvis.presentation.utils.ApiValues
import com.top1shvetsvadim1.jarvis.presentation.utils.CurrentUser
import retrofit2.http.GET
import retrofit2.http.Query

interface MoviesApiService {

    @GET("movie/now_playing")
    suspend fun getMoviesNowPlaying(
        @Query("page") page: Int,
        @Query("language") language: String = CurrentUser.userLanguage,
        @Query("api_key") apiKey: String = ApiValues.API_KEY,
    ): MoviesResponseAPI

    @GET("genre/movie/list")
    suspend fun getGenreList(
        @Query("language") language: String = CurrentUser.userLanguage,
        @Query("api_key") apiKey: String = ApiValues.API_KEY
    ): GenreResponse

    @GET("movie/popular")
    suspend fun getMoviesPopular(
        @Query("page") page: Int,
        @Query("language") language: String = CurrentUser.userLanguage,
        @Query("api_key") apiKey: String = ApiValues.API_KEY
    ): MoviesResponseAPI
}