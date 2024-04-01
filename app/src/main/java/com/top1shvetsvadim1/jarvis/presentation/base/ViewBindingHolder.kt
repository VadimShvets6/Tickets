package com.top1shvetsvadim1.jarvis.presentation.base

import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.viewbinding.ViewBinding
import com.top1shvetsvadim1.jarvis.presentation.utils.extentions.launchUI
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.ensureActive

interface ViewBindingHolder<B : ViewBinding> {
    var binding: B?

    fun requireBinding() = checkNotNull(binding)

    fun requireBinding(lambda: (binding: B) -> Unit) {
        binding?.let(lambda)
    }

    fun applyOnBinding(lambda: B.() -> Unit) {
        binding?.let(lambda)
    }

    fun registerOnBinding(coroutineScope: CoroutineScope, lambda: B.() -> Unit) {
        if (binding != null) {
            applyOnBinding(lambda)
        } else {
            coroutineScope.launchUI {
                while (binding == null) {
                    delay(100)
                    ensureActive()
                }
                applyOnBinding(lambda)
            }
        }
    }

    @Suppress("DEPRECATION")
    fun hideSoftInput() {
        binding?.root?.let { ViewCompat.getWindowInsetsController(it)?.hide(WindowInsetsCompat.Type.ime()) }
    }

    fun registerBinding(binding: B, lifecycleOwner: LifecycleOwner) {
        this.binding = binding
        lifecycleOwner.lifecycle.addObserver(object : DefaultLifecycleObserver {
            override fun onDestroy(owner: LifecycleOwner) {
                owner.lifecycle.removeObserver(this)
                beforeDestroyView()
                this@ViewBindingHolder.binding = null
            }
        })
    }

    fun beforeDestroyView()
}