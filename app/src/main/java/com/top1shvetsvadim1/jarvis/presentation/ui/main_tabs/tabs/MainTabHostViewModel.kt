package com.top1shvetsvadim1.jarvis.presentation.ui.main_tabs.tabs

import com.top1shvetsvadim1.jarvis.presentation.base.Reducer
import com.top1shvetsvadim1.jarvis.presentation.base.ViewModelBase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainTabHostViewModel @Inject constructor(

) : ViewModelBase<MainTabHostState, MainTabHostEvent, MainTabHostIntent>() {

    override val reducer = MainTabHostReducer()
    override fun handleIntent(intent: MainTabHostIntent) {

    }

    inner class MainTabHostReducer : Reducer<MainTabHostState, MainTabHostEvent>(MainTabHostState())
}