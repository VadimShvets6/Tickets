package com.top1shvetsvadim1.jarvis.presentation.utils.experemental

import com.top1shvetsvadim1.jarvis.presentation.controler.ContextManager

fun Int.ssp(): Int {
    val context = ContextManager.retrieveApplicationContext()
    return context.resources.getDimensionPixelSize(
        context.resources.getIdentifier("_${this}ssp", "dimen", context.packageName)
    )
}

fun Int.sdp(): Int {
    val context = ContextManager.retrieveApplicationContext()
    return context.resources.getDimensionPixelSize(
        context.resources.getIdentifier("_${this}sdp", "dimen", context.packageName)
    )
}
val Int.sdp get() = this.sdp()
val Int.ssp get() = this.ssp()
