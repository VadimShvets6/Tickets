package com.top1shvetsvadim1.jarvis.presentation.ui.main_tabs.home.ui_items

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.flexeiprata.novalles.annotations.BindViewHolder
import com.flexeiprata.novalles.annotations.Instruction
import com.flexeiprata.novalles.annotations.PrimaryTag
import com.flexeiprata.novalles.annotations.UIModel
import com.flexeiprata.novalles.interfaces.Instructor
import com.google.android.flexbox.AlignItems
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.top1shvetsvadim1.jarvis.R
import com.top1shvetsvadim1.jarvis.common.Text
import com.top1shvetsvadim1.jarvis.common.getStringText
import com.top1shvetsvadim1.jarvis.databinding.ItemGenresBaseBinding
import com.top1shvetsvadim1.jarvis.presentation.utils.recycler_utils.Action
import com.top1shvetsvadim1.jarvis.presentation.utils.recycler_utils.BaseUiModel
import com.top1shvetsvadim1.jarvis.presentation.utils.recycler_utils.DelegateAdapter
import com.top1shvetsvadim1.jarvis.presentation.utils.recycler_utils.DelegateViewHolder
import com.top1shvetsvadim1.jarvis.presentation.utils.recycler_utils.ItemPrefetchDelegate

@UIModel
data class ItemGenresBase(
    @PrimaryTag val tag: String,
    val title: Text,
    val listItems: List<BaseUiModel>
) : BaseUiModel()

@BindViewHolder(ItemGenresDelegate.ItemGenresViewHolder::class)
@Instruction(ItemGenresBase::class)
class ItemGenresInstructor : Instructor

class ItemGenresDelegate : ItemPrefetchDelegate<ItemGenresBase, ItemGenresDelegate.ItemGenresViewHolder>(
    ItemGenresBase::class,
    R.layout.item_genres_base
) {

    override fun createViewHolder(view: View): ItemGenresViewHolder {
        return ItemGenresViewHolder(ItemGenresBaseBinding.bind(view))
    }

    override val prefetchedViewsCount = 2
    override fun provideInstructor(holder: ItemGenresViewHolder, item: ItemGenresBase, payload: Any): Instructor {
        return ItemGenresInstructor()
    }

    override fun bindHolder(model: ItemGenresBase, holder: ItemGenresViewHolder, instructor: Instructor) {
        holder.bind(model)
    }

    inner class ItemGenresViewHolder(private val binding: ItemGenresBaseBinding) :
        DelegateViewHolder<ItemGenresBase>(binding) {

        private val adapterGenres = DelegateAdapter.Builder()
            .setDelegates(ItemTilesWithTextDelegate())
            .setActionProcessor(action)
            .build()

        init {
            initRecyclerView()
        }

        override fun bind(item: ItemGenresBase) {
            setListItems(item.listItems)
            setTitle(item.title)
        }

        fun setTitle(title: Text) {
            binding.title.text = title.getStringText(context)
        }

        fun setListItems(listItems: List<BaseUiModel>) {
            adapterGenres.submitList(listItems)
        }


        private fun initRecyclerView() {
            binding.recyclerView.apply {
                layoutManager = FlexboxLayoutManager(context).apply {
                    flexWrap = FlexWrap.WRAP
                    flexDirection = FlexDirection.ROW
                    justifyContent = JustifyContent.FLEX_START
                    alignItems = AlignItems.FLEX_START
                }
                adapter = adapterGenres
            }
        }
    }

    sealed interface ItemGenresActions : Action {
        object OnGenreClick : ItemGenresActions
    }
}