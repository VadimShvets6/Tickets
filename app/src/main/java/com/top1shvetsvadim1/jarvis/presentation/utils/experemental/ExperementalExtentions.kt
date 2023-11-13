package com.top1shvetsvadim1.jarvis.presentation.utils.experemental

import com.top1shvetsvadim1.jarvis.presentation.controler.ContextManager

fun Int.ssp(): Int {
    val context = ContextManager.retrieveApplicationContext()
    return context.resources.getDimensionPixelSize(
        context.resources.getIdentifier("_${this}ssp", "dimen", context.packageName)
    )
}

val Int.ssp get() = this.ssp()
