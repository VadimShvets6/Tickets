package com.top1shvetsvadim1.jarvis.presentation.activity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.top1shvetsvadim1.jarvis.domain.usecase.genres.FetchMoviesGenresUseCase
import com.top1shvetsvadim1.jarvis.domain.usecase.now_playing.FetchFirstListNowPlayingUseCase
import com.top1shvetsvadim1.jarvis.domain.usecase.popular.FetchFirstListMoviesPopularUseCase
import com.top1shvetsvadim1.jarvis.domain.usecase.top_rated.FetchFirstListMoviesTopRatedUseCase
import com.top1shvetsvadim1.jarvis.domain.usecase.trending.FetchTrendMoviesUseCase
import com.top1shvetsvadim1.jarvis.domain.usecase.trending.FetchTrendPeopleUseCase
import com.top1shvetsvadim1.jarvis.domain.usecase.upcoming.FetchFirstListMoviesUpcomingUseCase
import com.top1shvetsvadim1.jarvis.presentation.utils.extentions.launchSafeInIO
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val fetchFirstListNowPlayingUseCase: FetchFirstListNowPlayingUseCase,
    private val fetchMoviesGenresUseCase: FetchMoviesGenresUseCase,
    private val fetchFirstListMoviesPopularUseCase: FetchFirstListMoviesPopularUseCase,
    private val fetchFirstListMoviesTopRatedUseCase: FetchFirstListMoviesTopRatedUseCase,
    private val fetchFirstListMoviesUpcomingUseCase: FetchFirstListMoviesUpcomingUseCase,
    private val fetchTrendMoviesUseCase: FetchTrendMoviesUseCase,
    private val fetchTrendPeopleUseCase: FetchTrendPeopleUseCase
) : ViewModel() {

    var isReady: Boolean = false

    fun fetchMainData() {
        viewModelScope.launchSafeInIO {
            fetchFirstListNowPlayingUseCase()
            fetchMoviesGenresUseCase()
            fetchFirstListMoviesPopularUseCase()
            fetchFirstListMoviesTopRatedUseCase()
            fetchFirstListMoviesUpcomingUseCase()
            fetchTrendMoviesUseCase()
            fetchTrendPeopleUseCase()
            isReady = true
        }
    }

}