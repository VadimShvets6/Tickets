package com.top1shvetsvadim1.jarvis.presentation.controler

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.content.Context
import androidx.multidex.MultiDexApplication
import com.top1shvetsvadim1.jarvis.presentation.activity.MainActivity
import java.lang.ref.WeakReference
/*
open class ApplicationController : MultiDexApplication() {

    var mainActivityWeakRef: WeakReference<MainActivity> = WeakReference(null)
    var currentActivity: Activity? = null

    companion object {
        lateinit var instance: ApplicationController
        lateinit var context: Context
        var currentActivitySafe: WeakReference<Activity>? = null
        val applicationContext get() = context
    }
    override fun onCreate() {
        instance = this
        context = this.applicationContext
        context = this.applicationContext
        super.onCreate()
    }
}*/

@SuppressLint("StaticFieldLeak")
object ContextManager {

    private var injectedContext: Context? = null

    private var activity: WeakReference<Activity> = WeakReference(null)

    fun retrieveApplicationContext() = injectedContext ?: throw Exception("Application is dead")

    fun retrieveWindow() = activity.get()?.window

    fun retrieveActivityContext() = activity

    fun injectApplicationContext(application: Application) {
        injectedContext = application.applicationContext
    }
}