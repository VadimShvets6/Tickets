package com.top1shvetsvadim1.jarvis.presentation.utils.extentions

import android.content.Context
import android.util.TypedValue
import androidx.annotation.DimenRes
import com.top1shvetsvadim1.jarvis.presentation.controler.ContextManager.retrieveApplicationContext

val Int.dp
    get() = retrieveApplicationContext().let {
        TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            this.toFloat(),
            it.resources.displayMetrics
        )
    }

fun Context.intDimen(@DimenRes res: Int): Int {
    return resources.getDimensionPixelSize(res)
}