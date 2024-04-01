package com.top1shvetsvadim1.jarvis.presentation.ui.main_tabs.details.ui_items

import android.text.SpannableString
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.flexeiprata.novalles.annotations.BindViewHolder
import com.flexeiprata.novalles.annotations.Instruction
import com.flexeiprata.novalles.annotations.PrimaryTag
import com.flexeiprata.novalles.annotations.UIModel
import com.flexeiprata.novalles.interfaces.Instructor
import com.top1shvetsvadim1.jarvis.common.Text
import com.top1shvetsvadim1.jarvis.common.getStringText
import com.top1shvetsvadim1.jarvis.databinding.ItemRatingBinding
import com.top1shvetsvadim1.jarvis.presentation.utils.recycler_utils.BaseUiModel
import com.top1shvetsvadim1.jarvis.presentation.utils.recycler_utils.DelegateViewHolder
import com.top1shvetsvadim1.jarvis.presentation.utils.recycler_utils.ItemSimpleDelegate

@UIModel
data class ItemRating(
    @PrimaryTag val tag: String,
    val rating: SpannableString,
    val voteCount: Text,
) : BaseUiModel()

@BindViewHolder(ItemRatingDelegate.ItemRatingViewHolder::class)
@Instruction(ItemRating::class)
class ItemRatingInstruction : Instructor
class ItemRatingDelegate : ItemSimpleDelegate<ItemRating, ItemRatingDelegate.ItemRatingViewHolder>(
    ItemRating::class
) {
    override fun createViewHolder(inflater: LayoutInflater, parent: ViewGroup): ItemRatingViewHolder {
        return ItemRatingViewHolder(ItemRatingBinding.inflate(inflater, parent, false))
    }

    override fun provideInstructor(holder: ItemRatingViewHolder, item: ItemRating, payload: Any): Instructor {
        return ItemRatingInstruction()
    }

    inner class ItemRatingViewHolder(private val binding: ItemRatingBinding) : DelegateViewHolder<ItemRating>(binding) {

        fun setRating(rating: SpannableString) {
            binding.rating.text = rating
            binding.rating.isVisible = rating.isNotBlank()
        }

        fun setVoteCount(voteCount: Text){
            binding.votes.text = voteCount.getStringText(context)
        }

        override fun setOnClickListeners(item: ItemRating) {
            binding.rate.setOnClickListener {

            }
        }
    }
}
