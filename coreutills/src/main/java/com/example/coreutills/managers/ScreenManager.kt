package com.example.coreutills.managers

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import android.os.Build
import android.util.DisplayMetrics
import android.view.View
import android.view.WindowManager
import android.view.WindowMetrics
import androidx.core.graphics.Insets
import androidx.core.view.WindowCompat
import com.example.coreutills.extentions.screenHeight
import kotlin.math.abs
import kotlin.math.max

object ScreenManager {

    private val insetHandler = mutableMapOf<String, Int>()
    val statusBarPadding get() = insetHandler["top"] ?: 0
    val navigationBarPadding get() = insetHandler["bottom"] ?: 0
    fun consumeInset(insets: Insets) {
        insetHandler["bottom"] = insets.bottom
        insetHandler["top"] = insets.top
    }

    fun setKeyboard(keyboardHeight: Int) {
        insetHandler["keyboard"] = max(insetHandler["keyboard"] ?: 0, keyboardHeight)
    }

    fun setStatusBarContrast(activity: Activity, isLight: Boolean) {
        activity.apply {
            WindowCompat.getInsetsController(
                window,
                window.decorView
            ).isAppearanceLightStatusBars = isLight
        }
    }

    fun restoreStatusBar(activity: Activity) {
        val isThemeLight =
            activity.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_NO
        setStatusBarContrast(activity, isThemeLight)
    }

    @SuppressLint("ObsoleteSdkInt")
    fun calculateRealHeight(context: Context): Int {
        return when {
            Build.VERSION.SDK_INT < Build.VERSION_CODES.M -> context.screenHeight
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.R -> {
                val metrics: WindowMetrics =
                    context.getSystemService(WindowManager::class.java)?.currentWindowMetrics ?: return 0
                metrics.bounds.height()
            }

            else -> {
                DisplayMetrics().apply {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        context.getSystemService(WindowManager::class.java).defaultDisplay.getRealMetrics(
                            this
                        )
                    }
                }.heightPixels
            }
        }
    }

    @SuppressLint("ObsoleteSdkInt")
    fun calculateRealWidth(context: Context): Int {
        return when {
            Build.VERSION.SDK_INT < Build.VERSION_CODES.M -> context.screenHeight
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.R -> {
                val metrics: WindowMetrics =
                    context.getSystemService(WindowManager::class.java)?.currentWindowMetrics ?: return 0
                metrics.bounds.width()
            }

            else -> {
                DisplayMetrics().apply {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        context.getSystemService(WindowManager::class.java).defaultDisplay.getRealMetrics(
                            this
                        )
                    }
                }.widthPixels
            }
        }
    }

    fun isThemeLight(activity: Activity): Boolean {
        return activity.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_NO
    }

    fun calculateStartPositionView(context: Context, focus: View): Int {
        val screen = IntArray(2)
        focus.getLocationOnScreen(screen)
        return calculateRealHeight(context) - screen[1] - focus.height
    }
    fun requestFoldCompatibleScreen(context: Context): Context {
        return context
    }

}