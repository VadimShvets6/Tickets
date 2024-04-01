package com.top1shvetsvadim1.jarvis.presentation.base

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.coreutills.misc.StateResultWithLoading
import com.top1shvetsvadim1.jarvis.presentation.utils.extentions.launchIO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged

abstract class ViewModelBase<S : State, E : Event, I : IntentMVI> : ViewModel() {

    abstract val reducer: Reducer<S, E>

    abstract fun handleIntent(intent: I)

    open fun onDestroy() {
    }

    protected inline fun launchNet(crossinline action: suspend () -> Unit) {
        viewModelScope.launchIO {
            reducer.onLoading(LaunchNet::class)
            try {
                action()
            } catch (ex: Exception) {
                reducer.onError(ex, LaunchNet::class)
            } finally {
                reducer.onFinish(LaunchNet::class)
            }
        }
    }

    fun reduceToState(reducingAction: suspend S.() -> S) {
        viewModelScope.launchIO {
            reducer.reduceToState(reducingAction)
        }
    }

    fun reduceToEvent(reducingAction: () -> E) {
        viewModelScope.launchIO {
            reducer.reduceToEvent(reducingAction)
        }
    }


    inline fun <reified T : Any, reified R> SuspendUseCase<R, T>.reduceToState(
        params: R,
        noinline reducingAction: S.(T) -> S
    ) {
        viewModelScope.launchIO {
            try {
                val result = invoke(params)
                reducer.reduceToState {
                    reducingAction(result)
                }
            } catch (ex: Exception) {
                reducer.onError(ex, this@reduceToState::class)
            }
        }
    }

    inline fun <reified T : Any?, reified R> SuspendUseCase<R, T>.reduceToEvent(
        params: R,
        showLoading: Boolean = true,
        noinline reducingAction: (T) -> E
    ) {
        viewModelScope.launchIO {
            if (showLoading) {
                reducer.onLoading(this@reduceToEvent::class)
            }
            try {
                val result = invoke(params)
                reducer.reduceToEvent {
                    reducingAction(result)
                }
            } catch (ex: Exception) {
                reducer.onError(ex, this@reduceToEvent::class)
            } finally {
                if (showLoading) {
                    reducer.onFinish(this@reduceToEvent::class)
                }
            }
        }
    }

    inline fun <reified T : Any?, reified R> SuspendUseCase<R, T>.reduceToMaybeEvent(
        params: R,
        showLoading: Boolean = true,
        noinline reducingAction: suspend (T) -> E?
    ) {
        viewModelScope.launchIO {
            if (showLoading) {
                reducer.onLoading(this@reduceToMaybeEvent::class)
            }
            try {
                val result = invoke(params)
                reducingAction(result)?.let {
                    reducer.reduceToEvent {
                        it
                    }
                }
            } catch (ex: Exception) {
                reducer.onError(ex, this@reduceToMaybeEvent::class)
            } finally {
                if (showLoading) {
                    reducer.onFinish(this@reduceToMaybeEvent::class)
                }
            }
        }
    }

    inline fun <reified T : Any, reified R> SuspendUseCase<R, T>.launch(params: R, showLoading: Boolean = true) {
        viewModelScope.launchIO {
            if (showLoading) {
                reducer.onLoading(this@launch::class)
            }
            try {
                invoke(params)
            } catch (ex: Exception) {
                reducer.onError(ex, this@launch::class)
            } finally {
                if (showLoading) {
                    reducer.onFinish(this@launch::class)
                }
            }
        }
    }

    inline fun <reified T : Any, reified R> SuspendUseCase<R, T>.launch(
        params: R, showLoading: Boolean = true,
        noinline reducingAction: (T) -> Unit = {}
    ) {
        viewModelScope.launchIO {
            if (showLoading) {
                reducer.onLoading(this@launch::class)
            }
            try {
                reducingAction(invoke(params))
            } catch (ex: Exception) {
                reducer.onError(ex, this@launch::class)
            } finally {
                if (showLoading) {
                    reducer.onFinish(this@launch::class)
                }
            }
        }
    }

    inline fun <reified T : Any> SuspendUseCaseNoParams<T>.launch(
        showLoading: Boolean = true,
        noinline reducingAction: (T) -> Unit = {}
    ) {
        viewModelScope.launchIO {
            if (showLoading) {
                reducer.onLoading(this@launch::class)
            }
            try {
                reducingAction(invoke())
            } catch (ex: Exception) {
                reducer.onError(ex, this@launch::class)
            } finally {
                if (showLoading) {
                    reducer.onFinish(this@launch::class)
                }
            }
        }
    }

    inline fun <reified T : Any, reified R> SuspendUseCase<R, T>.fetch(params: R, showLoading: Boolean = false) {
        viewModelScope.launchIO {
            if (showLoading) {
                reducer.onLoading(this@fetch::class)
            }
            try {
                invoke(params)
            } catch (ex: Exception) {
                Log.d("Deb", "Error happened in ${this@fetch::class.simpleName} (${ex.message})")
                ex.printStackTrace()
            } finally {
                if (showLoading) {
                    reducer.onFinish(this@fetch::class)
                }
            }
        }
    }

    inline fun <reified T : Any> SuspendUseCaseNoParams<T>.reduceToState(crossinline reducingAction: S.(T) -> S) {
        viewModelScope.launchIO {
            try {
                val result = invoke()
                reducer.reduceToState {
                    reducingAction(result)
                }
            } catch (ex: Exception) {
                reducer.onError(ex, this@reduceToState::class)
            }
        }
    }

    inline fun <reified T : Any> SuspendUseCaseNoParams<T>.fetch(showLoading: Boolean = false) {
        viewModelScope.launchIO {
            if (showLoading) {
                reducer.onLoading(this@fetch::class)
            }
            try {
                invoke()
            } catch (ex: Exception) {
                Log.d("Deb", "Error happened in ${this@fetch::class.simpleName} (${ex.message})")
                ex.printStackTrace()
            } finally {
                if (showLoading) {
                    reducer.onFinish(this@fetch::class)
                }
            }
        }
    }

    inline fun <reified T : Any> UseCaseNoParams<Flow<T>>.flowToState(crossinline reducingAction: suspend S.(T) -> S) {
        viewModelScope.launchIO {
            try {
                invoke().distinctUntilChanged().collectLatest { result ->
                    result.let {
                        reducer.reduceToState {
                            reducingAction(it)
                        }
                    }
                }
            } catch (ex: Exception) {
                reducer.onError(ex, this@flowToState::class)
            }
        }
    }

    inline fun <reified T : Any> UseCaseNoParams<Flow<T?>>.flowToStateNullable(crossinline reducingAction: suspend S.(T) -> S) {
        viewModelScope.launchIO {
            try {
                invoke().distinctUntilChanged().collectLatest { result ->
                    result?.let {
                        reducer.reduceToState {
                            reducingAction(it)
                        }
                    }
                }
            } catch (ex: Exception) {
                reducer.onError(ex, this@flowToStateNullable::class)
            }
        }
    }

    inline fun <reified T : Any> UseCaseNoParams<Flow<T>>.flowToStateWithLoading(crossinline reducingAction: suspend S.(StateResultWithLoading<T>) -> S) {
        viewModelScope.launchIO {
            try {
                combine(
                    invoke().distinctUntilChanged(),
                    reducer.loadingOutputFlow
                ) { result, loading ->
                    StateResultWithLoading(
                        stateResult = result,
                        loadingPairs = loading
                    )
                }.collectLatest {
                    reducer.reduceToState {
                        reducingAction(it)
                    }
                }
            } catch (ex: Exception) {
                reducer.onError(ex, this@flowToStateWithLoading::class)
            }
        }
    }

    inline fun <reified T : Any, reified P : Any> UseCase<P, Flow<T>>.flowToStateWithLoading(
        params: P,
        crossinline reducingAction: suspend S.(StateResultWithLoading<T>) -> S
    ) {
        viewModelScope.launchIO {
            try {
                combine(
                    invoke(params).distinctUntilChanged(),
                    reducer.loadingOutputFlow
                ) { result, loading ->
                    StateResultWithLoading(
                        stateResult = result,
                        loadingPairs = loading
                    )
                }.collectLatest {
                    reducer.reduceToState {
                        reducingAction(it)
                    }
                }
            } catch (ex: Exception) {
                reducer.onError(ex, this@flowToStateWithLoading::class)
            }
        }
    }

    inline fun <reified T : Any, reified P : Any> UseCase<P, Flow<T>>.flowToState(
        params: P,
        crossinline reducingAction: suspend S.(T) -> S
    ) {
        viewModelScope.launchIO {
            try {
                invoke(params).distinctUntilChanged().collectLatest {
                    reducer.reduceToState {
                        reducingAction(it)
                    }
                }
            } catch (ex: Exception) {
                reducer.onError(ex, this@flowToState::class)
            }
        }
    }

    inline fun <reified T : Any, reified P : Any> UseCase<P, Flow<T?>>.flowToStateNullable(
        params: P,
        crossinline reducingAction: suspend S.(T) -> S
    ) {
        viewModelScope.launchIO {
            try {
                invoke(params).collectLatest { result ->
                    result?.let {
                        reducer.reduceToState {
                            reducingAction(it)
                        }
                    }
                }
            } catch (ex: Exception) {
                reducer.onError(ex, this@flowToStateNullable::class)
            }
        }
    }

    inline fun <reified T : Any, reified P : Any> UseCase<P, Flow<T?>>.flowToStateNullableWithLoading(
        params: P,
        crossinline reducingAction: suspend S.(StateResultWithLoading<T>) -> S
    ) {
        viewModelScope.launchIO {
            try {
                combine(
                    invoke(params).distinctUntilChanged(),
                    reducer.loadingOutputFlow
                ) { result, loading ->
                    StateResultWithLoading(
                        stateResult = result ?: return@combine null,
                        loadingPairs = loading
                    )
                }.collectLatest { stateResult ->
                    stateResult?.let {
                        reducer.reduceToState {
                            reducingAction(stateResult)
                        }
                    }
                }
            } catch (ex: Exception) {
                reducer.onError(ex, this@flowToStateNullableWithLoading::class)
            }
        }
    }

}

interface State

interface IntentMVI
interface Event
interface LaunchNet