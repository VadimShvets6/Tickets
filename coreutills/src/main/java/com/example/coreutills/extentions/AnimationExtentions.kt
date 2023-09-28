package com.example.coreutills.extentions

import android.animation.ValueAnimator
import android.view.View
import androidx.core.view.isVisible
import androidx.core.view.updateLayoutParams
import com.example.coreutills.managers.AnimationsManager

fun View.animateHeight(
    newHeight: Int,
) {
    val anim: ValueAnimator = ValueAnimator.ofInt(this.measuredHeight, newHeight)
    anim.addUpdateListener { valueAnimator ->
        this.updateLayoutParams {
            this.height = valueAnimator.animatedValue as Int
        }
    }
    anim.duration = 200
    anim.start()
}

fun View.animateIsVisible(
    visible: Boolean,
    duration: Long = AnimationsManager.ShortAnimationDuration,
    invisibleState: Int = View.GONE
) {
    when (visible) {
        true -> if (!isVisible) {
            isVisible = true
            startAnimation(
                AnimationsManager.getBasicFadeIn(duration = duration)
            )
        }
        false -> if (isVisible) {
            visibility = invisibleState
            startAnimation(
                AnimationsManager.getBasicFadeOut(duration = duration)
            )
        }
    }
}