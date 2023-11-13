package com.top1shvetsvadim1.jarvis.presentation.ui.main_tabs.details

import androidx.lifecycle.viewModelScope
import com.example.coreutills.wrappers.JobWrapper
import com.top1shvetsvadim1.jarvis.domain.usecase.movies_details.FetchMovieDetailsScenario
import com.top1shvetsvadim1.jarvis.domain.usecase.movies_details.GetMoviesDetailsScenario
import com.top1shvetsvadim1.jarvis.presentation.base.Reducer
import com.top1shvetsvadim1.jarvis.presentation.base.ViewModelBase
import com.top1shvetsvadim1.jarvis.presentation.utils.extentions.launchIO
import dagger.hilt.android.lifecycle.HiltViewModel
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
                                    copy(movieDetails = it)
                                }
                            }
                    } catch (e: Exception) {
                        reducer.onError(e, GetMoviesDetailsScenario::class)
                    }
                }
            }
        }
    }

    inner class MovieDetailsReduce : Reducer<MovieDetailsState, MovieDetailsEvent>(MovieDetailsState())
}