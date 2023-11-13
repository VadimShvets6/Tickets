package com.top1shvetsvadim1.jarvis.presentation.ui.main_tabs.details

import com.top1shvetsvadim1.jarvis.domain.models.MoviesDetailsModel
import com.top1shvetsvadim1.jarvis.presentation.base.Event
import com.top1shvetsvadim1.jarvis.presentation.base.IntentMVI
import com.top1shvetsvadim1.jarvis.presentation.base.State
import com.top1shvetsvadim1.jarvis.presentation.utils.recycler_utils.BaseUiModel

data class MovieDetailsState(
    val items: List<BaseUiModel> = emptyList(),
    val movieDetails: MoviesDetailsModel? = null,
    val isLoading: Boolean = false
) : State

sealed interface MovieDetailsEvent : Event {}

sealed interface MovieDetailsIntent : IntentMVI {
    data class Load(val id: Int) : MovieDetailsIntent
}