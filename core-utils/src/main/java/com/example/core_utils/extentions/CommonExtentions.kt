package com.example.core_utils.extentions

inline fun <reified T> tryLogException(tryAction: () -> T?): T? {
    return try {
        tryAction()
    } catch (ex: Exception) {
        ex.printStackTrace()
        null
    }
}

inline fun <reified T> tryNull(tryAction: () -> T?): T? {
    return try {
        tryAction()
    } catch (ex: Exception) {
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