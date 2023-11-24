package com.top1shvetsvadim1.jarvis.presentation.ui.main_tabs.details.ui_items

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.view.animation.LinearInterpolator
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.animation.doOnEnd
import androidx.core.view.isVisible
import androidx.core.view.updateLayoutParams
import androidx.core.view.updatePadding
import com.example.coreutills.extentions.screenHeight
import com.example.coreutills.factories.AnimatorFactory
import com.example.coreutills.managers.AnimationsManager
import com.example.coreutills.utils.toArithmetic
import com.flexeiprata.novalles.annotations.BindViewHolder
import com.flexeiprata.novalles.annotations.Instruction
import com.flexeiprata.novalles.annotations.PrimaryTag
import com.flexeiprata.novalles.annotations.UIModel
import com.flexeiprata.novalles.interfaces.Instructor
import com.top1shvetsvadim1.jarvis.common.Text
import com.top1shvetsvadim1.jarvis.common.getStringText
import com.top1shvetsvadim1.jarvis.databinding.ItemMoviesDescriptionBinding
import com.top1shvetsvadim1.jarvis.presentation.utils.experemental.sdp
import com.top1shvetsvadim1.jarvis.presentation.utils.recycler_utils.BaseUiModel
import com.top1shvetsvadim1.jarvis.presentation.utils.recycler_utils.DelegateViewHolder
import com.top1shvetsvadim1.jarvis.presentation.utils.recycler_utils.ItemSimpleDelegate
import kotlin.math.sqrt

@UIModel
data class ItemDescription(
    @PrimaryTag val tag: String,
    val title: Text,
    val description: String,
    val isEmptyOverview: Boolean
) : BaseUiModel()

@BindViewHolder(ItemDescriptionDelegate.ItemDescriptionViewHolder::class)
@Instruction(ItemDescription::class)
class ItemDescriptionInstructor : Instructor
@Suppress("unused")
class ItemDescriptionDelegate :
    ItemSimpleDelegate<ItemDescription, ItemDescriptionDelegate.ItemDescriptionViewHolder>(
        ItemDescription::class
    ) {

    override fun createViewHolder(
        inflater: LayoutInflater,
        parent: ViewGroup
    ): ItemDescriptionViewHolder {
        return ItemDescriptionViewHolder(
            ItemMoviesDescriptionBinding.inflate(
                inflater,
                parent,
                false
            )
        )
    }

    override fun provideInstructor(
        holder: ItemDescriptionViewHolder,
        item: ItemDescription,
        payload: Any
    ): Instructor {
        return ItemDescriptionInstructor()
    }

    inner class ItemDescriptionViewHolder(private val binding: ItemMoviesDescriptionBinding) :
        DelegateViewHolder<ItemDescription>(binding) {

        fun setTitle(title: Text) {
            binding.title.text = title.getStringText(context)
        }

        fun setDescription(description: String) {
            binding.description.text = description
        }

        fun setIsEmptyOverview(isEmptyOverview: Boolean){
            binding.emptyOverview.isVisible = isEmptyOverview
            binding.readMore.isVisible = !isEmptyOverview
            binding.containerImage.isVisible = isEmptyOverview
        }

        override fun setOnClickListeners(item: ItemDescription) {
            binding.readMore.setOnClickListener {
                binding.description.updateLayoutParams<ConstraintLayout.LayoutParams> {
                    height = binding.title.height
                }
                binding.description.maxLines = 100
                binding.description.measure(
                    View.MeasureSpec.makeMeasureSpec(context.screenHeight - 48.sdp, View.MeasureSpec.AT_MOST),
                    ConstraintLayout.LayoutParams.WRAP_CONTENT
                )
                val measurements = binding.description.measuredHeight
                AnimatorFactory.startSimpleFactory(
                    view = binding.description,
                    component = AnimatorFactory.AnimatorComponent(binding.description.height, measurements * 2),
                    duration = 400,
                    interpolator = LinearInterpolator(),
                    postConfiguration = {
                        doOnEnd {
                            binding.description.updateLayoutParams<ConstraintLayout.LayoutParams> {
                                height = ConstraintLayout.LayoutParams.WRAP_CONTENT
                            }
                            binding.description.updatePadding(bottom = 8.sdp)
                            AnimationsManager.visibilityComplexAnimationLinear(binding.readMore, false)
                        }
                    }
                ) { view, value ->
                    view.updateLayoutParams<ConstraintLayout.LayoutParams> {
                        height = value
                    }
                }
            }

            binding.containerImage.setOnClickListener {
                it.animateFunDrag()
            }
        }

        private fun View.animateFunDrag() {
            post {
                pivotY = height.toFloat()
                AnimatorFactory.createSimpleFloatFactory(
                    this,
                    component = AnimatorFactory.AnimatorComponent(0f, -10f, 6f, -6f, 7f, 0f),
                    duration = 1000,
                    postConfiguration = {
                        startDelay = 200
                    },
                    interpolator = DecelerateInterpolator()
                ) { view, value ->
                    view.rotation = value
                }.buildAndStart()
            }
        }
    }
}