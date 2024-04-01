package com.top1shvetsvadim1.jarvis.presentation.ui.main_tabs.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.example.coreutills.extentions.animateIsVisible
import com.top1shvetsvadim1.jarvis.R
import com.top1shvetsvadim1.jarvis.databinding.FragmentHomeBinding
import com.top1shvetsvadim1.jarvis.presentation.base.FragmentBaseMVI
import com.top1shvetsvadim1.jarvis.presentation.ui.main_tabs.home.ui_items.ItemBaseMoviesDelegate
import com.top1shvetsvadim1.jarvis.presentation.ui.main_tabs.home.ui_items.ItemGenresDelegate
import com.top1shvetsvadim1.jarvis.presentation.ui.main_tabs.home.ui_items.ItemMovieHorizontalDelegate
import com.top1shvetsvadim1.jarvis.presentation.ui.main_tabs.home.ui_items.ItemNowPlayingBaseDelegate
import com.top1shvetsvadim1.jarvis.presentation.ui.main_tabs.home.ui_items.ItemNowPlayingPosterDelegate
import com.top1shvetsvadim1.jarvis.presentation.ui.main_tabs.tabs.FragmentMainTabHostDirections
import com.top1shvetsvadim1.jarvis.presentation.utils.custo_views.SpaceDecorator
import com.top1shvetsvadim1.jarvis.presentation.utils.recycler_utils.Action
import com.top1shvetsvadim1.jarvis.presentation.utils.recycler_utils.DelegateAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FragmentHome : FragmentBaseMVI<FragmentHomeBinding, HomeState, HomeEvent, HomeViewModel>() {
    override val viewModel: HomeViewModel by viewModels()

    private val adapterHome = DelegateAdapter.Builder()
        .setDelegates(ItemNowPlayingBaseDelegate(), ItemBaseMoviesDelegate(), ItemGenresDelegate())
        .setActionProcessor {
            processAction(it)
        }
        .build()

    override fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentHomeBinding {
        return FragmentHomeBinding.inflate(inflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.handleIntent(HomeIntent.LoadItems)
    }

    override fun render(state: HomeState) {
        requireBinding().isLoading.isVisible = state.isLoading
        requireBinding().recyclerView.animateIsVisible(!state.isLoading)
        adapterHome.submitListChunked(state.items, lifecycleScope)
    }

    override fun applyOnViews(): FragmentHomeBinding.() -> Unit {
        return {
            initRecyclerView()
        }
    }

    private fun initRecyclerView() {
        requireBinding().recyclerView.adapter = adapterHome
        requireBinding().recyclerView.setRecycledViewPool(RecyclerView.RecycledViewPool())
        requireBinding().recyclerView.addItemDecoration(SpaceDecorator(requireContext(), R.dimen.default_margin))
    }

    private fun processAction(action: Action) {
        fun processActionItemMovieHorizontal(action: ItemMovieHorizontalDelegate.ItemMovieHorizontalActions) {
            when (action) {
                is ItemMovieHorizontalDelegate.ItemMovieHorizontalActions.OnClickMovie -> {
                    navigateToAnimated(FragmentMainTabHostDirections.toFragmentMovieDetails(action.id))
                }
            }
        }

        fun processActionItemNowPlaying(action: ItemNowPlayingPosterDelegate.NowPlayingAction) {
            when (action) {
                is ItemNowPlayingPosterDelegate.NowPlayingAction.OnClick -> {
                    navigateToAnimated(FragmentMainTabHostDirections.toFragmentMovieDetails(action.id))
                }
            }
        }

        when (action) {
            is ItemMovieHorizontalDelegate.ItemMovieHorizontalActions -> processActionItemMovieHorizontal(action)
            is ItemNowPlayingPosterDelegate.NowPlayingAction -> processActionItemNowPlaying(action)
        }
    }

}