package com.top1shvetsvadim1.jarvis.presentation.ui.preview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.graphics.Insets
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.viewModels
import com.example.coreutills.managers.ScreenManager
import com.top1shvetsvadim1.jarvis.databinding.FragmentWelcomeBinding
import com.top1shvetsvadim1.jarvis.presentation.base.FragmentBaseMVI
import com.top1shvetsvadim1.jarvis.presentation.ui.preview.items.ItemPreviewDelegate
import com.top1shvetsvadim1.jarvis.presentation.ui.preview.items.ItemPreviewUIModel
import com.top1shvetsvadim1.jarvis.presentation.utils.recycler_utils.DelegateAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FragmentWelcome : FragmentBaseMVI<FragmentWelcomeBinding, WelcomeState, WelcomeEvent, WelcomeViewModel>() {

    override val viewModel: WelcomeViewModel by viewModels()

    private val adapter = DelegateAdapter.Builder()
        .setDelegates(ItemPreviewDelegate())
        .build()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.handleIntent(WelcomeIntent.LoadItems)
    }

    override fun render(state: WelcomeState) {
        adapter.submitList(state.items)
    }

    override fun setupInset(inset: Insets) {
        requireBinding().start.updateLayoutParams<ConstraintLayout.LayoutParams> {
            bottomMargin = inset.bottom + 50
        }
    }

    override fun applyOnViews(): FragmentWelcomeBinding.() -> Unit {
        return {
            ScreenManager.setStatusBarContrast(requireActivity(), false)
            start.setOnClickListener {
            }
            viewPager.adapter = adapter
            // indicator.attachTo(viewPager)
        }
    }

    override fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentWelcomeBinding {
        return FragmentWelcomeBinding.inflate(inflater)
    }
}