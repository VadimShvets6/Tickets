package com.top1shvetsvadim1.jarvis.presentation.ui.main_tabs.tabs

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.graphics.Insets
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.NavHostFragment
import com.top1shvetsvadim1.jarvis.R
import com.top1shvetsvadim1.jarvis.databinding.FragmentMainTabHostBinding
import com.top1shvetsvadim1.jarvis.presentation.base.FragmentBaseMVI
import com.top1shvetsvadim1.jarvis.presentation.utils.managers.NavigationManager
import com.top1shvetsvadim1.jarvis.presentation.utils.views.HubBottomNavigation
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FragmentMainTabHost :
    FragmentBaseMVI<FragmentMainTabHostBinding, MainTabHostState, MainTabHostEvent, MainTabHostViewModel>() {

    override val viewModel: MainTabHostViewModel by viewModels()

    override fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentMainTabHostBinding {
        return FragmentMainTabHostBinding.inflate(inflater)
    }

    override fun setupInset(inset: Insets) {
        requireBinding().navigation.consumeInsets(inset)
    }

    override fun render(state: MainTabHostState) {

    }

    override fun applyOnViews(): FragmentMainTabHostBinding.() -> Unit {
        return {
            requireBinding().navigation.consumeTabs(mutableListOf<HubBottomNavigation.Tab>().apply {
                add(HubBottomNavigation.Tab(
                    id = R.id.fragmentHome, icon = R.drawable.ic_home, text = "Home"
                ))
                add(HubBottomNavigation.Tab(
                    id = R.id.fragmentWelcome2, icon = R.drawable.ic_search, text = "Search"
                ))
                add(HubBottomNavigation.Tab(
                    id = R.id.fragmentHome, icon = R.drawable.ic_saved, text = "Tickets"
                ))
                add(HubBottomNavigation.Tab(
                    id = R.id.fragmentHome, icon = R.drawable.ic_profile, text = "Profile"
                ))
            })
            val navHostFragment = requireBinding().tabHostMain.getFragment<NavHostFragment>()
            val navController = navHostFragment.navController
            navController.let { NavigationManager.setupWithNavController(requireBinding().navigation, it) }

        }
    }
}