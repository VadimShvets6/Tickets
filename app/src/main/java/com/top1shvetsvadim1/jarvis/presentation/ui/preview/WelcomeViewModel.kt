package com.top1shvetsvadim1.jarvis.presentation.ui.preview

import com.top1shvetsvadim1.jarvis.R
import com.top1shvetsvadim1.jarvis.common.Image
import com.top1shvetsvadim1.jarvis.common.Text
import com.top1shvetsvadim1.jarvis.presentation.base.Reducer
import com.top1shvetsvadim1.jarvis.presentation.base.ViewModelBase
import com.top1shvetsvadim1.jarvis.presentation.ui.preview.items.ItemPreviewUIModel
import com.top1shvetsvadim1.jarvis.presentation.utils.recycler_utils.BaseUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class WelcomeViewModel @Inject constructor() : ViewModelBase<WelcomeState, WelcomeEvent, WelcomeIntent>() {

    override val reducer: Reducer<WelcomeState, WelcomeEvent> = WelcomeReducer()

    override fun handleIntent(intent: WelcomeIntent) {
        when (intent) {
            WelcomeIntent.LoadItems -> reduceToState {
                copy(
                    items = mapItems()
                )
            }
        }
    }

    private fun mapItems(): List<BaseUiModel> {
        return mutableListOf<BaseUiModel>().apply {
            add(ItemPreviewUIModel(
                tag = "preview_1",
                image = Image.Resource(R.drawable.preview_1),
                title = Text.Resource(R.string.key_movies_tv_and_much_more),
                description = Text.Resource(R.string.key_watch_anywhere),
            ))
        }
    }

    class WelcomeReducer : Reducer<WelcomeState, WelcomeEvent>(WelcomeState()) {

    }
}