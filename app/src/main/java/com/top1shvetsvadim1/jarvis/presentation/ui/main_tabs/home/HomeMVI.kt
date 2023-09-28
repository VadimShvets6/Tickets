package com.top1shvetsvadim1.jarvis.presentation.ui.main_tabs.home

import com.top1shvetsvadim1.jarvis.presentation.base.Event
import com.top1shvetsvadim1.jarvis.presentation.base.IntentMVI
import com.top1shvetsvadim1.jarvis.presentation.base.State

data class HomeState(
    val stub: Unit = Unit
) : State

sealed interface HomeEvent : Event {}

sealed interface HomeIntent : IntentMVI {}