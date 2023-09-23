package com.top1shvetsvadim1.jarvis.presentation.ui.preview

import android.animation.Animator
import android.animation.ValueAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.graphics.Insets
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.example.coreutills.factories.AnimatorFactory
import com.example.coreutills.managers.ScreenManager
import com.top1shvetsvadim1.jarvis.databinding.FragmentWelcomeBinding
import com.top1shvetsvadim1.jarvis.presentation.base.FragmentBaseMVI
import com.top1shvetsvadim1.jarvis.presentation.ui.preview.items.ItemPreviewDelegate
import com.top1shvetsvadim1.jarvis.presentation.utils.extentions.launchUI
import com.top1shvetsvadim1.jarvis.presentation.utils.recycler_utils.DelegateAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.ensureActive

@AndroidEntryPoint
class FragmentWelcome : FragmentBaseMVI<FragmentWelcomeBinding, WelcomeState, WelcomeEvent, WelcomeViewModel>() {

    override val viewModel: WelcomeViewModel by viewModels()

    private val adapterPreview = DelegateAdapter.Builder()
        .setDelegates(ItemPreviewDelegate())
        .build()

    private var autoScrollJob: Job? = null

    private val viewPagerCallback = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageScrollStateChanged(state: Int) {
            super.onPageScrollStateChanged(state)
            if (state == ViewPager2.SCROLL_STATE_SETTLING) {
                stopAutoScroll()
            }
        }
    }


    override fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentWelcomeBinding {
        return FragmentWelcomeBinding.inflate(inflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.handleIntent(WelcomeIntent.LoadItems)
    }

    override fun render(state: WelcomeState) {
        adapterPreview.submitList(state.items)
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
            viewPager.apply {
                adapter = adapterPreview
                offscreenPageLimit = 3
                indicator.attachTo(this)
                startAutoScroll()
                registerViewPagerCallbacks()
            }
        }
    }

    private fun registerViewPagerCallbacks() {
        requireBinding().viewPager.registerOnPageChangeCallback(viewPagerCallback)
    }

    private fun unRegisterViewPagerCallbacks() {
        requireBinding().viewPager.unregisterOnPageChangeCallback(viewPagerCallback)
    }

    private fun stopAutoScroll() {
        autoScrollJob?.cancel()
    }

    private fun startAutoScroll() {
        var animator: ValueAnimator?
        var previousValue = 0
        var offsetToDrag = 0
        applyOnBinding {
            animator = AnimatorFactory(
                view = viewPager,
                component = AnimatorFactory.AnimatorComponent(animatorValues = arrayOf(previousValue, offsetToDrag)),
                type = AnimatorFactory.AnimatorType(type = Int::class),
                instruction = AnimatorFactory.AnimationInstruction(onChange = { _, value ->
                    val currentPxToDrag = (value - previousValue).toFloat()
                    viewPager.fakeDragBy(-currentPxToDrag)
                    previousValue = value
                }),
                configuration = AnimatorFactory.AnimationConfiguration(
                    duration = 600,
                    interpolator = AccelerateDecelerateInterpolator(),
                    postConfiguration = {
                        addListener(object : Animator.AnimatorListener {
                            override fun onAnimationStart(p0: Animator) {
                                viewPager.beginFakeDrag()
                            }

                            override fun onAnimationEnd(p0: Animator) {
                                viewPager.endFakeDrag()
                            }

                            override fun onAnimationCancel(p0: Animator) {}
                            override fun onAnimationRepeat(p0: Animator) {}
                        })
                    })
            ).build()
            autoScrollJob = viewLifecycleOwner.lifecycleScope.launchUI {
                while (true) {
                    ensureActive()
                    delay(3000)
                    offsetToDrag += viewPager.width
                    if (viewPager.adapter?.itemCount?.minus(1) == viewPager.currentItem) {
                        offsetToDrag = 0
                    }
                    animator?.setIntValues(previousValue, offsetToDrag)
                    animator?.start()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        stopAutoScroll()
        unRegisterViewPagerCallbacks()
    }
}