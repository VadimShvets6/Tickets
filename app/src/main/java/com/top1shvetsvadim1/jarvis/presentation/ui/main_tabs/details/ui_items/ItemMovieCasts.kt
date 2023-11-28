package com.top1shvetsvadim1.jarvis.presentation.ui.main_tabs.details.ui_items

import android.view.LayoutInflater
import android.view.ViewGroup
import com.flexeiprata.novalles.annotations.BindViewHolder
import com.flexeiprata.novalles.annotations.Instruction
import com.flexeiprata.novalles.annotations.PrimaryTag
import com.flexeiprata.novalles.annotations.UIModel
import com.flexeiprata.novalles.interfaces.Instructor
import com.top1shvetsvadim1.jarvis.databinding.ItemMovieCastsBinding
import com.top1shvetsvadim1.jarvis.presentation.utils.recycler_utils.BaseUiModel
import com.top1shvetsvadim1.jarvis.presentation.utils.recycler_utils.DelegateAdapter
import com.top1shvetsvadim1.jarvis.presentation.utils.recycler_utils.DelegateViewHolder
import com.top1shvetsvadim1.jarvis.presentation.utils.recycler_utils.ItemSimpleDelegate

@UIModel
data class ItemMovieCasts(
    @PrimaryTag val tag: String,
    val listCasts: List<ItemCast>
) : BaseUiModel()

@BindViewHolder(ItemMoviesCastsDelegate.ItemMovieCastsViewHolder::class)
@Instruction(ItemMovieCasts::class)
class ItemMovieCastsInstructor : Instructor

@Suppress("unused")
class ItemMoviesCastsDelegate : ItemSimpleDelegate<ItemMovieCasts, ItemMoviesCastsDelegate.ItemMovieCastsViewHolder>(
    ItemMovieCasts::class
) {

    override fun createViewHolder(inflater: LayoutInflater, parent: ViewGroup): ItemMovieCastsViewHolder {
        return ItemMovieCastsViewHolder(ItemMovieCastsBinding.inflate(inflater, parent, false))
    }

    override fun provideInstructor(holder: ItemMovieCastsViewHolder, item: ItemMovieCasts, payload: Any): Instructor {
        return ItemMovieCastsInstructor()
    }

    inner class ItemMovieCastsViewHolder(private val binding: ItemMovieCastsBinding) :
        DelegateViewHolder<ItemMovieCasts>(binding) {

        private val adapter = DelegateAdapter.Builder()
            .setDelegates(ItemCastDelegate())
            .setActionProcessor(action)
            .build()

        init {
            binding.recycler.adapter = adapter
        }

        fun setListCasts(listCasts: List<ItemCast>) {
            adapter.submitList(listCasts.take(10))
        }

        override fun setOnClickListeners(item: ItemMovieCasts) {
            binding.viewAll.setOnClickListener {  }
        }
    }
}