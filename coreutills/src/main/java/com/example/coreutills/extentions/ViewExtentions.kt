package com.example.coreutills.extentions

import android.view.View
import androidx.appcompat.widget.SwitchCompat

interface ViewCustomFocusableParent {
    val externalFocusParent: View
}

val View.focusParent: View
    get() = run {
        val customFocusable = this.isViewCustomFocusableParent(0)
        customFocusable?.externalFocusParent ?: parent as View
    }

private fun View.isViewCustomFocusableParent(iterations: Int): ViewCustomFocusableParent? {
    return if (this is ViewCustomFocusableParent) {
        this
    } else if (iterations == 4) {
        null
    } else {
        (parent as? View?)?.isViewCustomFocusableParent(iterations + 1)
    }
}

inline fun SwitchCompat.onSwitch(
    crossinline onSwitch: (Boolean) -> Unit
) {
    setOnCheckedChangeListener(null)
    setOnClickListener {
        onSwitch(isChecked)
    }
}