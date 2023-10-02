package com.top1shvetsvadim1.jarvis.presentation.ui.main_tabs.home

import com.top1shvetsvadim1.jarvis.domain.usecase.now_playing.GetMoviesNowPlayingUseCase
import com.top1shvetsvadim1.jarvis.presentation.base.Reducer
import com.top1shvetsvadim1.jarvis.presentation.base.ViewModelBase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getMoviesNowPlayingUseCase: GetMoviesNowPlayingUseCase
) : ViewModelBase<HomeState, HomeEvent, HomeIntent>() {
    override val reducer = HomeReducer()

    override fun handleIntent(intent: HomeIntent) {
        when(intent){
            HomeIntent.LoadItems -> getMoviesNowPlayingUseCase.launch()
        }
    }

    inner class HomeReducer : Reducer<HomeState, HomeEvent>(HomeState())
}