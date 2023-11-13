package com.top1shvetsvadim1.jarvis.data.locale_storage.converts

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.top1shvetsvadim1.jarvis.data.locale_storage.movies_details.ProductCompaniesDB
import com.top1shvetsvadim1.jarvis.data.module.ProductCompaniesAPI

class ConvertsProductCompanies {
    @TypeConverter
    fun listToJson(value: List<ProductCompaniesDB>?) = Gson().toJson(value)

    @TypeConverter
    fun jsonToList(value: String) = Gson().fromJson(value, Array<ProductCompaniesDB>::class.java).toList()
}