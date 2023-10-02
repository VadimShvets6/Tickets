package com.top1shvetsvadim1.jarvis.presentation.ui.main_tabs.search

import com.top1shvetsvadim1.jarvis.presentation.base.Event
import com.top1shvetsvadim1.jarvis.presentation.base.IntentMVI
import com.top1shvetsvadim1.jarvis.presentation.base.State
data class SearchState(
    val stub: Unit = Unit
) : State

sealed interface SearchEvent : Event {}

sealed interface SearchIntent : IntentMVI {
    object LoadItems: SearchIntent
}