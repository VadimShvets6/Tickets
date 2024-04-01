package com.top1shvetsvadim1.jarvis.presentation.ui.main_tabs.profile

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.top1shvetsvadim1.jarvis.databinding.FragmentMyProfileBinding
import com.top1shvetsvadim1.jarvis.presentation.base.FragmentBaseMVI
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FragmentMyProfile :
    FragmentBaseMVI<FragmentMyProfileBinding, MyProfileState, MyProfileEvent, MyProfileViewModel>() {
    override val viewModel: MyProfileViewModel by viewModels()

    override fun render(state: MyProfileState) {

    }

    override fun applyOnViews(): FragmentMyProfileBinding.() -> Unit {
        return {

        }
    }

    override fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentMyProfileBinding {
        return FragmentMyProfileBinding.inflate(inflater)
    }
}