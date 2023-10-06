package com.top1shvetsvadim1.jarvis.data.locale_storage.converts

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class IntTypeConverts {

    @TypeConverter
    fun listToJson(value: List<Int>) = Gson().toJson(value)

    @TypeConverter
    fun jsonToList(value: String): List<Int> {
        return Gson().fromJson(
            value,
            object : TypeToken<List<Int>>() {}.type
        )
    }
}