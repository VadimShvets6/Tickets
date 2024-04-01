package com.top1shvetsvadim1.jarvis.presentation.ui.main_tabs.profile

import com.top1shvetsvadim1.jarvis.presentation.base.Reducer
import com.top1shvetsvadim1.jarvis.presentation.base.ViewModelBase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MyProfileViewModel @Inject constructor(

) : ViewModelBase<MyProfileState, MyProfileEvent, MyProfileIntent>() {
    override val reducer = MyProfileReducer()
    override fun handleIntent(intent: MyProfileIntent) {

    }

    inner class MyProfileReducer : Reducer<MyProfileState, MyProfileEvent>(MyProfileState())
}