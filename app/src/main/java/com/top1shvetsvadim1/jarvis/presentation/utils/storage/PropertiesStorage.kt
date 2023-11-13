package com.top1shvetsvadim1.jarvis.presentation.utils.storage

import android.util.Log
import com.example.coreutills.utils.SharedPreference
import com.top1shvetsvadim1.jarvis.presentation.controler.ContextManager.retrieveApplicationContext
import com.top1shvetsvadim1.jarvis.presentation.utils.extentions.launchIO
import com.top1shvetsvadim1.jarvis.presentation.utils.storage.PropertiesStorage.Properties.PotentialUserEmailAddress
import com.top1shvetsvadim1.jarvis.presentation.utils.storage.PropertiesStorage.Properties.UserLanguage
import com.top1shvetsvadim1.jarvis.presentation.utils.storage.PropertiesStorage.Properties.ReadMoreClue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Locale
import kotlin.coroutines.coroutineContext

object PropertiesStorage {
    enum class Properties {
        PotentialUserEmailAddress, UserLanguage, ReadMoreClue
    }

    inline fun <reified T> Properties.default(): T {
        val def: Any = when (this) {
            PotentialUserEmailAddress -> ""
            UserLanguage -> Locale.getDefault().language
            ReadMoreClue -> true
        }
        return if (def is T) def else throw Exception("Invalid default param")
    }

    inline fun <reified T : Any> set(property: Properties, value: T) {
        val sp = SharedPreference.getInstance()
        val key = property.name
        Log.d("deb", "Props set: ${property.name}, $value, ${retrieveApplicationContext()}, $sp}")
        when (value) {
            is String -> sp.SaveSharedPreference(retrieveApplicationContext(), key, value)
            is Boolean -> sp.SaveSharedPreference(retrieveApplicationContext(), key, value)
            is Int -> sp.SaveSharedPreference(retrieveApplicationContext(), key, value)
        }
    }

    suspend inline fun <reified T : Any> setAsync(property: Properties, value: T) {
        withContext(Dispatchers.IO) {
            set(property, value)
        }
    }

    inline fun <reified T : Any> setDropAsync(property: Properties, value: T) {
        CoroutineScope(Dispatchers.IO).launchIO {
            set(property, value)
        }
    }

    inline fun <reified T : Any> get(property: Properties): T {
        Log.d("deb", "Prop: $property")
        val sp = SharedPreference.getInstance()
        Log.d("deb", "Prop shared: $sp")
        val key = property.name
        Log.d("deb", "Prop key: $key")
        return when (T::class) {
            String::class -> sp.GetSharedPreference(
                retrieveApplicationContext(),
                key,
                property.default() as String
            ) as T

            Boolean::class -> sp.GetSharedPreference(
                retrieveApplicationContext(),
                key,
                property.default() as Boolean
            ) as T

            Int::class -> sp.GetSharedPreferenceInt(
                retrieveApplicationContext(),
                key,
                property.default() as Int
            ) as T

            else -> Unit as T
        }
    }

    fun getBoolean(property: Properties): Boolean = get(property)

    fun getString(property: Properties): String = get(property)

    fun getInt(property: Properties): Int = get(property)

    private inline fun <reified T> getValueKey(key: String): T {
        val sp = SharedPreference.getInstance()
        return when (T::class) {
            String::class -> sp.GetSharedPreference(
                retrieveApplicationContext(),
                key,
                ""
            ) as T

            Boolean::class -> sp.GetSharedPreference(
                retrieveApplicationContext(),
                key,
                false
            ) as T

            Int::class -> sp.GetSharedPreferenceInt(
                retrieveApplicationContext(),
                key,
                1
            ) as T

            else -> throw Exception("Cannot get serializable default by key.")
        }
    }

    private inline fun <reified T> setValueKey(key: String, value: T) {
        val sp = SharedPreference.getInstance()
        when (value) {
            is String -> sp.SaveSharedPreference(retrieveApplicationContext(), key, value)
            is Boolean -> sp.SaveSharedPreference(retrieveApplicationContext(), key, value)
            is Int -> sp.SaveSharedPreference(retrieveApplicationContext(), key, value)
            else -> throw Exception("Cannot save serializable value with key.")
        }
    }

    suspend inline fun <reified T : Any> getSuspend(key: Properties): T {
        return withContext(coroutineContext) {
            get<T>(key)
        }
    }

}