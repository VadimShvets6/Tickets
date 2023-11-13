package com.top1shvetsvadim1.jarvis.presentation.utils.special

import android.content.Context
import android.os.Build
import androidx.core.performance.DevicePerformance

interface PerformanceManager {
    /**
     * Returns HIGH if the performance class of the device fits the SDK,
     * or return LOW if the performance class does not fit or is too low
     */
    val devicePerformanceClass: PerformanceClass

    /**
     * Returns true if the device's performance class matches the current SDK, false otherwise
     */
    fun isHighPerformance(): Boolean

    /**
     * Returns true if the device's performance class does not match the current SDK, otherwise true
     */
    fun isLowPerformance(): Boolean

    companion object {
        /**
         * Create PerformanceClass from the context.
         *
         * @param context
         */
        fun create(context: Context): PerformanceManager = PerformanceManagerImpl(context)
    }

    /**
     * Enum class appropriate for device performance
     */
    enum class PerformanceClass {
        HIGH, LOW
    }
}

private class PerformanceManagerImpl(context: Context) : PerformanceManager {

    private val currentDevicePerformance = DevicePerformance.create(context)

    override val devicePerformanceClass: PerformanceManager.PerformanceClass = getCurrentDevicePerformance()

    private fun getCurrentDevicePerformance(): PerformanceManager.PerformanceClass {
        return if (currentDevicePerformance.mediaPerformanceClass >= Build.VERSION_CODES.S) {
            PerformanceManager.PerformanceClass.HIGH
        } else {
            PerformanceManager.PerformanceClass.LOW
        }
    }

    override fun isHighPerformance(): Boolean {
        return currentDevicePerformance.mediaPerformanceClass >= Build.VERSION_CODES.S
    }

    override fun isLowPerformance(): Boolean {
        return currentDevicePerformance.mediaPerformanceClass < Build.VERSION_CODES.S
    }
}