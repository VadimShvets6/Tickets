package com.top1shvetsvadim1.jarvis.presentation.base

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.viewbinding.ViewBinding
import com.top1shvetsvadim1.jarvis.presentation.utils.extentions.launchIO
import com.top1shvetsvadim1.jarvis.presentation.utils.extentions.switchToUI
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.net.UnknownHostException
import javax.net.ssl.SSLHandshakeException

abstract class FragmentBaseMVI<VB : ViewBinding, S : State, E : Event, VM : ViewModelBase<S, E, *>> :
    SuperFragment<VB>() {

   // abstract var viewModelFactory: ViewModelProvider.Factory
    abstract val viewModel: VM
    val currentState get() = viewModel.reducer.currentState
    protected val savedState: MutableMap<String, Any?> = mutableMapOf()


    /*inline fun <reified T : ViewModel> factoredViewModel(): Lazy<T> {
        return lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
            ViewModelProvider(this, viewModelFactory)[T::class.java]
        }
    }*/

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        applyOnViews().invoke(requireBinding())
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.reducer.state.collectLatest {
                    render(it)
                }

            }
        }
        lifecycleScope.launchWhenResumed {
            viewModel.reducer.effects.collectLatest {
                onEvent(it)
            }
        }
        lifecycleScope.launchWhenResumed {
            viewModel.reducer.errors.collectLatest {
                onException(it)
            }
        }
    }

    protected open fun onException(ex: Exception) {
        when (ex) {
            is HttpException -> handleHttpException(ex)
            is UnknownHostException -> handleNoInternetConnection(ex)
            is SSLHandshakeException -> handleSSLException(ex)
            else -> handleGenericExtension(ex)
        }
    }

    private fun handleHttpException(ex: HttpException) {
        when (ex.response()?.code()) {
            401 -> handleUnauthorizedException()
            404 -> handleNotFoundException()
            502 -> handleGenericExtension(ex)
            else -> handleGenericHttpExtension(ex)
        }
    }

    open fun handleUnauthorizedException() {
        //TODO
    }

    /**
     * 401 and 404 exceptions are handled in [handleUnauthorizedException] and [handleNotFoundException].
     */
    open fun handleGenericHttpExtension(ex: HttpException) {
        /*HttpErrorHandler.handleError(
            requireContext(),
            true,
            ex.response() ?: return
        ) { _, errorMessage ->
            applyOnContext {
               *//* DialogErrorNew(
                    context = this,
                    errorMessage = stringChain(errorMessage, getString(StringsResource.error_unexpected))
                ).show()*//*
            }
        }*/
    }

    open fun handleNotFoundException() {
        //val errorMessage = getString(StringsResource.system_error_message)
        applyOnContext {
            /*DialogErrorNew(
                context = this,
                errorMessage = errorMessage
            ).show()*/
        }
    }

    open fun handleGenericExtension(ex: Exception) {
        //Check message:
        when {
            ex.message?.contains("UnknownHost") == true -> handleNoInternetConnection(
                UnknownHostException()
            )

            else -> applyOnContext {
                /* DialogErrorNew(
                     context = this,
                     errorMessage = getString(StringsResource.error_unexpected)
                 ).show()*/
            }
        }
    }

    open fun handleSSLException(ex: SSLHandshakeException) {
        //TODO
    }

    open fun handleNoInternetConnection(ex: UnknownHostException) {
        /*applyOnContext {
            DialogErrorNew(
                context = this,
                errorMessage = getString(StringsResource.error_network)
            ).show()
        }*/
    }

    abstract fun render(state: S)

    open fun onEvent(effect: E) {

    }

    abstract fun applyOnViews(): VB.() -> Unit

    protected inline fun <reified T> asyncCompute(
        crossinline compute: () -> T,
        crossinline assign: (T) -> Unit
    ) {
        lifecycleScope.launchIO {
            val computation = compute()
            switchToUI {
                assign(computation)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        savedInstanceState?.keySet()?.forEach {
            savedState[it] = savedInstanceState.get(it)
        }
    }

    //Extensions
   /* fun <ARG : BaseBottomDialogLightFragment.BottomDialogParams> BaseBottomDialogLightFragment<*, ARG>.show(
        param: ARG
    ) {
        applyOnContext {
            show(this@FragmentBaseMVI, param)
        }
    }*/
}