package com.top1shvetsvadim1.jarvis.presentation.ui.main_tabs.home

import com.top1shvetsvadim1.jarvis.presentation.base.Event
import com.top1shvetsvadim1.jarvis.presentation.base.IntentMVI
import com.top1shvetsvadim1.jarvis.presentation.base.State
import com.top1shvetsvadim1.jarvis.presentation.utils.recycler_utils.BaseUiModel

data class HomeState(
    val items: List<BaseUiModel> = emptyList(),
    val isLoading: Boolean = true
) : State

sealed interface HomeEvent : Event {}

sealed interface HomeIntent : IntentMVI {
    object LoadItems: HomeIntent
}