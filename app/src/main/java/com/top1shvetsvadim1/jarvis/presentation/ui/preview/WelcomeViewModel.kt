package com.top1shvetsvadim1.jarvis.presentation.ui.preview

import android.widget.FrameLayout
import com.top1shvetsvadim1.jarvis.R
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
            add(
                ItemPreviewUIModel(
                    tag = "preview_1",
                    viewResourceID = R.layout.item_preview,
                    viewConfiguration = {},
                    viewLayoutParams = FrameLayout.LayoutParams(
                        FrameLayout.LayoutParams.MATCH_PARENT,
                        FrameLayout.LayoutParams.MATCH_PARENT
                    ),
                )
            )
            add(
                ItemPreviewUIModel(
                    tag = "preview_2",
                    viewResourceID = R.layout.item_preview_2,
                    viewConfiguration = {},
                    viewLayoutParams = FrameLayout.LayoutParams(
                        FrameLayout.LayoutParams.MATCH_PARENT,
                        FrameLayout.LayoutParams.MATCH_PARENT
                    )
                )
            )
            add(
                ItemPreviewUIModel(
                    tag = "preview_3",
                    viewResourceID = R.layout.item_preview_3,
                    viewConfiguration = {},
                    viewLayoutParams = FrameLayout.LayoutParams(
                        FrameLayout.LayoutParams.MATCH_PARENT,
                        FrameLayout.LayoutParams.MATCH_PARENT
                    )

                )
            )
        }
    }

    class WelcomeReducer : Reducer<WelcomeState, WelcomeEvent>(WelcomeState())
}