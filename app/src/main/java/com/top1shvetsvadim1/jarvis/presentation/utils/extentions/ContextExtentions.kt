package com.top1shvetsvadim1.jarvis.presentation.utils.extentions

import android.content.Context
import android.content.res.Configuration
import android.util.TypedValue
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.core.content.ContextCompat
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
fun Context.getColorCompat(@ColorRes colorRes: Int): Int {
    return ContextCompat.getColor(this, colorRes)
}

fun Context.isDarkMode(): Boolean {
    val currentNightMode =
        this.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK

    return currentNightMode == Configuration.UI_MODE_NIGHT_YES
}