package com.top1shvetsvadim1.jarvis.presentation.ui.preview.items

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.FrameLayout.LayoutParams
import com.flexeiprata.novalles.annotations.BindViewHolder
import com.flexeiprata.novalles.annotations.Instruction
import com.flexeiprata.novalles.annotations.PrimaryTag
import com.flexeiprata.novalles.annotations.UIModel
import com.flexeiprata.novalles.interfaces.Instructor
import com.top1shvetsvadim1.jarvis.databinding.ItemPreviewBaseBinding
import com.top1shvetsvadim1.jarvis.presentation.utils.recycler_utils.BaseUiModel
import com.top1shvetsvadim1.jarvis.presentation.utils.recycler_utils.DelegateViewHolder
import com.top1shvetsvadim1.jarvis.presentation.utils.recycler_utils.ItemSimpleDelegate

@UIModel
data class ItemPreviewUIModel(
    @PrimaryTag val tag: String,
    val viewResourceID: Int,
    val viewConfiguration: ProViewConfigurator,
    val viewLayoutParams: FrameLayout.LayoutParams
) : BaseUiModel()

fun interface ProViewConfigurator {
    fun configureView(view: View)
}

@BindViewHolder(ItemPreviewDelegate.ItemPreviewViewHolder::class)
@Instruction(ItemPreviewUIModel::class)
class ItemPreviewInstruction : Instructor

@Suppress("unused")
class ItemPreviewDelegate : ItemSimpleDelegate<ItemPreviewUIModel, ItemPreviewDelegate.ItemPreviewViewHolder>(
    ItemPreviewUIModel::class
) {

    override fun createViewHolder(inflater: LayoutInflater, parent: ViewGroup): ItemPreviewViewHolder {
        return ItemPreviewViewHolder(ItemPreviewBaseBinding.inflate(inflater, parent, false))
    }

    override fun provideInstructor(holder: ItemPreviewViewHolder, item: ItemPreviewUIModel, payload: Any): Instructor {
        return ItemPreviewInstruction()
    }

    inner class ItemPreviewViewHolder(private val binding: ItemPreviewBaseBinding) :
        DelegateViewHolder<ItemPreviewUIModel>(binding) {

        private var viewForInflater: View? = null
        fun setViewResourceID(viewResourceID: Int) {
            binding.root.removeAllViews()
            viewForInflater = LayoutInflater.from(context).inflate(viewResourceID, binding.containerPreview, false)
            binding.containerPreview.addView(viewForInflater)
        }

        fun setViewConfiguration(viewConfiguration: ProViewConfigurator) {
            viewForInflater?.let { viewConfiguration.configureView(it) }
        }

        fun setViewLayoutParams(viewLayoutParams: LayoutParams) {
            viewForInflater?.let {
                it.layoutParams = viewLayoutParams
            }
        }
        /*fun setImage(image: Image) {
            image.loadImage(binding.image)
        }

        fun setTitle(title: Text) {
            binding.title.text = title.getStringText(context)
        }

        fun setDescription(description: Text) {
            binding.description.text = description.getStringText(context)
        }*/
    }
}