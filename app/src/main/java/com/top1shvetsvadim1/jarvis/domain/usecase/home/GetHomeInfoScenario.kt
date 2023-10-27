package com.top1shvetsvadim1.jarvis.domain.usecase.home

import com.top1shvetsvadim1.jarvis.domain.models.HomeGenresModel
import com.top1shvetsvadim1.jarvis.domain.models.HomeHubModel
import com.top1shvetsvadim1.jarvis.domain.models.HomeMoviesModel
import com.top1shvetsvadim1.jarvis.domain.models.HomePeopleModel
import com.top1shvetsvadim1.jarvis.domain.repositories.RepositoryGenres
import com.top1shvetsvadim1.jarvis.domain.repositories.RepositoryMoviesNowPlaying
import com.top1shvetsvadim1.jarvis.domain.repositories.RepositoryMoviesPopular
import com.top1shvetsvadim1.jarvis.domain.repositories.RepositoryMoviesTopRated
import com.top1shvetsvadim1.jarvis.domain.repositories.RepositoryMoviesUpcoming
import com.top1shvetsvadim1.jarvis.domain.repositories.RepositoryPeople
import com.top1shvetsvadim1.jarvis.domain.repositories.RepositoryTrending
import com.top1shvetsvadim1.jarvis.presentation.base.SuspendUseCaseNoParams
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetHomeInfoScenario @Inject constructor(
    private val repositoryMoviesNowPlaying: RepositoryMoviesNowPlaying,
    private val repositoryMoviesPopular: RepositoryMoviesPopular,
    private val repositoryMoviesTopRated: RepositoryMoviesTopRated,
    private val repositoryGenres: RepositoryGenres,
    private val repositoryMoviesUpcoming: RepositoryMoviesUpcoming,
    private val repositoryTrending: RepositoryTrending,
    private val repositoryPeople: RepositoryPeople
) : SuspendUseCaseNoParams<Flow<HomeHubModel>> {
    override suspend fun invoke(): Flow<HomeHubModel> {
        return combine(
            getListWithMovies(),
            getListGenres(),
            getListPeople()
        ) { movies, genres, peoples ->
            HomeHubModel(
                movies = movies,
                genres = genres,
                people = peoples
            )
        }
    }

    private suspend fun getListWithMovies(): Flow<HomeMoviesModel> {
        return combine(
            repositoryMoviesNowPlaying.getListMoviesNowPlayingPreview(),
            repositoryMoviesPopular.getListMoviePopularPreview(),
            repositoryMoviesTopRated.getListMovieTopRatedPreview(),
            repositoryMoviesUpcoming.getMoviesUpcomingListPreview(),
            repositoryTrending.getTrendingMoviesPreview(),
        ) { nowPlayingList, popularList, topRated, upcoming, trending ->
            HomeMoviesModel(
                listNowPlaying = nowPlayingList,
                listPopularMovies = popularList,
                listTopRated = topRated,
                listUpcoming = upcoming,
                listTrendMovies = trending,
            )
        }
    }

    private suspend fun getListGenres(): Flow<HomeGenresModel> {
        return repositoryGenres.getAllListGenres().map {
            HomeGenresModel(listGenres = it)
        }
    }

    private suspend fun getListPeople(): Flow<HomePeopleModel> {
        return repositoryPeople.getTrendingPeopleListPreview().map {
            HomePeopleModel(listTrendPeople = it)
        }
    }
}