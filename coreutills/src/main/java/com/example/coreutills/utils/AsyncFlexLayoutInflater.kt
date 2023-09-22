package com.example.coreutills.utils

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AsyncFlexLayoutInflater(val context: Context) {

    private val scope = CoroutineScope(Dispatchers.Default)

    fun inflateAsync(layoutID: Int, parentView: ViewGroup, onResult: (View?) -> Unit) {
        scope.launch {
            try {
                val view = BasicInflater(context = context).inflate(layoutID, parentView, false)
                onResult(view)
                withContext(Dispatchers.Main) {

                }
            } catch (ex: Exception) {
                Log.d("AsyncInflated", "Failed with ${ex.printStackTrace()}")
                ex.printStackTrace()
                onResult(null)
                withContext(Dispatchers.Main) {

                }
            }
        }
    }

    fun inflateAsyncWithMain(layoutID: Int, parentView: ViewGroup, onResult: (View?) -> Unit) {
        scope.launch {
            try {
                val view = BasicInflater(context = context).inflate(layoutID, parentView, false)
                withContext(Dispatchers.Main) {
                    onResult(view)
                }
            } catch (ex: Exception) {
                withContext(Dispatchers.Main) {
                    Log.d("AsyncInflated", "Failed with ${ex.printStackTrace()}")
                    ex.printStackTrace()
                    onResult(null)
                }
            }
        }
    }

    fun breakInflation() {
        scope.coroutineContext.cancelChildren()
    }

    private class BasicInflater(context: Context?) : LayoutInflater(context) {

        override fun cloneInContext(newContext: Context): LayoutInflater {
            return BasicInflater(newContext)
        }

        override fun onCreateView(name: String, attrs: AttributeSet): View {
            for (prefix in sClassPrefixList) {
                try {
                    val view = createView(name, prefix, attrs)
                    if (view != null) {
                        return view
                    }
                } catch (e: ClassNotFoundException) {
                    // In this case we want to let the base class take a crack
                    // at it.
                }
            }
            return super.onCreateView(name, attrs)
        }

        companion object {
            private val sClassPrefixList = arrayOf(
                "android.widget.",
                "android.webkit.",
                "android.app."
            )
        }
    }

}