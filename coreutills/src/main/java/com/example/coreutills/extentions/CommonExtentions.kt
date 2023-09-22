package com.example.coreutills.extentions

import android.util.Log
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

val KClass<*>.nameTag get() = qualifiedName ?: ""
