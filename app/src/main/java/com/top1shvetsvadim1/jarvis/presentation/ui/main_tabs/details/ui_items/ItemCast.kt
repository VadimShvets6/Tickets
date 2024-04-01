package com.top1shvetsvadim1.jarvis.presentation.ui.main_tabs.details.ui_items

import android.view.LayoutInflater
import android.view.ViewGroup
import coil.load
import com.flexeiprata.novalles.annotations.BindViewHolder
import com.flexeiprata.novalles.annotations.Instruction
import com.flexeiprata.novalles.annotations.PrimaryTag
import com.flexeiprata.novalles.annotations.UIModel
import com.flexeiprata.novalles.interfaces.Instructor
import com.top1shvetsvadim1.jarvis.R
import com.top1shvetsvadim1.jarvis.databinding.ItemCastBinding
import com.top1shvetsvadim1.jarvis.presentation.utils.extentions.toImageUrl
import com.top1shvetsvadim1.jarvis.presentation.utils.recycler_utils.BaseUiModel
import com.top1shvetsvadim1.jarvis.presentation.utils.recycler_utils.DelegateViewHolder
import com.top1shvetsvadim1.jarvis.presentation.utils.recycler_utils.ItemSimpleDelegate

@UIModel
data class ItemCast(
    @PrimaryTag val tag: String,
    val name: String,
    val characterName: String,
    val image: String?
) : BaseUiModel()

@BindViewHolder(ItemCastDelegate.ItemCastViewHolder::class)
@Instruction(ItemCast::class)
class ItemCastInstructor : Instructor
@Suppress("unused")
class ItemCastDelegate : ItemSimpleDelegate<ItemCast, ItemCastDelegate.ItemCastViewHolder>(
    ItemCast::class
) {

    override fun createViewHolder(inflater: LayoutInflater, parent: ViewGroup): ItemCastViewHolder {
        return ItemCastViewHolder(ItemCastBinding.inflate(inflater, parent, false))
    }

    override fun provideInstructor(holder: ItemCastViewHolder, item: ItemCast, payload: Any): Instructor {
        return ItemCastInstructor()
    }

    inner class ItemCastViewHolder(private val binding: ItemCastBinding) : DelegateViewHolder<ItemCast>(binding) {
        fun setName(name: String) {
            binding.name.text = name
        }

        fun setCharacterName(characterName: String) {
            binding.character.text = characterName
        }

        fun setImage(image: String?) {
            binding.posterImage.load(image?.toImageUrl()){
                placeholder(R.drawable.movie_poster_placeholder)
            }
        }
    }
}