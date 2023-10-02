package com.top1shvetsvadim1.jarvis.presentation.ui.main_tabs.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.top1shvetsvadim1.jarvis.databinding.FragmentHomeBinding
import com.top1shvetsvadim1.jarvis.presentation.base.FragmentBaseMVI
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FragmentHome : FragmentBaseMVI<FragmentHomeBinding, HomeState, HomeEvent, HomeViewModel>() {
    override val viewModel: HomeViewModel by viewModels()

    override fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentHomeBinding {
        return FragmentHomeBinding.inflate(inflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.handleIntent(HomeIntent.LoadItems)
    }

    override fun render(state: HomeState) {
    }

    override fun applyOnViews(): FragmentHomeBinding.() -> Unit {
        return {

        }
    }
}