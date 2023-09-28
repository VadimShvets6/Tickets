package com.top1shvetsvadim1.jarvis.presentation.ui.preview.dialogs

import android.content.DialogInterface
import android.content.res.ColorStateList
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.graphics.Insets
import com.top1shvetsvadim1.jarvis.R
import com.top1shvetsvadim1.jarvis.databinding.BottomDialogStartNowBinding
import com.top1shvetsvadim1.jarvis.presentation.base.BaseBottomDialog
import com.top1shvetsvadim1.jarvis.presentation.utils.extentions.launchUI
import com.top1shvetsvadim1.jarvis.presentation.utils.storage.PropertiesStorage
import kotlinx.coroutines.delay

class BottomDialogStartNow : BaseBottomDialog<BottomDialogStartNowBinding, BottomDialogStartNow.Params>(
    isFullExpanded = true,
    isFullScreen = true
) {

    data class Params(
        val email: String,
        val onClickStart: () -> Unit
    ) : BottomDialogParams

    override fun getViewBinding(): BottomDialogStartNowBinding {
        return BottomDialogStartNowBinding.inflate(layoutInflater)
    }

    override fun setupInset(inset: Insets) {}

    override fun getViewForImeAnimation(): View? {
        return binding?.forKeyboard
    }

    override fun onReady() {
        applyOnBinding {
            params { args ->
                scope.launchUI {
                    delay(300)
                    showKeyboard()
                    editText.requestFocus()
                }
                val colorEditTextContainer = if (args.email.isNotBlank()) {
                    ContextCompat.getColor(requireContext(), R.color.c_green_00a676)
                } else {
                    ContextCompat.getColor(requireContext(), R.color.c_blue_text_0091ff_c0e3fd)
                }
                requireBinding().editTextContainer.boxStrokeColor = colorEditTextContainer
                requireBinding().editTextContainer.hintTextColor = ColorStateList.valueOf(colorEditTextContainer)
                editText.setText(args.email)
                editText.setSelection(args.email.length)
                start.setOnClickListener {
                    dismiss()
                    args.onClickStart()
                }
                closeButton.setOnClickListener { dismiss() }
            }
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        PropertiesStorage.set(
            PropertiesStorage.Properties.PotentialUserEmailAddress,
            requireBinding().editText.text.toString()
        )
    }
}