package com.top1shvetsvadim1.jarvis.presentation.utils.extentions

import com.top1shvetsvadim1.jarvis.presentation.utils.ApiValues
import java.util.Locale

fun String.toImageUrl(): String {
    return ApiValues.BASE_ORIGINAL_IMAGE + this
}

fun String.toImageAvatarUrl(): String {
    return ApiValues.BASE_500_IMAGE + this
}


fun String.capitalizeFirst(): String {
    return this.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() }
}