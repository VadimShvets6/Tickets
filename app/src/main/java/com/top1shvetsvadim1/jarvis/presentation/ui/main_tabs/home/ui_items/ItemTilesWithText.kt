package com.top1shvetsvadim1.jarvis.presentation.ui.main_tabs.home.ui_items

import android.view.LayoutInflater
import android.view.ViewGroup
import com.flexeiprata.novalles.annotations.BindViewHolder
import com.flexeiprata.novalles.annotations.Instruction
import com.flexeiprata.novalles.annotations.PrimaryTag
import com.flexeiprata.novalles.annotations.UIModel
import com.flexeiprata.novalles.interfaces.Instructor
import com.top1shvetsvadim1.jarvis.databinding.ItemTilesWithTextBinding
import com.top1shvetsvadim1.jarvis.presentation.utils.extentions.capitalizeFirst
import com.top1shvetsvadim1.jarvis.presentation.utils.recycler_utils.Action
import com.top1shvetsvadim1.jarvis.presentation.utils.recycler_utils.BaseUiModel
import com.top1shvetsvadim1.jarvis.presentation.utils.recycler_utils.DelegateViewHolder
import com.top1shvetsvadim1.jarvis.presentation.utils.recycler_utils.ItemSimpleDelegate

@UIModel
data class ItemTilesWithText(
    @PrimaryTag val tag: String,
    val title: String
) : BaseUiModel()

@BindViewHolder(ItemTilesWithTextDelegate.ItemTilesWithTextViewHolder::class)
@Instruction(ItemTilesWithText::class)
class ItemTilesWithTextInstructor : Instructor

@Suppress("unused")
class ItemTilesWithTextDelegate :
    ItemSimpleDelegate<ItemTilesWithText, ItemTilesWithTextDelegate.ItemTilesWithTextViewHolder>(
        ItemTilesWithText::class
    ) {

    override fun createViewHolder(inflater: LayoutInflater, parent: ViewGroup): ItemTilesWithTextViewHolder {
        return ItemTilesWithTextViewHolder(ItemTilesWithTextBinding.inflate(inflater))
    }

    override fun provideInstructor(
        holder: ItemTilesWithTextViewHolder,
        item: ItemTilesWithText,
        payload: Any
    ): Instructor {
        return ItemTilesWithTextInstructor()
    }

    inner class ItemTilesWithTextViewHolder(private val binding: ItemTilesWithTextBinding) :
        DelegateViewHolder<ItemTilesWithText>(binding) {

        fun setTitle(title: String) {
            binding.title.text = title.capitalizeFirst()
        }

        override fun setOnClickListeners(item: ItemTilesWithText) {
            binding.title.setOnClickListener {
                pushAction(ItemTilesWithTextAction.OnClick)
            }
        }
    }

    sealed interface ItemTilesWithTextAction : Action {
        object OnClick : ItemTilesWithTextAction
    }
}
