package com.top1shvetsvadim1.jarvis.presentation.ui.main_tabs.home

import com.top1shvetsvadim1.jarvis.presentation.base.Reducer
import com.top1shvetsvadim1.jarvis.presentation.base.ViewModelBase

class HomeViewModel : ViewModelBase<HomeState,HomeEvent, HomeIntent>() {
    override val reducer = HomeReducer()

    override fun handleIntent(intent: HomeIntent) {
    }

    inner class HomeReducer: Reducer<HomeState, HomeEvent>(HomeState())
}