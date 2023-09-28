package com.example.coreutills.extentions

import android.content.Context
import android.util.TypedValue

val Context.screenHeight
    get() = resources.displayMetrics.heightPixels
