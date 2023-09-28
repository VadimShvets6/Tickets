package com.top1shvetsvadim1.jarvis.presentation.utils.views

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.annotation.DrawableRes
import androidx.annotation.IdRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.graphics.Insets
import androidx.core.view.forEachIndexed
import androidx.core.view.updatePadding
import com.example.coreutills.extentions.animateIsVisible
import com.example.coreutills.factories.AnimatorFactory
import com.example.coreutills.wrappers.JobWrapper
import com.top1shvetsvadim1.jarvis.R
import com.top1shvetsvadim1.jarvis.databinding.HubBottomNavigationBinding
import com.top1shvetsvadim1.jarvis.databinding.ViewHubItemNavigationBinding
import com.top1shvetsvadim1.jarvis.presentation.utils.extentions.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob


class HubBottomNavigation @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null,
) : ConstraintLayout(context, attrs) {

    private val scope: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)
    private val jobWrapper = JobWrapper()

    private val tabs = mutableListOf<Tab>()
    private val binding = HubBottomNavigationBinding.inflate(LayoutInflater.from(context), this, true)

    private var currentSelectedTabAsView: View? = null
    private var activeTab: Int = 0

    var onTabChanged: (Int) -> Unit = {}

    private fun getTextAnimator(view: View, isInverse: Boolean) = AnimatorFactory.createSimpleFloatFactory(
        view,
        component = if (isInverse) AnimatorFactory.inversePercentAnimator else AnimatorFactory.percentAnimator,
        duration = 200,
        interpolator = AccelerateDecelerateInterpolator()
    ) { view, percent ->
        view.scaleX = percent
        view.scaleY = percent
        view.animateIsVisible(percent != 0f)
        /* view.updateLayoutParams {
             width = (84.dp * percent).toInt()
         }*/
    }

    private fun setTintColor(view: View) {

    }


    private fun setupTabs() {
        binding.mainContainer.removeAllViews()
        tabs.map { tab ->
            LayoutInflater.from(context).inflate(R.layout.view_hub_item_navigation, binding.mainContainer, false)
                .apply {
                    val bindingItem = ViewHubItemNavigationBinding.bind(this)
                    bindingItem.image.setImageResource(tab.icon)
                    // bindingItem.image.setTintColorOf(ThemeManager.currentTheme.primaryVariant)
                    bindingItem.text.text = tab.text
                    setOnClickListener {
                        if (currentSelectedTabAsView != it) {
                            currentSelectedTabAsView?.let {
                                val currentBinding = ViewHubItemNavigationBinding.bind(it)
                                getTextAnimator(currentBinding.text, true).buildAndStart()
                                //getTintAnimator(currentBinding.text, true).buildAndStart()
                                //getTintAnimator(currentBinding.image, true).buildAndStart()
                                currentBinding.bg.background = null
                                currentBinding.image.setColorFilter(
                                    ContextCompat.getColor(
                                        context,
                                        R.color.color_737373
                                    )
                                )
                                currentBinding.text.setTextColor(ContextCompat.getColor(context, R.color.white))
                            }
                            currentSelectedTabAsView = it
                            activeTab = binding.mainContainer.indexOfChild(it)
                            onTabChanged(tabs[activeTab].id)
                            currentSelectedTabAsView?.let {
                                val currentBinding = ViewHubItemNavigationBinding.bind(it)
                                getTextAnimator(currentBinding.text, false).buildAndStart()
                                //getTintAnimator(currentBinding.text, false).buildAndStart()
                                //getTintAnimator(currentBinding.image, false).buildAndStart()
                                currentBinding.bg.background =
                                    ContextCompat.getDrawable(context, R.drawable.rectangele_bottom_ripple)
                                currentBinding.image.setColorFilter(ContextCompat.getColor(context, R.color.white))
                                currentBinding.text.setTextColor(ContextCompat.getColor(context, R.color.white))
                            }
                        }
                    }
                }
        }.forEach {
            binding.mainContainer.addView(it)
        }

    }

    fun consumeTabs(tabs: List<Tab>) {
        this.tabs.apply {
            clear()
            addAll(tabs)
        }
        setupTabs()
        applyTabsParams()
    }

    private fun applyTabsParams() {
        binding.mainContainer.forEachIndexed { index, view ->
            if (index == activeTab) {
                Log.d("Deb", "Tab active index $index")
                setTabActive(view)
            } else {
                setTabInactive(view)
            }
        }
    }

    private fun setTabActive(tabView: View) {
        val binding = ViewHubItemNavigationBinding.bind(tabView)
        currentSelectedTabAsView = tabView
        binding.root.post {
            binding.image.setColorFilter(ContextCompat.getColor(context, R.color.white))
            binding.text.apply {
                setTextColor(ContextCompat.getColor(context, R.color.white)) // TODO CHANGE TO TYPEFACE
            }
            binding.bg.background =
                ContextCompat.getDrawable(context, R.drawable.rectangele_bottom_ripple)
            getTextAnimator(binding.text, false).buildAndStart()
            // getTintAnimator(binding.text, false).buildAndStart()
            //getTintAnimator(binding.image, false).buildAndStart()
            /* binding.bg.alpha = 1f
             binding.bg.updateLayoutParams {
                 width = 84.dp.toInt()
             }*/
        }
    }

    private fun setTabInactive(tabView: View) {
        val binding = ViewHubItemNavigationBinding.bind(tabView)
        binding.root.post {

            binding.text.apply {
                setTextColor(ContextCompat.getColor(context, R.color.black))
            }
            getTextAnimator(binding.text, true)
            //getTintAnimator(binding.image, true).buildAndStart()

            // binding.bg.alpha = 0f
            /* binding.image.updateLayoutParams {
                 width = 50
                     height =  50
             }*/
        }
    }

    fun consumeInsets(insets: Insets) {
        binding.mainContainer.updatePadding(
            bottom = (insets.bottom + 12.dp).toInt()
        )
    }

    fun queryTabs(onTab: (Tab) -> Boolean) {
        /*tabs.forEachIndexed { index, tab ->
            if (onTab(tab)) {
                setTabActive(binding.mainContainer.getChildAt(index))
            } else {
                setTabInactive(binding.mainContainer.getChildAt(index))
            }
        }*/

    }

    data class Tab(
        @IdRes val id: Int,
        @DrawableRes val icon: Int,
        val text: String
    ) {
        /*constructor(
            context: Context
            @IdRes val id: Int,
            @DrawableRes val icon: Int,
            @StringRes val text: Int
        ): super(
            id
        )*/
    }
}