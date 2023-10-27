package com.top1shvetsvadim1.jarvis.presentation.ui.main_tabs.details

import com.top1shvetsvadim1.jarvis.presentation.base.Reducer
import com.top1shvetsvadim1.jarvis.presentation.base.ViewModelBase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MovieDetailsViewModel @Inject constructor(

) : ViewModelBase<MovieDetailsState, MovieDetailsEvent, MovieDetailsIntent>() {

    override val reducer = MovieDetailsReduce()

    override fun handleIntent(intent: MovieDetailsIntent) {
    }

    inner class MovieDetailsReduce: Reducer<MovieDetailsState, MovieDetailsEvent>(MovieDetailsState())
}