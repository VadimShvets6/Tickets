package com.top1shvetsvadim1.jarvis.presentation.base

import android.app.Dialog
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.FrameLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.graphics.Insets
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsAnimationCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.core.view.updateLayoutParams
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.example.coreutills.extentions.animateHeight
import com.example.coreutills.extentions.tryNull
import com.example.coreutills.managers.ScreenManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.top1shvetsvadim1.jarvis.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

abstract class BaseBottomDialog<T : ViewBinding, ARG : BaseBottomDialog.BottomDialogParams> @JvmOverloads constructor(
    val isFullExpanded: Boolean = false,
    val isFullScreen: Boolean = false
) : BottomSheetDialogFragment(), ViewBindingHolder<T> {

    override var binding: T? = null
    private var dialog: BottomSheetDialog? = null
    var behavior: BottomSheetBehavior<View>? = null

    val scope = CoroutineScope(Dispatchers.IO)

    private var viewForAnim: View? = null

    abstract fun getViewBinding(): T

    protected var arg: ARG? = null

    protected inline fun params(params: (ARG) -> Unit) {
        arg?.let(params) ?: dismiss()
    }

    protected inline fun onParams(params: ARG.() -> Unit) {
        arg?.let(params) ?: dismiss()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        dialog = BottomSheetDialog(requireContext())
        dialog?.setOnShowListener {
            val d = it as BottomSheetDialog
            val sheet =
                d.findViewById<FrameLayout>(com.google.android.material.R.id.design_bottom_sheet)

            it.window?.let { it1 ->
                if (activity == null) return@setOnShowListener
                WindowInsetsControllerCompat(it1, it1.decorView).isAppearanceLightNavigationBars =
                    ScreenManager.isThemeLight(requireActivity())
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.Q) {
                    it1.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING)
                } else {
                    it1.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
                }
                WindowCompat.setDecorFitsSystemWindows(it1, false)
            }
            it.window?.setDimAmount(0.33f)

            sheet?.setBackgroundColor(Color.parseColor("#eeffaa"))
            sheet?.background = null
            if (isFullScreen) {
                sheet?.updateLayoutParams<CoordinatorLayout.LayoutParams> {
                    height =
                        ScreenManager.calculateRealHeight(requireContext()) - ScreenManager.statusBarPadding - 10
                }
            }
            sheet?.let {
                behavior = BottomSheetBehavior.from(sheet)
            }
            sheet?.let { view ->
                ViewCompat.setOnApplyWindowInsetsListener(view) { _, insets ->
                    insets.apply {
                        view.updatePadding(bottom = 0)
                    }
                }
            }
            behavior?.isHideable = true
            behavior?.isDraggable = true
            if (isFullScreen) {
                behavior?.peekHeight =
                    requireContext().resources.getDimension(R.dimen._280sdp).toInt()
            }
            if (isFullExpanded) {
                behavior?.state = BottomSheetBehavior.STATE_EXPANDED
            }
            behavior?.isFitToContents = true
            behavior?.halfExpandedRatio = 0.66f
            behavior?.skipCollapsed = true
            setPeekHeight(isFullExpanded)
        }

        return dialog ?: super.onCreateDialog(savedInstanceState)
    }

    open fun onReady() {

    }

    open fun setupInset(inset: Insets) {
        binding?.root?.updatePadding(bottom = inset.bottom, top = 0)
    }

    private fun setSystemBarsInsets(inset: (Insets) -> Unit) {
        getSystemBarsInset(view = requireBinding().root, onInset = inset)
    }

    private fun getSystemBarsInset(view: View, onInset: (Insets) -> Unit) {
        ViewCompat.setOnApplyWindowInsetsListener(view) { _, windowInset ->
            val systemGesture = windowInset.getInsets(WindowInsetsCompat.Type.systemGestures())
            val inset =
                windowInset.getInsets(WindowInsetsCompat.Type.systemBars() or WindowInsetsCompat.Type.displayCutout())
            // TODO Temporary solution - solve the problem with animation with callback
            viewForAnim?.animateHeight(
                if (windowInset.isVisible(WindowInsetsCompat.Type.ime())) {
                    windowInset.getInsets(WindowInsetsCompat.Type.ime()).bottom
                } else inset.bottom
            )
            onInset(Insets.of(systemGesture.left, inset.top, systemGesture.right, inset.bottom))
            WindowInsetsCompat.CONSUMED
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewForAnim = getViewForImeAnimation()
        viewForAnim?.let { animationKeyboard(it) }
        setSystemBarsInsets(::setupInset)

        onReady()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = getViewBinding()
        binding?.let {
            registerBinding(it, viewLifecycleOwner)
        }
        return requireBinding().root
    }

    private fun setSystemBarsInsets() {

    }

    fun setPeekHeight(isExpanded: Boolean) {
        val behaviour = behavior

        behaviour?.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(p0: View, p1: Float) {

            }

            override fun onStateChanged(p0: View, p1: Int) {
                when (p1) {
                    BottomSheetBehavior.STATE_HIDDEN -> dismiss()
                    BottomSheetBehavior.STATE_COLLAPSED -> dismiss()
                    else -> Unit
                }
            }
        })
        if (isExpanded) {
            behaviour?.state = BottomSheetBehavior.STATE_EXPANDED
        }
    }

    private fun animationKeyboard(view: View) { //TODO solve the problem with animation with callback
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.Q) return
        ViewCompat.setWindowInsetsAnimationCallback(
            view,
            object : WindowInsetsAnimationCompat.Callback(
                DISPATCH_MODE_CONTINUE_ON_SUBTREE
            ) {
                private var startPosition = 0
                override fun onPrepare(animation: WindowInsetsAnimationCompat) {
                    context?.let {
                        startPosition = ScreenManager.calculateStartPositionView(it, view)
                    }
                }

                override fun onProgress(
                    insets: WindowInsetsCompat,
                    runningAnimations: MutableList<WindowInsetsAnimationCompat>
                ): WindowInsetsCompat {
                    val heightKeyboard =
                        insets.getInsets(WindowInsetsCompat.Type.ime()).bottom - startPosition
                    Log.d("deb", "KEYBOARD: $heightKeyboard")
                    view.animateHeight(heightKeyboard)
                    return insets
                }
            }
        )
    }

    /**
     * To animation the keyboard in dialogs, return the usual View from the method,
     * which will be located below the input field, with which we will change the
     * height depending on the system keyboard
     */
    open fun getViewForImeAnimation(): View? {
        return null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewForAnim = null
    }

    override fun beforeDestroyView() {

    }

    fun show(fragment: Fragment, params: ARG) {
        if (fragment.isAdded && !fragment.isDetached && !fragment.isRemoving) {
            tryNull {
                arg = params
                show(fragment.parentFragmentManager, this::class.simpleName.toString())
            }
        }
    }

    fun show(fragment: Fragment) {
        if (fragment.isAdded && !fragment.isDetached && !fragment.isRemoving) {
            tryNull {
                arg = null
                show(fragment.parentFragmentManager, this::class.simpleName.toString())
            }
        }
    }

    protected fun showKeyboard() {
        ViewCompat.getWindowInsetsController(requireBinding().root)?.show(WindowInsetsCompat.Type.ime())
    }

    override fun onSaveInstanceState(outState: Bundle) {
        //Do not call super
    }

    interface BottomDialogParams
}