package com.top1shvetsvadim1.jarvis.presentation.utils.extentions

import android.util.TypedValue
import com.top1shvetsvadim1.jarvis.presentation.controler.ContextManager.retrieveApplicationContext

val Int.dp
    get() = retrieveApplicationContext().let {
        TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            this.toFloat(),
            it.resources.displayMetrics
        )
    }