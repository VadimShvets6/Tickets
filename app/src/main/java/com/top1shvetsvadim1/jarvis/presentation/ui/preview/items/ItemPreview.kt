package com.top1shvetsvadim1.jarvis.presentation.ui.preview.items

import android.view.LayoutInflater
import android.view.ViewGroup
import com.flexeiprata.novalles.annotations.BindViewHolder
import com.flexeiprata.novalles.annotations.Instruction
import com.flexeiprata.novalles.annotations.PrimaryTag
import com.flexeiprata.novalles.annotations.UIModel
import com.flexeiprata.novalles.interfaces.Instructor
import com.top1shvetsvadim1.jarvis.common.Image
import com.top1shvetsvadim1.jarvis.common.Text
import com.top1shvetsvadim1.jarvis.common.getStringText
import com.top1shvetsvadim1.jarvis.common.loadImage
import com.top1shvetsvadim1.jarvis.databinding.ItemPreviewBinding
import com.top1shvetsvadim1.jarvis.presentation.utils.recycler_utils.BaseUiModel
import com.top1shvetsvadim1.jarvis.presentation.utils.recycler_utils.DelegateViewHolder
import com.top1shvetsvadim1.jarvis.presentation.utils.recycler_utils.ItemSimpleDelegate

@UIModel
data class ItemPreviewUIModel(
    @PrimaryTag val tag: String,
    val image: Image,
    val title: Text,
    val description: Text
) : BaseUiModel()

@BindViewHolder(ItemPreviewDelegate.ItemPreviewViewHolder::class)
@Instruction(ItemPreviewUIModel::class)
class ItemPreviewInstruction : Instructor

@Suppress("unused")
class ItemPreviewDelegate : ItemSimpleDelegate<ItemPreviewUIModel, ItemPreviewDelegate.ItemPreviewViewHolder>(
    ItemPreviewUIModel::class
) {

    override fun createViewHolder(inflater: LayoutInflater, parent: ViewGroup): ItemPreviewViewHolder {
        return ItemPreviewViewHolder(ItemPreviewBinding.inflate(inflater, parent, false))
    }

    override fun provideInstructor(holder: ItemPreviewViewHolder, item: ItemPreviewUIModel, payload: Any): Instructor {
        return ItemPreviewInstruction()
    }

    inner class ItemPreviewViewHolder(private val binding: ItemPreviewBinding) :
        DelegateViewHolder<ItemPreviewUIModel>(binding) {

        fun setImage(image: Image) {
            image.loadImage(binding.image)
        }

        fun setTitle(title: Text) {
            binding.title.text = title.getStringText(context)
        }

        fun setDescription(description: Text) {
            binding.description.text = description.getStringText(context)
        }
    }
}