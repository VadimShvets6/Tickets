package com.top1shvetsvadim1.jarvis.data.module

import com.google.gson.annotations.SerializedName

data class MoviesReviewsResponse(
    @SerializedName("results")
    val movieReviews: List<MovieReviewsAPI>
)

data class MovieReviewsAPI(
    @SerializedName("author")
    val author: String,
    @SerializedName("author_details")
    val authorDetails: AuthorDetailsAPI,
    @SerializedName("content")
    val content: String,
    @SerializedName("created_at")
    val createdAt: String
)

data class AuthorDetailsAPI(
    @SerializedName("name")
    val name: String?,
    @SerializedName("username")
    val username: String,
    @SerializedName("avatar_path")
    val avatarPath: String,
    @SerializedName("rating")
    val rating: Int?
)