package com.top1shvetsvadim1.jarvis.presentation.utils.special

import com.top1shvetsvadim1.jarvis.presentation.utils.extentions.launchIO
import com.top1shvetsvadim1.jarvis.presentation.utils.extentions.switchToUI
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.ensureActive

class ConditionalSuspendBlock(
    val isOneTime: Boolean = false,
    val coroutineScope: CoroutineScope? = null
) {

    private val scope = coroutineScope ?: CoroutineScope(Dispatchers.IO)
    private var lock = false

    fun action(condition: () -> Boolean, action: () -> Unit) {
        if (!lock) {
            scope.launchIO {
                if (isOneTime) {
                    lock = true
                }
                while (!condition()) {
                    delay(25)
                    ensureActive()
                }
                switchToUI {
                    action()
                }
            }
        }
    }

    fun forceUnlock() {
        lock = false
    }
}