package com.example.coreutills.rendering

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Canvas
import android.graphics.ColorFilter
import android.graphics.PorterDuff
import android.graphics.Rect
import android.graphics.drawable.Drawable
import androidx.annotation.DrawableRes
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.graphics.drawable.DrawableCompat
import com.example.coreutills.utils.toArithmetic

class TilingDrawable(drawable: Drawable) : DrawableWrapper(drawable) {
    private var callbackEnabled = true

    constructor(@DrawableRes resource: Int, context: Context) : this(
        AppCompatResources.getDrawable(
            context,
            resource
        )!!
    )

    override fun draw(canvas: Canvas) {
        callbackEnabled = false
        val bounds = bounds
        val width = (wrappedDrawable.intrinsicWidth.toArithmetic() / 2.4).get<Int>()
        val height = (wrappedDrawable.intrinsicHeight.toArithmetic() / 2.4).get<Int>()
        var x = bounds.left
        while (x < bounds.right + width - 1) {
            var y = bounds.top
            while (y < bounds.bottom + height) {
                wrappedDrawable.setBounds(x, y, x + width, y + height)
                try {
                    wrappedDrawable.draw(canvas)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                y += height
            }
            x += width
        }
        callbackEnabled = true
    }

    override fun onBoundsChange(bounds: Rect) {}

    override fun invalidateDrawable(who: Drawable) {
        if (callbackEnabled) {
            super.invalidateDrawable(who)
        }
    }

    override fun scheduleDrawable(who: Drawable, what: Runnable, `when`: Long) {
        if (callbackEnabled) {
            super.scheduleDrawable(who, what, `when`)
        }
    }

    override fun unscheduleDrawable(who: Drawable, what: Runnable) {
        if (callbackEnabled) {
            super.unscheduleDrawable(who, what)
        }
    }

    override fun getIntrinsicWidth(): Int {
        return -1
    }

    override fun getIntrinsicHeight(): Int {
        return -1
    }
}

open class DrawableWrapper(drawable: Drawable) : Drawable(), Drawable.Callback {
    var wrappedDrawable: Drawable = drawable
        set(drawable) {
            field.callback = null
            field = drawable
            drawable.callback = this
        }

    override fun draw(canvas: Canvas) = wrappedDrawable.draw(canvas)

    override fun onBoundsChange(bounds: Rect) {
        wrappedDrawable.bounds = bounds
    }

    override fun setChangingConfigurations(configs: Int) {
        wrappedDrawable.changingConfigurations = configs
    }

    override fun getChangingConfigurations() = wrappedDrawable.changingConfigurations

    override fun setFilterBitmap(filter: Boolean) {
        wrappedDrawable.isFilterBitmap = filter
    }

    override fun setAlpha(alpha: Int) {
        //wrappedDrawable.alpha = alpha
    }

    override fun setColorFilter(cf: ColorFilter?) {
        wrappedDrawable.colorFilter = cf
    }

    override fun invalidateSelf() {
        super.invalidateSelf()
        wrappedDrawable.invalidateSelf()
    }

    override fun isStateful() = wrappedDrawable.isStateful

    override fun setState(stateSet: IntArray) = wrappedDrawable.setState(stateSet)

    override fun getState() = wrappedDrawable.state

    override fun jumpToCurrentState() = wrappedDrawable.jumpToCurrentState()

    override fun getCurrent() = wrappedDrawable.current

    override fun setVisible(visible: Boolean, restart: Boolean) =
        super.setVisible(visible, restart) || wrappedDrawable.setVisible(visible, restart)

    override fun getAlpha(): Int {
        return wrappedDrawable.alpha
    }

    override fun getTransparentRegion() = wrappedDrawable.transparentRegion

    override fun getIntrinsicWidth() = wrappedDrawable.intrinsicWidth

    override fun getIntrinsicHeight() = wrappedDrawable.intrinsicHeight

    override fun getMinimumWidth() = wrappedDrawable.minimumWidth

    override fun getMinimumHeight() = wrappedDrawable.minimumHeight

    override fun getPadding(padding: Rect) = wrappedDrawable.getPadding(padding)

    override fun invalidateDrawable(who: Drawable) = invalidateSelf()

    override fun scheduleDrawable(who: Drawable, what: Runnable, `when`: Long) = scheduleSelf(what, `when`)

    override fun unscheduleDrawable(who: Drawable, what: Runnable) = unscheduleSelf(what)

    override fun onLevelChange(level: Int) = wrappedDrawable.setLevel(level)

    override fun setAutoMirrored(mirrored: Boolean) = DrawableCompat.setAutoMirrored(wrappedDrawable, mirrored)

    override fun isAutoMirrored() = DrawableCompat.isAutoMirrored(wrappedDrawable)

    @Deprecated("Deprecated in Java", ReplaceWith("wrappedDrawable.opacity"))
    @Suppress("DEPRECATION")
    override fun getOpacity(): Int {
        return wrappedDrawable.opacity
    }

    override fun setTint(tint: Int) = DrawableCompat.setTint(wrappedDrawable, tint)

    override fun setTintList(tint: ColorStateList?) = DrawableCompat.setTintList(wrappedDrawable, tint)

    override fun setTintMode(tintMode: PorterDuff.Mode?) {
        tintMode?.let { DrawableCompat.setTintMode(wrappedDrawable, it) }
    }

    override fun setHotspot(x: Float, y: Float) = DrawableCompat.setHotspot(wrappedDrawable, x, y)

    override fun setHotspotBounds(left: Int, top: Int, right: Int, bottom: Int) =
        DrawableCompat.setHotspotBounds(wrappedDrawable, left, top, right, bottom)
}