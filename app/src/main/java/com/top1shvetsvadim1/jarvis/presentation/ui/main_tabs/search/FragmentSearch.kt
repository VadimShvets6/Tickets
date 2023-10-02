package com.top1shvetsvadim1.jarvis.presentation.ui.main_tabs.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.top1shvetsvadim1.jarvis.databinding.FragmentSearchBinding
import com.top1shvetsvadim1.jarvis.presentation.base.FragmentBaseMVI
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FragmentSearch : FragmentBaseMVI<FragmentSearchBinding, SearchState, SearchEvent, SearchViewModel>() {
    override val viewModel: SearchViewModel by viewModels()

    override fun render(state: SearchState) {
    }

    override fun applyOnViews(): FragmentSearchBinding.() -> Unit {
        return {

        }
    }

    override fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentSearchBinding {
        return FragmentSearchBinding.inflate(inflater)
    }
}