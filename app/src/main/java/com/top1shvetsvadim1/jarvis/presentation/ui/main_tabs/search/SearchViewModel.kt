package com.top1shvetsvadim1.jarvis.presentation.ui.main_tabs.search

import com.top1shvetsvadim1.jarvis.presentation.base.Reducer
import com.top1shvetsvadim1.jarvis.presentation.base.ViewModelBase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(

): ViewModelBase<SearchState, SearchEvent, SearchIntent>() {
    override val reducer = SearchReducer()

    override fun handleIntent(intent: SearchIntent) {

    }

    inner class SearchReducer: Reducer<SearchState, SearchEvent>(SearchState())
}