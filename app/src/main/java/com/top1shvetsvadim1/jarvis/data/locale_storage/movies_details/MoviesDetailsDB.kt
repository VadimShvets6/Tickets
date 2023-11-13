package com.top1shvetsvadim1.jarvis.data.locale_storage.movies_details

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.google.gson.annotations.SerializedName
import com.top1shvetsvadim1.jarvis.data.locale_storage.converts.ConvertsProductCompanies
import com.top1shvetsvadim1.jarvis.data.locale_storage.converts.IntTypeConverts
import com.top1shvetsvadim1.jarvis.data.module.GenreAPI
import com.top1shvetsvadim1.jarvis.data.module.ProductCompaniesAPI

@Entity("movies_details")
data class MoviesDetailsDB(
    val adult: Boolean,
    val backdropPath: String?,
    val budged: Long,
    @TypeConverters(IntTypeConverts::class) val genreId: List<Int>,
    @PrimaryKey val id: Int,
    val overview: String,
    val popularity: Double,
    val posterPath: String?,
    @TypeConverters(ConvertsProductCompanies::class) val productionCompanies: List<ProductCompaniesDB>,
    val releaseDate: String,
    val revenue: Long,
    val runtime: Int,
    val status: String,
    val title: String,
    val voteAverage: Double,
    val voteCount: Int
)

data class ProductCompaniesDB(
    val id: Int,
    val logoPath: String?,
    val companiesName: String,
)
