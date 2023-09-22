package com.top1shvetsvadim1.jarvis.presentation.base

import android.util.Log
import com.example.coreutills.extentions.nameTag
import com.example.coreutills.misc.LoadingStateMapWrapper
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlin.reflect.KClass

abstract class Reducer<S : State, E : Event>(initialState: S) {

    private val _state = MutableStateFlow(initialState)
    val state = _state.asStateFlow()

    val currentState get() = state.value

    private val effectChannel = Channel<E>(Channel.RENDEZVOUS)
    val effects = effectChannel.receiveAsFlow()

    private val exceptionChannel = Channel<Exception>(Channel.RENDEZVOUS)
    val errors = exceptionChannel.receiveAsFlow()

    private val loadingInputTaggedChannel = MutableStateFlow(LoadingStateMapWrapper(emptyMap()))
    val loadingOutputFlow = loadingInputTaggedChannel.asStateFlow().map { it.map }

    /**
     * You should pass there use cases classes via [KClass] that you launch via [ViewModelBase.launch], [ViewModelBase.reduceToState], [ViewModelBase.flowToState], [ViewModelBase.flowToStateNullable].
     * Exceptions thrown from this use cases WILL NOT trigger parent fragment error handler.
     *
     * You can launch use cases via [ViewModelBase.fetch] without passing it to this list.
     */
    protected open val errorExcludedUseCases = listOf<KClass<*>>()

    suspend fun sendState(newState: S) {
        _state.emit(newState)
    }

    suspend inline fun updateState(manageState: S.() -> S) {
        sendState(manageState(currentState))
    }

    suspend fun pushEvent(event: E) {
        effectChannel.send(event)
    }

    suspend fun <T> reduceToState(source: T, reducerAction: S.(T) -> S) {
        sendState(currentState.reducerAction(source))
    }

    suspend fun <T> reduceToEvent(source: T, reducerAction: (T) -> E) {
        pushEvent(reducerAction(source))
    }

    suspend fun reduceToState(reducerAction: suspend S.() -> S) {
        sendState(currentState.reducerAction())
    }

    suspend fun reduceToEvent(reducerAction: () -> E) {
        pushEvent(reducerAction())
    }

    open suspend fun onLoading(source: KClass<*>) {
        loadingInputTaggedChannel.emit(
            loadingInputTaggedChannel.value.copy(
                map = loadingInputTaggedChannel.value.map.toMutableMap().apply {
                    onLoadingChanged(source, true)
                    this[transformSourceToTag(source)] = true
                }
            )
        )
    }

    open fun onLoadingChanged(source: KClass<*>, value: Boolean) {

    }

    open suspend fun onFinish(source: KClass<*>) {
        loadingInputTaggedChannel.emit(
            loadingInputTaggedChannel.value.copy(
                map = loadingInputTaggedChannel.value.map.toMutableMap().apply {
                    onLoadingChanged(source, false)
                    this[transformSourceToTag(source)] = false
                }
            )
        )
    }

    protected open fun transformSourceToTag(source: KClass<*>): String {
        return source.nameTag
    }

    open suspend fun onError(ex: Exception, source: KClass<*>) {
        Log.d("Deb", "Error happened in ${source.simpleName} (${ex.message})")
        ex.printStackTrace()
        if (source !in errorExcludedUseCases) {
            Log.d("Deb", "Error passed in error handler")
            exceptionChannel.send(ex)
        }
    }
}

object NoEvents : Event
