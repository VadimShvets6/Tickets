package com.top1shvetsvadim1.jarvis.presentation.ui.main_tabs.details

import android.text.Spannable
import android.text.SpannableString
import android.text.style.RelativeSizeSpan
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.viewModelScope
import com.example.coreutills.utils.CustomTypefaceSpan
import com.example.coreutills.wrappers.JobWrapper
import com.top1shvetsvadim1.jarvis.R
import com.top1shvetsvadim1.jarvis.common.Text
import com.top1shvetsvadim1.jarvis.domain.models.MoviesDetailsModel
import com.top1shvetsvadim1.jarvis.domain.usecase.movies_details.FetchMovieDetailsScenario
import com.top1shvetsvadim1.jarvis.domain.usecase.movies_details.GetMoviesDetailsScenario
import com.top1shvetsvadim1.jarvis.presentation.base.Reducer
import com.top1shvetsvadim1.jarvis.presentation.base.ViewModelBase
import com.top1shvetsvadim1.jarvis.presentation.controler.ContextManager.retrieveApplicationContext
import com.top1shvetsvadim1.jarvis.presentation.ui.main_tabs.details.ui_items.ItemCast
import com.top1shvetsvadim1.jarvis.presentation.ui.main_tabs.details.ui_items.ItemDescription
import com.top1shvetsvadim1.jarvis.presentation.ui.main_tabs.details.ui_items.ItemGeneralInformation
import com.top1shvetsvadim1.jarvis.presentation.ui.main_tabs.details.ui_items.ItemMovieCasts
import com.top1shvetsvadim1.jarvis.presentation.ui.main_tabs.details.ui_items.ItemRating
import com.top1shvetsvadim1.jarvis.presentation.utils.extentions.addAsync
import com.top1shvetsvadim1.jarvis.presentation.utils.extentions.launchIO
import com.top1shvetsvadim1.jarvis.presentation.utils.extentions.roundToTwoDecimal
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
        fun formatRatingString(): SpannableString {
            return if (model.details.voteCount > 0) {
                val base = "${model.details.voteAverage.roundToTwoDecimal()}/10"
                val spanBase = SpannableString(base)
                spanBase.setSpan(RelativeSizeSpan(0.33f), 4, 6, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
                spanBase.setSpan(
                    CustomTypefaceSpan(
                        ResourcesCompat.getFont(
                            retrieveApplicationContext(),
                            R.font.roboto_regular
                        ) ?: return SpannableString("")
                    ), 4, 6, Spannable.SPAN_INCLUSIVE_INCLUSIVE
                )
                spanBase
            } else {
                SpannableString("")
            }
        }

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

            addAsync {
                val votesString = if (model.details.voteCount == 0) {
                    Text.Resource(R.string.key_no_one_rated)
                } else {
                    Text.Resource(
                        R.string.n_rates,
                        listOf(Text.Simple(model.details.voteCount.toString()))
                    )
                }
                ItemRating(tag = "item_rating", rating = formatRatingString(), voteCount = votesString)
            }
        }
    }

    inner class MovieDetailsReduce : Reducer<MovieDetailsState, MovieDetailsEvent>(MovieDetailsState())
}