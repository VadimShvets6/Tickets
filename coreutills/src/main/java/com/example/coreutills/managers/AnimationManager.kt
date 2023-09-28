package com.example.coreutills.managers

import android.view.animation.AccelerateInterpolator
import android.view.animation.AlphaAnimation
import android.view.animation.LinearInterpolator

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
}