package com.top1shvetsvadim1.jarvis.presentation.ui.main_tabs.details

import androidx.lifecycle.viewModelScope
import com.example.coreutills.wrappers.JobWrapper
import com.top1shvetsvadim1.jarvis.R
import com.top1shvetsvadim1.jarvis.common.Text
import com.top1shvetsvadim1.jarvis.domain.models.MoviesDetailsModel
import com.top1shvetsvadim1.jarvis.domain.usecase.movies_details.FetchMovieDetailsScenario
import com.top1shvetsvadim1.jarvis.domain.usecase.movies_details.GetMoviesDetailsScenario
import com.top1shvetsvadim1.jarvis.presentation.base.Reducer
import com.top1shvetsvadim1.jarvis.presentation.base.ViewModelBase
import com.top1shvetsvadim1.jarvis.presentation.ui.main_tabs.details.ui_items.ItemCast
import com.top1shvetsvadim1.jarvis.presentation.ui.main_tabs.details.ui_items.ItemDescription
import com.top1shvetsvadim1.jarvis.presentation.ui.main_tabs.details.ui_items.ItemGeneralInformation
import com.top1shvetsvadim1.jarvis.presentation.ui.main_tabs.details.ui_items.ItemMovieCasts
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
class MovieDetailsViewModel @Inject constructor(
    private val fetchMovieDetailsScenario: FetchMovieDetailsScenario,
    private val getMoviesDetailsScenario: GetMoviesDetailsScenario
) : ViewModelBase<MovieDetailsState, MovieDetailsEvent, MovieDetailsIntent>() {

    override val reducer = MovieDetailsReduce()
    private val jobWrapper = JobWrapper()
    override fun handleIntent(intent: MovieDetailsIntent) {
        when (intent) {
            is MovieDetailsIntent.Load -> {
                fetchMovieDetailsScenario.launch(
                    FetchMovieDetailsScenario.Params(intent.id),
                    showLoading = true
                )

                viewModelScope.launchIO {
                    try {
                        getMoviesDetailsScenario(GetMoviesDetailsScenario.Params(intent.id)).distinctUntilChanged()
                            .collectLatest {
                                reduceToState {
                                    copy(movieDetails = it, items = mapDetailsItems(it).awaitAll())
                                }
                            }
                    } catch (e: Exception) {
                        reducer.onError(e, GetMoviesDetailsScenario::class)
                    }
                }
            }
        }
    }

    private suspend fun mapDetailsItems(model: MoviesDetailsModel): List<Deferred<BaseUiModel>> {
        return mutableListOf<Deferred<BaseUiModel>>().apply {
            // Item general information
            addAsync {
                ItemGeneralInformation(
                    tag = "item_general_information",
                    originalTitle = model.details.originalTitle,
                    originalLanguage = model.details.originalLanguage,
                    budget = model.details.budged,
                    revenue = model.details.revenue,
                    country = model.details.countryOfOrigin
                )
            }

            // Item description
            addAsync {
                ItemDescription(
                    tag = "item_description",
                    title = Text.Resource(R.string.key_description),
                    description = model.details.overview,
                    isEmptyOverview = model.details.overview.isEmpty()
                )
            }

            //Item cast
            addAsync {
                ItemMovieCasts(
                    tag = "item_movie_cast",
                    listCasts = model.actors.map {
                        ItemCast(
                            tag = "cast_${it.castId}",
                            name = it.name,
                            characterName = it.characterName,
                            image = it.profilePath
                        )
                    }
                )
            }
        }
    }

    inner class MovieDetailsReduce : Reducer<MovieDetailsState, MovieDetailsEvent>(MovieDetailsState())
}