package com.top1shvetsvadim1.jarvis.presentation.utils

import com.top1shvetsvadim1.jarvis.presentation.utils.storage.PropertiesStorage

object CurrentUser {

    val userLanguage get() = PropertiesStorage.getString(PropertiesStorage.Properties.UserLanguage)
}