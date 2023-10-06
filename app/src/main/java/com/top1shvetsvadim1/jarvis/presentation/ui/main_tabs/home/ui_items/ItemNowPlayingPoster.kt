package com.top1shvetsvadim1.jarvis.presentation.ui.main_tabs.home.ui_items

import android.view.LayoutInflater
import android.view.ViewGroup
import coil.load
import com.flexeiprata.novalles.annotations.BindViewHolder
import com.flexeiprata.novalles.annotations.Instruction
import com.flexeiprata.novalles.annotations.NonUIProperty
import com.flexeiprata.novalles.annotations.PrimaryTag
import com.flexeiprata.novalles.annotations.UIModel
import com.flexeiprata.novalles.interfaces.Instructor
import com.top1shvetsvadim1.jarvis.databinding.ItemNowPlyaingPosterBinding
import com.top1shvetsvadim1.jarvis.presentation.utils.extentions.toImageUrl
import com.top1shvetsvadim1.jarvis.presentation.utils.recycler_utils.BaseUiModel
import com.top1shvetsvadim1.jarvis.presentation.utils.recycler_utils.DelegateViewHolder
import com.top1shvetsvadim1.jarvis.presentation.utils.recycler_utils.ItemSimpleDelegate

@UIModel
data class ItemNowPlayingPoster(
    @PrimaryTag val tag: String,
    val poster: String,
    val voteAverage: Double,
    @NonUIProperty val titleMovie: String,
    @NonUIProperty val genres: String
) : BaseUiModel()

@BindViewHolder(ItemNowPlayingPosterDelegate.ItemNowPlayingPosterViewHolder::class)
@Instruction(ItemNowPlayingPoster::class)
class ItemNowPlayingPosterInstructor : Instructor

@Suppress("unused")
class ItemNowPlayingPosterDelegate :
    ItemSimpleDelegate<ItemNowPlayingPoster, ItemNowPlayingPosterDelegate.ItemNowPlayingPosterViewHolder>(
        ItemNowPlayingPoster::class
    ) {

    override fun createViewHolder(inflater: LayoutInflater, parent: ViewGroup): ItemNowPlayingPosterViewHolder {
        return ItemNowPlayingPosterViewHolder(ItemNowPlyaingPosterBinding.inflate(inflater, parent, false))
    }

    override fun provideInstructor(
        holder: ItemNowPlayingPosterViewHolder,
        item: ItemNowPlayingPoster,
        payload: Any
    ): Instructor {
        return ItemNowPlayingPosterInstructor()
    }

    inner class ItemNowPlayingPosterViewHolder(private val binding: ItemNowPlyaingPosterBinding) :
        DelegateViewHolder<ItemNowPlayingPoster>(binding) {

        fun setPoster(poster: String) {
            binding.posterImage.load(poster.toImageUrl())
        }

        fun setVoteAverage(voteAverage: Double) {
            binding.ratingCount.text = voteAverage.toString()
        }
    }
}