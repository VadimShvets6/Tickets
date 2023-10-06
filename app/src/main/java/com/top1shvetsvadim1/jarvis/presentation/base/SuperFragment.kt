package com.top1shvetsvadim1.jarvis.presentation.base

import android.app.Activity
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.activity.OnBackPressedCallback
import androidx.annotation.IdRes
import androidx.core.graphics.Insets
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsAnimationCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.example.coreutills.extentions.focusParent
import com.example.coreutills.extentions.tryNull
import com.example.coreutills.managers.ScreenManager
import com.top1shvetsvadim1.jarvis.R
import com.top1shvetsvadim1.jarvis.presentation.activity.MainActivity
import dagger.android.support.AndroidSupportInjection

abstract class SuperFragment<T : ViewBinding> : Fragment(), ViewBindingHolder<T>, FragmentNavigationInterface,
    FragmentBaseInterface, InsetsSupported {

    abstract fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?): T

    override var binding: T? = null

    // viewAnimateIme - transferable container
    private var viewAnimateIme: View? = null

    private var showNetworkView: Boolean = false

    // focus - for register focus listener
    protected var focus: ViewTreeObserver.OnGlobalFocusChangeListener? = null

    // keyboard - size keyboard
    private var keyboard = 0
    protected var animatorCallback: WindowInsetsAnimationCompat.Callback? = null

    //Insets
    protected var insets: Insets = Insets.NONE

    /*var networkInfoView: NetworkInfoView? = null
    private var showNetworkView: Boolean = false*/

    override fun beforeDestroyView() {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        performDependencyInjection()
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(requireActivity().window, false)
        hideSoftInput()
    }

    private fun performDependencyInjection() {
      //  AndroidSupportInjection.inject(this)
    }

    open fun reconnect() {}

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = getViewBinding(inflater, container)
        val view = binding?.root
        binding?.let {
            registerBinding(it, viewLifecycleOwner)
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setSystemBarsInsets(::setupInset)
        viewAnimateIme = getViewForImeAnimation()
        viewAnimateIme?.let { animateImeForViews(it) }
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.Q) {
            activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        }
    }

    //===================NAVIGATION=======================================

    override fun navigateToAnimatedPop(navDirections: NavDirections) {
        findNavControllerSafely()?.apply {
            if (currentDestination?.getAction(navDirections.actionId) != null) {
                currentDestination?.id?.let {
                    navigate(navDirections, getHorizontalNavOptions().setPopUpTo(it, true).build())
                } ?: run {
                    navigateToAnimated(navDirections)
                }
            }
        }
    }

    override fun navigateToAnimatedID(@IdRes navigateToRes: Int, args: Bundle) {
        findNavControllerSafely()?.apply {
            tryNull {
                navigate(navigateToRes, args, getNavOptions())
            }
        }
    }

    override fun navigateToAnimated(navDirections: NavDirections) {
        findNavControllerSafely()?.apply {
            if (currentDestination?.getAction(navDirections.actionId) != null) {
                try {
                    navigate(navDirections, getNavOptions())
                } catch (ex: Exception) {
                    ex.printStackTrace()
                }
            }
        }
    }

    override fun navigateTo(direction: NavDirections) {
        findNavControllerSafely()?.apply {
            if (currentDestination?.getAction(direction.actionId) != null) {
                tryNull {
                    navigate(direction)
                }
            }
        }
    }

    override fun navigateTo(@IdRes navigateToRes: Int) {
        findNavControllerSafely()?.navigate(navigateToRes)
    }

    override fun navigateToAnimated(@IdRes navigateToRes: Int, args: Bundle) {
        findNavControllerSafely()?.navigate(navigateToRes, args, getHorizontalNavOptions().build())
    }

    override fun navigatePopBackStack() {
        findNavControllerSafely()?.popBackStack()
    }

    override fun navigateUp() {
        findNavControllerSafely()?.navigateUp()
    }

    override fun pop() {
        findNavControllerSafely()?.popBackStack()
    }

    private fun getHorizontalNavOptions(): NavOptions.Builder {
        return NavOptions.Builder()
            .setEnterAnim(R.anim.h_fragment_enter_horrizontal)
            .setExitAnim(R.anim.h_fragment_pop_exit_horrizontal)
            .setPopEnterAnim(R.anim.h_fragment_pop_enter_horrizontal)
            .setPopExitAnim(R.anim.h_fragment_exit_horrizontal)
    }

    override fun getNavigationResult(key: String): MutableLiveData<String>? =
        try {
            findNavControllerSafely()?.currentBackStackEntry?.savedStateHandle?.getLiveData(key)
        } catch (e: Exception) {
            MutableLiveData(key)
        }


    override fun findNavControllerSafely(): NavController? {
        exitTransition = null
        reenterTransition = null
        return findNavControllerForTransition()
    }

    fun findNavControllerForTransition(): NavController? {
        return if (!isDetached) {
            try {
                if (activity is MainActivity) {
                    (activity as MainActivity).navController
                } else {
                    findNavController()
                }
            } catch (ex: Exception) {
                null
            }
        } else {
            null
        }
    }

    override fun handleOnBackPressed(OnBackPressedHandle: (() -> Unit)?) {
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(OnBackPressedHandle != null) {
                override fun handleOnBackPressed() {
                    OnBackPressedHandle?.invoke()
                }
            }
        )
    }

    override fun popTo(@IdRes destinationId: Int, inclusive: Boolean) {
        findNavControllerSafely()?.popBackStack(destinationId, inclusive)
    }

    override fun navigateToInclusive(navDirections: NavDirections, @IdRes popUpToId: Int) {
        findNavControllerSafely()?.apply {
            if (currentDestination?.getAction(navDirections.actionId) != null) {
                tryNull {
                    val option = NavOptions.Builder()
                        .setPopUpTo(popUpToId, true)
                        .build()
                    navigate(navDirections, option)
                }
            }
        }
    }

    override fun navigateToAnimatedInclusive(navDirections: NavDirections, @IdRes popUpToId: Int) {
        findNavControllerSafely()?.apply {
            if (currentDestination?.getAction(navDirections.actionId) != null) {
                tryNull {
                    val option = getHorizontalNavOptions()
                        .setPopUpTo(popUpToId, true)
                        .build()
                    navigate(navDirections, option)
                }
            }
        }
    }

    override fun navigateToAnimated(navDirections: NavDirections, @IdRes popUpToId: Int) {
        findNavControllerSafely()?.apply {
            if (currentDestination?.getAction(navDirections.actionId) != null) {
                tryNull {
                    navigate(navDirections, getNavOptions(popUpToId))
                }
            }
        }
    }

    override fun navigateToAnimatedPop(@IdRes navigateToRes: Int, args: Bundle) {
        findNavControllerSafely()?.apply {
            currentDestination?.id?.let {
                navigate(navigateToRes, args, getHorizontalNavOptions().setPopUpTo(it, true).build())
            } ?: run {
                navigate(navigateToRes, args, getNavOptions())
            }
        }
    }

    override fun setNavigationResult(result: String?, key: String) {
        try {
            findNavControllerSafely()?.previousBackStackEntry?.savedStateHandle?.set(key, result)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    protected open fun getNavOptions(): NavOptions? {
        val builder = getHorizontalNavOptions()
        return builder.build()
    }

    protected open fun getNavOptions(@IdRes resId: Int): NavOptions? {
        val builder = getHorizontalNavOptions()
        return builder.setPopUpTo(resId, false).build()
    }

    protected open fun getVerticalNavOptions(): NavOptions.Builder {
        return NavOptions.Builder()
            .setEnterAnim(R.anim.fade_in)
            .setPopExitAnim(R.anim.fade_out)
    }

    //====================KEYBOARD ANIMATION====================================

    override fun setupInset(inset: Insets) {
        requireBinding().root.updatePadding(top = inset.top, bottom = inset.bottom)
    }

    private fun getSystemBarsInset(
        view: View,
        onInset: (Insets) -> Unit
    ) {
        ViewCompat.setOnApplyWindowInsetsListener(view) { _, windowInset ->
            val systemGesture = windowInset.getInsets(WindowInsetsCompat.Type.systemGestures())
            val inset =
                windowInset.getInsets(WindowInsetsCompat.Type.systemBars() or WindowInsetsCompat.Type.displayCutout())
            ScreenManager.consumeInset(inset)
            insets = inset
            onInset(Insets.of(systemGesture.left, inset.top, systemGesture.right, inset.bottom))
            WindowInsetsCompat.CONSUMED
        }
    }

    /**
     * Method calls getSystemBarsInset(), passes the current root of the screen as a parameter. By default it is shifted
     * by root
     */
    private fun setSystemBarsInsets(inset: (Insets) -> Unit) {
        getSystemBarsInset(
            view = requireBinding().root,
            onInset = inset
        )
    }

    /**
     * A Method for shifting the content to the visibility of the keyboard.
     * this method takes your content container as a parameter. When the keyboard is opened,
     * the method will shift all content so that the view has focus is above the keyboard.
     * Tested with ScrollView, ConstraintLayout, LinearLayout, FrameLayout, Relative Layout.
     * Works unstable in conjunction with CoordinatorLayout.
     *
     * To register a method in your fragment override the getViewForImeAnimation() method,
     * return your container
     * Example:
     *        override fun getViewForImeAnimation(): View? {
     *              return binding.root
     *        }
     */
    private fun animateImeForViews(
        views: View,
        modes: InsetFocus = InsetFocus.CURRENT_FOCUS
    ) {

        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.Q) return

        var startBottom = 0
        var startRoot: Int
        var offset = 0f
        var invers = false
        var startBottomPrevious = 0
        when (modes) {
            InsetFocus.CURRENT_FOCUS -> {
                ViewCompat.setWindowInsetsAnimationCallback(views, object : WindowInsetsAnimationCompat.Callback(
                    DISPATCH_MODE_CONTINUE_ON_SUBTREE
                ) {

                    init {
                        animatorCallback = this
                    }

                    var padding = 0
                    override fun onPrepare(animation: WindowInsetsAnimationCompat) {
                        super.onPrepare(animation)
                        if (views is RecyclerView) {
                            activity?.currentFocus?.let {
                                context?.apply {
                                    startBottom = calculateStartPosition(it.focusParent)
                                    if (padding == 0) {
                                        padding = views.paddingBottom
                                    }
                                    startRoot = calculateStartPosition(views)
                                    if (startBottom < startRoot) {
                                        views.scrollY += startRoot - startBottom
                                        startBottom = calculateStartPosition(it.focusParent)
                                    }
                                }
                            }
                        } else {
                            // Here we calculate the position of the view in focus to find out its Y coordinates
                            activity?.currentFocus?.let {
                                /*Screen size not including systemBars, view location by Y on top and subtract its
                                size to find out the coordinates from ist bottom*/
                                startBottom = calculateStartPosition(it.focusParent)
                                startRoot = calculateStartPosition(views)
                                if (startBottom < startRoot) {
                                    views.scrollY += startRoot - startBottom
                                    startBottom = calculateStartPosition(it.focusParent)
                                }
                            }
                        }
                    }

                    override fun onProgress(
                        insets: WindowInsetsCompat,
                        runningAnimations: MutableList<WindowInsetsAnimationCompat>
                    ): WindowInsetsCompat {
                        keyboard = insets.getInsets(WindowInsetsCompat.Type.ime()).bottom
                        val insetData = insets.getInsets(WindowInsetsCompat.Type.ime())
                        ScreenManager.setKeyboard(keyboard)
                        if (views is RecyclerView) {
                            if (startBottom > keyboard) {
                                views.updatePadding(bottom = padding)
                            } else {
                                val offsetRecycler = startBottom - keyboard
                                views.translationY = offsetRecycler.toFloat()
                            }
                            if (invers) {
                                views.translationY = 0f
                            }
                        } else {

                            /*If the absolute start is below the keyboard size, shift the content by Y find
                            out the required shift in pixel through offset*/
                            if (startBottom < keyboard) {
                                offset = (startBottom - keyboard).toFloat()
                                views.translationY = offset
                            }

                            // After the keyboard has disappeared from the screen, we return the elements to their original position
                            if (invers) {
                                views.translationY = if (startBottomPrevious < keyboard) {
                                    (startBottomPrevious - keyboard).toFloat()
                                } else {
                                    0f
                                }
                            }

                            // We remember the initial state so that later we can return the view to the same place
                            if (!invers) {
                                startBottomPrevious = startBottom
                            }
                        }
                        return insets
                    }

                    override fun onEnd(animation: WindowInsetsAnimationCompat) {
                        if (views is RecyclerView) {
                            invers = !invers
                            animateImeForViews(views, InsetFocus.FOCUS_CHANGE)
                        } else {
                            invers = !invers
                            /*If the keyboard has finished its execution and is still visible on the screen,
                            we include the method in the focus change mode, so that the content shifts if the person
                            moves to a new element*/
                            animateImeForViews(views, InsetFocus.FOCUS_CHANGE)
                        }
                    }
                })
            }

            InsetFocus.FOCUS_CHANGE -> {
                views.viewTreeObserver.addOnGlobalFocusChangeListener(object :
                    ViewTreeObserver.OnGlobalFocusChangeListener {
                    init {
                        focus = this
                    }

                    override fun onGlobalFocusChanged(oldFocus: View?, newFocus: View?) {
                        context?.apply {
                            newFocus?.let { viewFocus ->
                                startBottom = calculateStartPosition(viewFocus.focusParent)
                                startRoot = calculateStartPosition(views)

                                if (startBottom < startRoot) {
                                    views.scrollY += startRoot - startBottom
                                    startBottom = calculateStartPosition(viewFocus.focusParent)
                                }
                                // Test logic may be improved it the future
                                if (startBottom < keyboard) {
                                    if (views is RecyclerView) {
                                        offset = if (startBottom < startRoot) {
                                            (startRoot - keyboard).toFloat()
                                        } else {
                                            (startBottom - keyboard).toFloat()
                                        }
                                        views.translationY += offset
                                    } else {
                                        offset = (startBottom - keyboard).toFloat()
                                        views.translationY += offset
                                    }
                                }
                            }
                        }
                    }
                })
            }
        }
    }

    /**
     * Offset reset
     */
    private fun resetOffset(): Float {
        return 0f
    }


    /**
     * Starting position calculation method
     */
    private fun calculateStartPosition(focus: View): Int {
        val screen = IntArray(2)
        focus.getLocationOnScreen(screen)
        return context?.let {
            ScreenManager.calculateRealHeight(it) - screen[1] - focus.height
        } ?: 0
    }

    /**
     * Offset calculation method
     */
    private fun calculateOffset(root: View, focus: View, keyboardSize: Int): Float {
        var offset = 0
        var start = calculateStartPosition(focus)
        val startRoot = calculateStartPosition(root)
        if (start < startRoot) {
            root.scrollY += startRoot - start
            start = calculateStartPosition(focus)
        }
        if (start < keyboardSize) {
            offset = start - keyboardSize
        }
        return offset.toFloat()
    }

    /**
     * Check if keyboard is 0
     */
    private fun Int.keyboardIsNull(): Boolean {
        return this == 0
    }

    override fun getViewForImeAnimation(): View? {
        return null
    }

    /**
     * Enum class responsible for switching modes
     */
    enum class InsetFocus {
        CURRENT_FOCUS, FOCUS_CHANGE
    }

    //==========================INLINE====================================

    inline fun switchToUIThread(crossinline action: () -> Unit) {
        activity?.runOnUiThread {
            action()
        }
    }

    inline fun applyOnContext(action: Context.() -> Unit) {
        context?.let(action)
    }

    //======================FRAGMENT BASE===========================

    override fun finishActivity() {
        context?.let { context ->
            if (context is Activity) {
                context.finishAndRemoveTask()
            }
        }
    }

    override fun hideSoftInput() {
        try {
            val imm = view?.context?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            imm?.hideSoftInputFromWindow(view?.windowToken, 0)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    open fun onRelease(){

    }

    override fun onDestroyView() {
        super.onDestroyView()
        onRelease()
        binding = null
        viewAnimateIme?.viewTreeObserver?.removeOnGlobalFocusChangeListener(focus)
        viewAnimateIme = null
    }
}


//TODO: separate interfaces
interface FragmentNavigationInterface {
    fun navigateToAnimated(navDirections: NavDirections)
    fun navigateToAnimated(@IdRes navigateToRes: Int, args: Bundle)
    fun navigateToAnimated(navDirections: NavDirections, @IdRes popUpToId: Int)
    fun navigateToAnimatedID(navigateToRes: Int, args: Bundle)
    fun navigateToAnimatedPop(navDirections: NavDirections)
    fun navigateToAnimatedPop(navigateToRes: Int, args: Bundle)
    fun navigateToAnimatedInclusive(navDirections: NavDirections, @IdRes popUpToId: Int)
    fun navigateToInclusive(navDirections: NavDirections, @IdRes popUpToId: Int)
    fun navigateTo(direction: NavDirections)
    fun navigateTo(@IdRes navigateToRes: Int)
    fun navigatePopBackStack()
    fun navigateUp()
    fun pop()
    fun popTo(@IdRes destinationId: Int, inclusive: Boolean = false)
    fun getNavigationResult(key: String = "result"): MutableLiveData<String>?
    fun setNavigationResult(result: String?, key: String = "result")
    fun findNavControllerSafely(): NavController?
    fun handleOnBackPressed(OnBackPressedHandle: (() -> Unit)? = null)
}

interface FragmentBaseInterface {
    fun finishActivity()
}

interface InsetsSupported {
    fun setupInset(inset: Insets)
    fun getViewForImeAnimation(): View?
}