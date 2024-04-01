package com.top1shvetsvadim1.jarvis.data.service

import com.top1shvetsvadim1.jarvis.data.module.ExternalMoviesAPI
import com.top1shvetsvadim1.jarvis.data.module.GenreResponse
import com.top1shvetsvadim1.jarvis.data.module.MovieDetailsAPI
import com.top1shvetsvadim1.jarvis.data.module.MoviesCastResponse
import com.top1shvetsvadim1.jarvis.data.module.MoviesResponseAPI
import com.top1shvetsvadim1.jarvis.data.module.PeopleResponse
import com.top1shvetsvadim1.jarvis.presentation.utils.ApiValues
import com.top1shvetsvadim1.jarvis.presentation.utils.CurrentUser
import retrofit2.http.GET
import retrofit2.http.Path
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
        @Query("region") region: String = CurrentUser.userLanguage,
        @Query("api_key") apiKey: String = ApiValues.API_KEY
    ): MoviesResponseAPI

    @GET("movie/top_rated")
    suspend fun getMoviesTopRated(
        @Query("page") page: Int,
        @Query("language") language: String = CurrentUser.userLanguage,
        @Query("api_key") apiKey: String = ApiValues.API_KEY
    ): MoviesResponseAPI

    @GET("movie/upcoming")
    suspend fun getMoviesUpcoming(
        @Query("page") page: Int,
        @Query("language") language: String = CurrentUser.userLanguage,
        @Query("region") region: String = CurrentUser.userLanguage,
        @Query("api_key") apiKey: String = ApiValues.API_KEY
    ): MoviesResponseAPI

    @GET("trending/movie/{time_window}")
    suspend fun getTrendingMovies(
        @Path("time_window") time: String = "day",
        @Query("language") language: String = CurrentUser.userLanguage,
        @Query("api_key") apiKey: String = ApiValues.API_KEY
    ): MoviesResponseAPI

    @GET("trending/person/{time_window}")
    suspend fun getTrendingPeople(
        @Path("time_window") time: String = "day",
        @Query("language") language: String = CurrentUser.userLanguage,
        @Query("api_key") apiKey: String = ApiValues.API_KEY
    ): PeopleResponse

    @GET("movie/{movie_id}")
    suspend fun getMoviesDetailsById(
        @Path("movie_id") id: Int,
        @Query("language") language: String = CurrentUser.userLanguage,
        @Query("api_key") apiKey: String = ApiValues.API_KEY
    ): MovieDetailsAPI

    @GET("movie/{movie_id}/credits")
    suspend fun getMovieActorsById(
        @Path("movie_id") id: Int,
        @Query("language") language: String = CurrentUser.userLanguage,
        @Query("api_key") apiKey: String = ApiValues.API_KEY
    ) : MoviesCastResponse

    @GET("movie/{movie_id}/external_ids")
    suspend fun getMoviesExternal(
        @Path("movie_id") id: Int,
        @Query("api_key") apiKey: String = ApiValues.API_KEY
    ) : ExternalMoviesAPI
}