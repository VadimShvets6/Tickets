package com.top1shvetsvadim1.jarvis.presentation.ui.main_tabs.tickets

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.top1shvetsvadim1.jarvis.databinding.FragmentTicketsBinding
import com.top1shvetsvadim1.jarvis.presentation.base.FragmentBaseMVI
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FragmentTickets : FragmentBaseMVI<FragmentTicketsBinding, TicketsState, TicketsEvent, TicketsViewModel>() {
    override val viewModel: TicketsViewModel by viewModels()

    override fun render(state: TicketsState) {
    }

    override fun applyOnViews(): FragmentTicketsBinding.() -> Unit {
        return {

        }
    }

    override fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentTicketsBinding {
        return FragmentTicketsBinding.inflate(inflater)
    }
}