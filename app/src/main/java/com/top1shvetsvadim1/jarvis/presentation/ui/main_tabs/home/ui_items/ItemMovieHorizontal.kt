package com.top1shvetsvadim1.jarvis.presentation.ui.main_tabs.home.ui_items

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import coil.load
import com.flexeiprata.novalles.annotations.BindViewHolder
import com.flexeiprata.novalles.annotations.Instruction
import com.flexeiprata.novalles.annotations.NonUIProperty
import com.flexeiprata.novalles.annotations.PrimaryTag
import com.flexeiprata.novalles.annotations.UIModel
import com.flexeiprata.novalles.interfaces.Instructor
import com.top1shvetsvadim1.jarvis.R
import com.top1shvetsvadim1.jarvis.databinding.ItemMovieHorizontalBinding
import com.top1shvetsvadim1.jarvis.presentation.utils.extentions.roundToTwoDecimal
import com.top1shvetsvadim1.jarvis.presentation.utils.extentions.toImageUrl
import com.top1shvetsvadim1.jarvis.presentation.utils.recycler_utils.Action
import com.top1shvetsvadim1.jarvis.presentation.utils.recycler_utils.BaseUiModel
import com.top1shvetsvadim1.jarvis.presentation.utils.recycler_utils.DelegateViewHolder
import com.top1shvetsvadim1.jarvis.presentation.utils.recycler_utils.ItemSimpleDelegate

@UIModel
data class ItemMovieHorizontal(
    @PrimaryTag val tag: String,
    val title: String,
    val voteAverage: Double,
    val posterImage: String,
    val releaseData: String = "",
    val isUpcoming: Boolean = false,
    @NonUIProperty val id: Int
) : BaseUiModel()

@BindViewHolder(ItemMovieHorizontalDelegate.ItemMovieHorizontalViewHolder::class)
@Instruction(ItemMovieHorizontal::class)
class ItemMovieHorizontalInstructor : Instructor

@Suppress("unused")
class ItemMovieHorizontalDelegate :
    ItemSimpleDelegate<ItemMovieHorizontal, ItemMovieHorizontalDelegate.ItemMovieHorizontalViewHolder>(
        ItemMovieHorizontal::class
    ) {

    override fun createViewHolder(inflater: LayoutInflater, parent: ViewGroup): ItemMovieHorizontalViewHolder {
        return ItemMovieHorizontalViewHolder(ItemMovieHorizontalBinding.inflate(inflater))
    }

    override fun provideInstructor(
        holder: ItemMovieHorizontalViewHolder,
        item: ItemMovieHorizontal,
        payload: Any
    ): Instructor {
        return ItemMovieHorizontalInstructor()
    }

    inner class ItemMovieHorizontalViewHolder(private val binding: ItemMovieHorizontalBinding) :
        DelegateViewHolder<ItemMovieHorizontal>(binding) {

        fun setTitle(title: String) {
            binding.title.text = title
        }

        fun setVoteAverage(voteAverage: Double) {
            binding.rating.text = String.format("%s/10 IMBd", voteAverage.roundToTwoDecimal().toString())
        }

        fun setPosterImage(posterImage: String) {
            binding.posterImage.load(posterImage.toImageUrl()) {
                placeholder(R.drawable.movie_poster_placeholder)
            }
        }

        fun setReleaseData(releaseData: String) {
            binding.releaseData.text = String.format("%s: %s", context.getString(R.string.key_premiere), releaseData)
        }

        fun setIsUpcoming(isUpcoming: Boolean) {
            binding.rating.isVisible = !isUpcoming
            binding.releaseData.isVisible = isUpcoming
        }

        override fun setOnClickListeners(item: ItemMovieHorizontal) {
            binding.root.setOnClickListener {
                pushAction(ItemMovieHorizontalActions.OnClickMovie(item.id))
            }
        }
    }

    sealed interface ItemMovieHorizontalActions : Action {
        data class OnClickMovie(val id: Int) : ItemMovieHorizontalActions
    }
}