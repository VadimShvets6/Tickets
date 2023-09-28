package com.example.coreutills.wrappers

import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job

class JobWrapper {

    private var mainJob: Job = Job()

    private var locked: Boolean = false

    infix fun reassign(job: Job) {
        mainJob.cancel()
        mainJob = job
    }

    fun lockPlace(coroutineScope: CoroutineScope, job: CoroutineScope.() -> Job) {
        if (!locked) {
            mainJob.cancel()
            mainJob = job(coroutineScope)
            locked = true
        }
    }

    fun cancel() {
        Log.d("JobWrapper", "cancel")
        mainJob.cancel()
    }

    fun unlock() {
        locked = true
    }

    companion object {
        fun lazyWrapperInit() = lazy {
            JobWrapper()
        }
    }
}