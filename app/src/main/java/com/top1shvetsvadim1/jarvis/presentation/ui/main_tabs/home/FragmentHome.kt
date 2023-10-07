package com.top1shvetsvadim1.jarvis.presentation.ui.main_tabs.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.top1shvetsvadim1.jarvis.R
import com.top1shvetsvadim1.jarvis.databinding.FragmentHomeBinding
import com.top1shvetsvadim1.jarvis.presentation.base.FragmentBaseMVI
import com.top1shvetsvadim1.jarvis.presentation.ui.main_tabs.home.ui_items.ItemNowPlayingBaseDelegate
import com.top1shvetsvadim1.jarvis.presentation.ui.main_tabs.home.ui_items.ItemPopularMoviesDelegate
import com.top1shvetsvadim1.jarvis.presentation.utils.custo_views.SpaceDecorator
import com.top1shvetsvadim1.jarvis.presentation.utils.recycler_utils.DelegateAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FragmentHome : FragmentBaseMVI<FragmentHomeBinding, HomeState, HomeEvent, HomeViewModel>() {
    override val viewModel: HomeViewModel by viewModels()

    private val adapterHome = DelegateAdapter.Builder()
        .setDelegates(ItemNowPlayingBaseDelegate(), ItemPopularMoviesDelegate())
        .setActionProcessor { }
        .build()

    override fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentHomeBinding {
        return FragmentHomeBinding.inflate(inflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.handleIntent(HomeIntent.LoadItems)
    }

    override fun render(state: HomeState) {
        adapterHome.submitList(state.items)
    }

    override fun applyOnViews(): FragmentHomeBinding.() -> Unit {
        return {
            initRecyclerView()
        }
    }

    private fun initRecyclerView() {
        requireBinding().recyclerView.adapter = adapterHome
        requireBinding().recyclerView.addItemDecoration(SpaceDecorator(requireContext(), R.dimen.default_margin))
    }

}