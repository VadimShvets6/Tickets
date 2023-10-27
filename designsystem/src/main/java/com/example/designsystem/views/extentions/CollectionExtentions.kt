package com.example.designsystem.views.extentions

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet

fun getTypedArray(
    context: Context,
    attributeSet: AttributeSet,
    attr: IntArray
): TypedArray {
    return context.obtainStyledAttributes(attributeSet, attr, 0, 0)
}