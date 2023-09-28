package com.top1shvetsvadim1.jarvis.di

import android.app.Application
import com.top1shvetsvadim1.jarvis.presentation.controler.ContextManager
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App : Application() {
    override fun onCreate() {
        super.onCreate()
        ContextManager.injectApplicationContext(this)
    }
}