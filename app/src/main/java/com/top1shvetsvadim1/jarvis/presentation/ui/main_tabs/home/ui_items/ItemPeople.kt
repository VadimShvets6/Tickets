package com.top1shvetsvadim1.jarvis.presentation.ui.main_tabs.home.ui_items

import android.view.LayoutInflater
import android.view.ViewGroup
import coil.load
import com.flexeiprata.novalles.annotations.BindViewHolder
import com.flexeiprata.novalles.annotations.Instruction
import com.flexeiprata.novalles.annotations.PrimaryTag
import com.flexeiprata.novalles.annotations.UIModel
import com.flexeiprata.novalles.interfaces.Instructor
import com.top1shvetsvadim1.jarvis.R
import com.top1shvetsvadim1.jarvis.databinding.ItemPeopleBinding
import com.top1shvetsvadim1.jarvis.presentation.utils.extentions.toImageAvatarUrl
import com.top1shvetsvadim1.jarvis.presentation.utils.extentions.toImageUrl
import com.top1shvetsvadim1.jarvis.presentation.utils.recycler_utils.BaseUiModel
import com.top1shvetsvadim1.jarvis.presentation.utils.recycler_utils.DelegateViewHolder
import com.top1shvetsvadim1.jarvis.presentation.utils.recycler_utils.ItemSimpleDelegate

@UIModel
data class ItemPeople(
    @PrimaryTag val tag: String,
    val name: String,
    val position: Int,
    val image: String
) : BaseUiModel()

@BindViewHolder(ItemPeopleDelegate.ItemPeopleViewHolder::class)
@Instruction(ItemPeople::class)
class ItemPeopleInstructor : Instructor

@Suppress("unused")
class ItemPeopleDelegate : ItemSimpleDelegate<ItemPeople, ItemPeopleDelegate.ItemPeopleViewHolder>(
    ItemPeople::class
) {

    override fun createViewHolder(inflater: LayoutInflater, parent: ViewGroup): ItemPeopleViewHolder {
        return ItemPeopleViewHolder(ItemPeopleBinding.inflate(inflater))
    }

    override fun provideInstructor(holder: ItemPeopleViewHolder, item: ItemPeople, payload: Any): Instructor {
        return ItemPeopleInstructor()
    }

    inner class ItemPeopleViewHolder(private val binding: ItemPeopleBinding) : DelegateViewHolder<ItemPeople>(binding) {

        fun setName(name: String) {
            binding.name.text = name
        }

        fun setPosition(position: Int) {
            binding.position.text = String.format("%s #%s", "Позиция", position.inc().toString())
            binding.mainPosition.text = position.inc().toString()
        }

        fun setImage(image: String) {
            binding.image.load(image.toImageUrl()) {
                placeholder(R.drawable.movie_poster_placeholder)
            }
        }
    }
}