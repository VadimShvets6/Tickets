package com.top1shvetsvadim1.jarvis.presentation.ui.main_tabs.home.ui_items

import android.view.LayoutInflater
import android.view.ViewGroup
import com.flexeiprata.novalles.annotations.BindViewHolder
import com.flexeiprata.novalles.annotations.Instruction
import com.flexeiprata.novalles.annotations.PrimaryTag
import com.flexeiprata.novalles.annotations.UIModel
import com.flexeiprata.novalles.interfaces.Instructor
import com.top1shvetsvadim1.jarvis.databinding.ItemPopularMoviesBinding
import com.top1shvetsvadim1.jarvis.presentation.utils.recycler_utils.BaseUiModel
import com.top1shvetsvadim1.jarvis.presentation.utils.recycler_utils.DelegateAdapter
import com.top1shvetsvadim1.jarvis.presentation.utils.recycler_utils.DelegateViewHolder
import com.top1shvetsvadim1.jarvis.presentation.utils.recycler_utils.ItemSimpleDelegate

@UIModel
data class ItemBaseMovies(
    @PrimaryTag val tag: String,
    val title: String,
    val listMoviesPopular: List<BaseUiModel>
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
            .setDelegates(ItemMovieHorizontalDelegate())
            .setActionProcessor(action)
            .build()

        override fun bind(item: ItemBaseMovies) {
            initRecycler()
            setTitle(item.title)
            setListMoviesPopular(item.listMoviesPopular)
        }

        fun setListMoviesPopular(listMoviesPopular: List<BaseUiModel>) {
            adapterMoviesPopular.submitList(listMoviesPopular)
        }

        fun setTitle(title: String) {
            binding.title.text = title
        }

        private fun initRecycler() {
            binding.recyclerView.adapter = adapterMoviesPopular
        }

    }
}