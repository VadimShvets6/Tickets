package com.top1shvetsvadim1.jarvis.presentation.activity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.top1shvetsvadim1.jarvis.domain.usecase.genres.FetchMoviesGenresUseCase
import com.top1shvetsvadim1.jarvis.domain.usecase.now_playing.FetchFirstListNowPlayingUseCase
import com.top1shvetsvadim1.jarvis.domain.usecase.popular.FetchFirstListMoviesPopularUseCase
import com.top1shvetsvadim1.jarvis.presentation.utils.extentions.launchSafeInIO
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val fetchFirstListNowPlayingUseCase: FetchFirstListNowPlayingUseCase,
    private val fetchMoviesGenresUseCase: FetchMoviesGenresUseCase,
    private val fetchFirstListMoviesPopularUseCase: FetchFirstListMoviesPopularUseCase
) : ViewModel() {

    var isReady: Boolean = false

    fun fetchMainData() {
        viewModelScope.launchSafeInIO {
            fetchFirstListNowPlayingUseCase()
            fetchMoviesGenresUseCase()
            fetchFirstListMoviesPopularUseCase()
            isReady = true
        }
    }

}