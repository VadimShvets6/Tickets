package com.top1shvetsvadim1.jarvis.data.module

import com.google.gson.annotations.SerializedName

data class ProductCompaniesAPI(
    @SerializedName("id")
    val id: Int,
    @SerializedName("logo_path")
    val logoPath: String?,
    @SerializedName("name")
    val companiesName: String,
)