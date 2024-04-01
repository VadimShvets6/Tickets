package com.top1shvetsvadim1.jarvis.presentation.ui.main_tabs.tickets

import com.top1shvetsvadim1.jarvis.presentation.base.Event
import com.top1shvetsvadim1.jarvis.presentation.base.IntentMVI
import com.top1shvetsvadim1.jarvis.presentation.base.State

data class TicketsState(
    val stub: Unit = Unit
) : State

sealed interface TicketsEvent : Event {}

sealed interface TicketsIntent : IntentMVI {
    object LoadItems: TicketsIntent
}