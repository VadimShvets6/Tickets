package com.top1shvetsvadim1.jarvis.presentation.ui.main_tabs.home

import androidx.lifecycle.viewModelScope
import com.example.coreutills.wrappers.JobWrapper
import com.top1shvetsvadim1.jarvis.domain.models.HomeHubModel
import com.top1shvetsvadim1.jarvis.domain.usecase.home.GetHomeInfoScenario
import com.top1shvetsvadim1.jarvis.presentation.base.Reducer
import com.top1shvetsvadim1.jarvis.presentation.base.ViewModelBase
import com.top1shvetsvadim1.jarvis.presentation.ui.main_tabs.home.ui_items.ItemBaseMovies
import com.top1shvetsvadim1.jarvis.presentation.ui.main_tabs.home.ui_items.ItemMovieHorizontal
import com.top1shvetsvadim1.jarvis.presentation.ui.main_tabs.home.ui_items.ItemNowPlayingBase
import com.top1shvetsvadim1.jarvis.presentation.ui.main_tabs.home.ui_items.ItemNowPlayingPoster
import com.top1shvetsvadim1.jarvis.presentation.utils.extentions.addAsync
import com.top1shvetsvadim1.jarvis.presentation.utils.extentions.launchIO
import com.top1shvetsvadim1.jarvis.presentation.utils.recycler_utils.BaseUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getHomeInfoScenario: GetHomeInfoScenario
) : ViewModelBase<HomeState, HomeEvent, HomeIntent>() {
    override val reducer = HomeReducer()

    private val jobWrapper = JobWrapper()
    override fun handleIntent(intent: HomeIntent) {
        when (intent) {
            HomeIntent.LoadItems ->
                viewModelScope.launchIO {
                    try {
                        getHomeInfoScenario().distinctUntilChanged().collectLatest {
                            reduceToState {
                                copy(
                                    items = mapHomeItems(it).awaitAll()
                                )
                            }
                        }
                    } catch (ex: Exception) {
                        reducer.onError(ex, GetHomeInfoScenario::class)
                    }
                }
        }
    }

    private suspend fun mapHomeItems(model: HomeHubModel): List<Deferred<BaseUiModel>> {
        return mutableListOf<Deferred<BaseUiModel>>().apply {
            // Now Playing
            addAsync {
                ItemNowPlayingBase(
                    tag = "item_now_playing_base",
                    title = "Now Playing",
                    listImages = model.listNowPlaying.map {
                        ItemNowPlayingPoster(
                            tag = "poster_${it.posterImage}",
                            poster = it.posterImage,
                            titleMovie = it.movieName,
                            genres = it.genres.joinToString(", "),
                            voteAverage = it.voteRated
                        )
                    }
                )
            }

            //Popular
            addAsync {
                ItemBaseMovies(
                    tag = "item_popular_movies",
                    title = "Popular",
                    listMoviesPopular = model.listPopularMovies.map {
                        ItemMovieHorizontal(
                            tag = "movie_popular_${it.id}",
                            title = it.movieName,
                            voteAverage = it.voteRated,
                            posterImage = it.posterImage
                        )
                    }
                )
            }
        }
    }

    inner class HomeReducer : Reducer<HomeState, HomeEvent>(HomeState())
}