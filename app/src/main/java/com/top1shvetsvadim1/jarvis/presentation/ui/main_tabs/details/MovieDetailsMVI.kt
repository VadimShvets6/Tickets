package com.top1shvetsvadim1.jarvis.presentation.ui.main_tabs.details

import com.top1shvetsvadim1.jarvis.presentation.base.Event
import com.top1shvetsvadim1.jarvis.presentation.base.IntentMVI
import com.top1shvetsvadim1.jarvis.presentation.base.State

data class MovieDetailsState(
    val stub: Unit = Unit
) : State

sealed interface MovieDetailsEvent : Event {}

sealed interface MovieDetailsIntent : IntentMVI {
    object LoadItems : MovieDetailsIntent
}