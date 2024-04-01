package com.example.coreutills.managers

import android.animation.Animator
import android.animation.ObjectAnimator
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.view.animation.AlphaAnimation
import android.view.animation.LinearInterpolator
import androidx.core.animation.doOnEnd
import androidx.core.view.isVisible
import androidx.core.view.updateLayoutParams

object AnimationsManager {

    const val MediumExpandedAnimationDuration = 450L
    const val MediumAnimationDuration = 325L
    const val ShortAnimationDuration = 200L
    const val ShortestAnimationDuration = 100L
    const val pageAnimationDuration = 150L

    fun getBasicFadeIn(duration: Long = 1000) = AlphaAnimation(0f, 1f).apply {
        interpolator = LinearInterpolator()
        this.duration = duration
    }

    fun getBasicFadeOut(duration: Long = 1000) = AlphaAnimation(1f, 0f).apply {
        interpolator = AccelerateInterpolator()
        startOffset = duration / 4
        this.duration = duration
    }

    fun visibilityComplexAnimationLinear(view: View, isVisible: Boolean) {
        if (!view.isVisible && isVisible) {
            view.updateLayoutParams {
                height = 0
            }
            view.isVisible = true
            view.measure(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            visibilityAnimators(view, 0, view.measuredHeight) {

            }
        } else if (view.isVisible && !isVisible) {
            visibilityAnimators(view, view.height, 0) {
                view.isVisible = false
            }
        }
    }

    private fun visibilityAnimators(view: View, initial: Int, final: Int, onEnd: (Animator) -> Unit) {
        ObjectAnimator.ofInt(initial, final).apply {
            addUpdateListener {
                val value = it.animatedValue as Int
                view.updateLayoutParams<ViewGroup.LayoutParams> {
                    height = value
                }
            }
            doOnEnd(onEnd)
            duration = 400
            interpolator = AccelerateInterpolator()
            start()
        }
        val (start, end) = if (initial < final) 0f to 1f else 1f to 0f
        ObjectAnimator.ofFloat(start, end).apply {
            addUpdateListener {
                val value = it.animatedValue as Float
                view.alpha = value
                view.scaleY = value
                view.scaleX = value
            }
            duration = 400
            interpolator = AccelerateInterpolator()
            start()
        }
    }
}