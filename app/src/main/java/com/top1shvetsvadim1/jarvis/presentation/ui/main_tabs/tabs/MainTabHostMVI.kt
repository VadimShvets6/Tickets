package com.top1shvetsvadim1.jarvis.presentation.ui.main_tabs.tabs

import com.top1shvetsvadim1.jarvis.presentation.base.Event
import com.top1shvetsvadim1.jarvis.presentation.base.IntentMVI
import com.top1shvetsvadim1.jarvis.presentation.base.State
import com.top1shvetsvadim1.jarvis.presentation.utils.recycler_utils.BaseUiModel

data class MainTabHostState(
    val stub: Unit = Unit
) : State

sealed interface MainTabHostEvent : Event {}

sealed interface MainTabHostIntent : IntentMVI {}