package com.top1shvetsvadim1.jarvis.data.module

import com.google.gson.annotations.SerializedName

data class ExternalMoviesAPI(
    @SerializedName("id") val id: Int,
    @SerializedName("imbd_id") val imdbId: String,
    @SerializedName("wikidata_id") val wikidataId: String,
    @SerializedName("facebook_id") val facebookId: String,
    @SerializedName("instagram_id") val instagramId: String,
    @SerializedName("twitter_id") val twitterId: String
)