package com.top1shvetsvadim1.jarvis.presentation.ui.preview

import com.top1shvetsvadim1.jarvis.presentation.base.Event
import com.top1shvetsvadim1.jarvis.presentation.base.IntentMVI
import com.top1shvetsvadim1.jarvis.presentation.base.State
import com.top1shvetsvadim1.jarvis.presentation.utils.recycler_utils.BaseUiModel

data class WelcomeState(
    val items: List<BaseUiModel> = emptyList()
) : State

sealed interface WelcomeEvent : Event {

}

sealed interface WelcomeIntent : IntentMVI {

    object LoadItems : WelcomeIntent
}