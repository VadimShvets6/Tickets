@file:Suppress("UNCHECKED_CAST")

package com.top1shvetsvadim1.jarvis.presentation.utils.extentions

import com.example.coreutills.extentions.tryLogException
import com.example.coreutills.extentions.tryNull
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.coroutineContext

fun <T> Channel<T>.send(lifecycleScope: CoroutineScope, value: T) {
    lifecycleScope.launch(Dispatchers.IO) {
        send(value)
    }
}

fun <T> MutableStateFlow<T>.emit(lifecycleScope: CoroutineScope, value: T) {
    lifecycleScope.launch(Dispatchers.IO) {
        emit(value)
    }
}

inline fun CoroutineScope.launchIO(crossinline action: suspend CoroutineScope.() -> Unit): Job {
    return launch(Dispatchers.IO) {
        action()
    }
}

inline fun CoroutineScope.launchUI(crossinline action: suspend CoroutineScope.() -> Unit): Job {
    return launch(Dispatchers.Main) {
        action()
    }
}

inline fun CoroutineScope.launchSafeInIO(
    crossinline onError: (Throwable) -> Unit = {},
    crossinline action: suspend CoroutineScope.() -> Unit
): Job {
    return launch(Dispatchers.IO) {
        try {
            action(this)
        } catch (ex: Exception) {
            ex.printStackTrace()
            onError(ex)
        }
    }
}

@Suppress("DeferredResultUnused")
inline fun CoroutineScope.dropAsync(
    crossinline async: suspend () -> Unit
) {
    async(Dispatchers.IO) {
        tryLogException {
            async()
        }
    }
}

suspend inline fun CoroutineScope.switchToUI(crossinline action: suspend CoroutineScope.() -> Unit) {
    withContext(Dispatchers.Main) {
        action()
    }
}

suspend inline fun <T, R> List<T>.mapAsync(crossinline transformation: suspend (T) -> R): List<R> {
    return withContext(coroutineContext) {
        map {
            async(Dispatchers.IO) {
                transformation(it)
            }
        }.awaitAll()
    }
}

@Suppress("UNCHECKED_CAST")
suspend inline fun <T, R> List<T>.mapAsyncNotNull(crossinline transformation: (T) -> R?): List<R> {
    return withContext(coroutineContext) {
        map {
            async(Dispatchers.IO) {
                transformation(it)
            }
        }.awaitAll()
    }.filterNot { it == null } as List<R>
}

@Suppress("UNCHECKED_CAST")
suspend inline fun <T, reified R> List<T>.mapAsyncSafeNotNull(crossinline transformation: (T) -> R?): List<R> {
    return withContext(coroutineContext) {
        map {
            async(Dispatchers.IO) {
                tryNull {
                    transformation(it)
                }
            }
        }.awaitAll()
    }.filterNot { it == null } as List<R>
}

suspend inline fun <T : Any, R> List<T>.mapIndexedAsync(crossinline transformation: (Int, T) -> R): List<R> {
    return withContext(coroutineContext) {
        mapIndexed { index, item ->
            async(Dispatchers.IO) {
                transformation(index, item)
            }
        }.awaitAll()
    }
}

inline fun <reified T> CoroutineScope.initAsync(crossinline initializer: suspend () -> T) = async(coroutineContext) {
    initializer()
}

suspend inline fun <reified T> MutableList<Deferred<T>>.addAsync(crossinline action: suspend () -> T) {
    withContext(coroutineContext) {
        add(
            async(Dispatchers.IO) {
                action()
            }
        )
    }
}

inline fun <reified T> MutableList<T>.add(crossinline action: () -> T) {
    add(
        action()
    )
}


inline fun <T1, T2, T3, T4, T5, T6, R> combine(
    flow: Flow<T1>,
    flow2: Flow<T2>,
    flow3: Flow<T3>,
    flow4: Flow<T4>,
    flow5: Flow<T5>,
    flow6: Flow<T6>,
    crossinline transform: suspend (T1, T2, T3, T4, T5, T6) -> R
): Flow<R> = kotlinx.coroutines.flow.combine(flow, flow2, flow3, flow4, flow5, flow6) { args: Array<*> ->
    transform(
        args[0] as T1,
        args[1] as T2,
        args[2] as T3,
        args[3] as T4,
        args[4] as T5,
        args[5] as T6
    )
}

inline fun <T1, T2, T3, T4, T5, T6, T7, R> combine(
    flow: Flow<T1>,
    flow2: Flow<T2>,
    flow3: Flow<T3>,
    flow4: Flow<T4>,
    flow5: Flow<T5>,
    flow6: Flow<T6>,
    flow7: Flow<T7>,
    crossinline transform: suspend (T1, T2, T3, T4, T5, T6, T7) -> R
): Flow<R> = kotlinx.coroutines.flow.combine(flow, flow2, flow3, flow4, flow5, flow6, flow7) { args: Array<*> ->
    transform(
        args[0] as T1,
        args[1] as T2,
        args[2] as T3,
        args[3] as T4,
        args[4] as T5,
        args[5] as T6,
        args[6] as T7
    )
}