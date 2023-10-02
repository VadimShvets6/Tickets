package com.top1shvetsvadim1.jarvis.presentation.ui.main_tabs.profile

import com.top1shvetsvadim1.jarvis.presentation.base.Event
import com.top1shvetsvadim1.jarvis.presentation.base.IntentMVI
import com.top1shvetsvadim1.jarvis.presentation.base.State

data class MyProfileState(
    val stub: Unit = Unit
) : State

sealed interface MyProfileEvent : Event {}

sealed interface MyProfileIntent : IntentMVI {
    object LoadItems : MyProfileIntent
}