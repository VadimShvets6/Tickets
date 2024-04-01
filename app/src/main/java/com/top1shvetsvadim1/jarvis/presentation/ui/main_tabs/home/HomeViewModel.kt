package com.top1shvetsvadim1.jarvis.presentation.ui.main_tabs.home

import androidx.lifecycle.viewModelScope
import com.top1shvetsvadim1.jarvis.R
import com.top1shvetsvadim1.jarvis.common.Text
import com.top1shvetsvadim1.jarvis.domain.models.HomeHubModel
import com.top1shvetsvadim1.jarvis.domain.usecase.home.GetHomeInfoScenario
import com.top1shvetsvadim1.jarvis.presentation.base.Reducer
import com.top1shvetsvadim1.jarvis.presentation.base.ViewModelBase
import com.top1shvetsvadim1.jarvis.presentation.ui.main_tabs.home.ui_items.ItemBaseMovies
import com.top1shvetsvadim1.jarvis.presentation.ui.main_tabs.home.ui_items.ItemGenresBase
import com.top1shvetsvadim1.jarvis.presentation.ui.main_tabs.home.ui_items.ItemMovieHorizontal
import com.top1shvetsvadim1.jarvis.presentation.ui.main_tabs.home.ui_items.ItemNowPlayingBase
import com.top1shvetsvadim1.jarvis.presentation.ui.main_tabs.home.ui_items.ItemNowPlayingPoster
import com.top1shvetsvadim1.jarvis.presentation.ui.main_tabs.home.ui_items.ItemPeople
import com.top1shvetsvadim1.jarvis.presentation.ui.main_tabs.home.ui_items.ItemTilesWithText
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

    override fun handleIntent(intent: HomeIntent) {
        when (intent) {
            HomeIntent.LoadItems -> {
                viewModelScope.launchIO {
                    try {
                        reducer.onLoading(GetHomeInfoScenario::class)
                        getHomeInfoScenario().distinctUntilChanged().collectLatest {
                            reduceToState {
                                copy(
                                    items = mapHomeItems(it).awaitAll(),
                                    isLoading = false
                                )
                            }
                        }
                    } catch (ex: Exception) {
                        reducer.onError(ex, GetHomeInfoScenario::class)
                    } finally {
                        reducer.onFinish(GetHomeInfoScenario::class)
                    }
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
                    title = Text.Resource(R.string.key_now_playing),
                    listImages = model.movies.listNowPlaying.map {
                        ItemNowPlayingPoster(
                            id = it.id,
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
                    title = Text.Resource(R.string.key_popular),
                    listItems = model.movies.listPopularMovies.map {
                        ItemMovieHorizontal(
                            tag = "movie_popular_${it.id}",
                            id = it.id,
                            title = it.movieName,
                            voteAverage = it.voteRated,
                            posterImage = it.posterImage
                        )
                    }
                )
            }

            //TopRated
            addAsync {
                ItemBaseMovies(
                    tag = "item_top_rated_movies",
                    title = Text.Resource(R.string.key_top_rated),
                    listItems = model.movies.listTopRated.map {
                        ItemMovieHorizontal(
                            tag = "movies_top_rated_${it.id}",
                            id = it.id,
                            title = it.movieName,
                            voteAverage = it.voteRated,
                            posterImage = it.posterImage
                        )
                    }
                )
            }

            addAsync {
                ItemGenresBase(
                    tag = "item_genres",
                    title = Text.Resource(R.string.key_genres),
                    listItems = model.genres.listGenres.map {
                        ItemTilesWithText(tag = "genres_$${it.id}", title = it.title)
                    },
                )
            }

            //Trending
            addAsync {
                ItemBaseMovies(
                    tag = "item_movies_trending",
                    title = Text.Resource(R.string.key_dayli_trending),
                    listItems = model.movies.listTrendMovies.map {
                        ItemMovieHorizontal(
                            tag = "movie_trend_${it.id}",
                            id = it.id,
                            title = it.movieName,
                            voteAverage = it.voteRated,
                            posterImage = it.posterImage,
                        )
                    },
                )
            }

            //Upcoming
            addAsync {
                ItemBaseMovies(
                    tag = "item_movies_upcoming",
                    title = Text.Resource(R.string.key_upcoming),
                    listItems = model.movies.listUpcoming.map {
                        ItemMovieHorizontal(
                            tag = "movie_upcoming_${it.id}",
                            id = it.id,
                            title = it.movieName,
                            voteAverage = it.voteRated,
                            posterImage = it.posterImage,
                            releaseData = it.releaseDate,
                            isUpcoming = true,
                        )
                    }
                )
            }

            //People trend
            addAsync {
                ItemBaseMovies(
                    tag = "item_people_trend",
                    title = Text.Simple("Рейтинг актёров"),
                    listItems = model.people.listTrendPeople.map {
                        ItemPeople(
                            tag = "people_trend_${it.id}",
                            name = it.name,
                            position = model.people.listTrendPeople.indexOf(it),
                            image = it.profilePath
                        )
                    }
                )
            }
        }
    }

    inner class HomeReducer : Reducer<HomeState, HomeEvent>(HomeState())
}