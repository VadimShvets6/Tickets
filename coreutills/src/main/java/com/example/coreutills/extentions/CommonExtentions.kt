package com.example.coreutills.extentions

import android.util.Log
import com.google.gson.Gson
import kotlin.reflect.KClass

inline fun <reified T> tryNull(tryAction: () -> T?): T? {
    return try {
        tryAction()
    } catch (ex: Exception) {
        null
    }
}

inline fun <reified T> tryNullLog(tryAction: () -> T?): T? {
    return try {
        tryAction()
    } catch (ex: Exception) {
        Log.d("TryNullLog", "_".repeat(20))
        ex.printStackTrace()
        null
    }
}

inline fun <reified T> tryLogException(tryAction: () -> T?): T? {
    return try {
        tryAction()
    } catch (ex: Exception) {
        ex.printStackTrace()
        null
    }
}

inline fun <reified T> T.toJSON(): String {
    return Gson().toJson(this)
}

inline fun <reified T> String.fromJSONTo(): T {
    return Gson().fromJson(this, T::class.java)
}

val KClass<*>.nameTag get() = qualifiedName ?: ""
