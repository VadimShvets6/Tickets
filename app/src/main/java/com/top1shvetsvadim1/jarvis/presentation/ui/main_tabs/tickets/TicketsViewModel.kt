package com.top1shvetsvadim1.jarvis.presentation.ui.main_tabs.tickets

import com.top1shvetsvadim1.jarvis.presentation.base.Reducer
import com.top1shvetsvadim1.jarvis.presentation.base.ViewModelBase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TicketsViewModel @Inject constructor(

) : ViewModelBase<TicketsState, TicketsEvent, TicketsIntent>() {
    override val reducer = TicketsReducer()

    override fun handleIntent(intent: TicketsIntent) {

    }

    inner class TicketsReducer: Reducer<TicketsState, TicketsEvent>(TicketsState())
}