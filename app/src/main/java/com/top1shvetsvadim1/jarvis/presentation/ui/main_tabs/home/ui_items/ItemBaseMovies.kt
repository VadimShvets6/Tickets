package com.top1shvetsvadim1.jarvis.presentation.ui.main_tabs.home.ui_items

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
import com.top1shvetsvadim1.jarvis.databinding.ItemPopularMoviesBinding
import com.top1shvetsvadim1.jarvis.presentation.utils.recycler_utils.BaseUiModel
import com.top1shvetsvadim1.jarvis.presentation.utils.recycler_utils.DelegateAdapter
import com.top1shvetsvadim1.jarvis.presentation.utils.recycler_utils.DelegateViewHolder
import com.top1shvetsvadim1.jarvis.presentation.utils.recycler_utils.ItemSimpleDelegate

@UIModel
data class ItemBaseMovies(
    @PrimaryTag val tag: String,
    val title: Text,
    val listItems: List<BaseUiModel>,
    val isSeeAllVisible: Boolean = true
) : BaseUiModel()

@BindViewHolder(ItemBaseMoviesDelegate.ItemBaseMoviesViewHolder::class)
@Instruction(ItemBaseMovies::class)
class ItemBaseMoviesInstructor : Instructor

class ItemBaseMoviesDelegate :
    ItemSimpleDelegate<ItemBaseMovies, ItemBaseMoviesDelegate.ItemBaseMoviesViewHolder>(
        ItemBaseMovies::class
    ) {

    override fun createViewHolder(inflater: LayoutInflater, parent: ViewGroup): ItemBaseMoviesViewHolder {
        return ItemBaseMoviesViewHolder(ItemPopularMoviesBinding.inflate(inflater))
    }

    override fun provideInstructor(
        holder: ItemBaseMoviesViewHolder,
        item: ItemBaseMovies,
        payload: Any
    ): Instructor {
        return ItemBaseMoviesInstructor()
    }

    override fun bindHolder(model: ItemBaseMovies, holder: ItemBaseMoviesViewHolder, instructor: Instructor) {
        holder.bind(model)
    }

    inner class ItemBaseMoviesViewHolder(private val binding: ItemPopularMoviesBinding) :
        DelegateViewHolder<ItemBaseMovies>(binding) {

        private val adapterMoviesPopular = DelegateAdapter.Builder()
            .setDelegates(ItemMovieHorizontalDelegate(), ItemTilesWithTextDelegate(), ItemPeopleDelegate())
            .setActionProcessor(action)
            .build()

        override fun bind(item: ItemBaseMovies) {
            initRecycler()
            setTitle(item.title)
            setListItems(item.listItems)
            setIsSeeAllVisible(item.isSeeAllVisible)
        }

        fun setListItems(listMoviesPopular: List<BaseUiModel>) {
            adapterMoviesPopular.submitList(listMoviesPopular)
        }

        fun setTitle(title: Text) {
            binding.title.text = title.getStringText(context)
        }

        fun setIsSeeAllVisible(isSeeAllVisible: Boolean) {
            binding.allMovies.isVisible = isSeeAllVisible
        }

        private fun initRecycler() {
            binding.recyclerView.adapter = adapterMoviesPopular
        }

        override fun setOnClickListeners(item: ItemBaseMovies) {
            binding.allMovies.setOnClickListener {

            }
        }

    }
}