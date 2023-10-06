package com.top1shvetsvadim1.jarvis.presentation.utils.extentions

import com.top1shvetsvadim1.jarvis.presentation.utils.ApiValues

fun String.toImageUrl(): String {
    return ApiValues.BASE_ORIGINAL_IMAGE + this
}