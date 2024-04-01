package com.top1shvetsvadim1.jarvis.presentation.ui.main_tabs.home.ui_items

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.flexeiprata.novalles.annotations.BindViewHolder
import com.flexeiprata.novalles.annotations.Instruction
import com.flexeiprata.novalles.annotations.PrimaryTag
import com.flexeiprata.novalles.annotations.UIModel
import com.flexeiprata.novalles.interfaces.Instructor
import com.top1shvetsvadim1.jarvis.common.Text
import com.top1shvetsvadim1.jarvis.common.getStringText
import com.top1shvetsvadim1.jarvis.databinding.ItemNowPlayingBinding
import com.top1shvetsvadim1.jarvis.presentation.utils.extentions.capitalizeFirst
import com.top1shvetsvadim1.jarvis.presentation.utils.extentions.setTextAnimation
import com.top1shvetsvadim1.jarvis.presentation.utils.recycler_utils.BaseUiModel
import com.top1shvetsvadim1.jarvis.presentation.utils.recycler_utils.DelegateAdapter
import com.top1shvetsvadim1.jarvis.presentation.utils.recycler_utils.DelegateViewHolder
import com.top1shvetsvadim1.jarvis.presentation.utils.recycler_utils.ItemSimpleDelegate

@UIModel
data class ItemNowPlayingBase(
    @PrimaryTag val tag: String,
    val title: Text,
    val listImages: List<BaseUiModel>
) : BaseUiModel()

@BindViewHolder(ItemNowPlayingBaseDelegate.ItemNowPlayingViewHolder::class)
@Instruction(ItemNowPlayingBase::class)
class ItemNowPlayingInstructor : Instructor

class ItemNowPlayingBaseDelegate :
    ItemSimpleDelegate<ItemNowPlayingBase, ItemNowPlayingBaseDelegate.ItemNowPlayingViewHolder>(
        ItemNowPlayingBase::class
    ) {

    override fun createViewHolder(inflater: LayoutInflater, parent: ViewGroup): ItemNowPlayingViewHolder {
        return ItemNowPlayingViewHolder(ItemNowPlayingBinding.inflate(inflater, parent, false))
    }

    override fun provideInstructor(
        holder: ItemNowPlayingViewHolder,
        item: ItemNowPlayingBase,
        payload: Any
    ): Instructor {
        return ItemNowPlayingInstructor()
    }

    override fun bindHolder(model: ItemNowPlayingBase, holder: ItemNowPlayingViewHolder, instructor: Instructor) {
        holder.bind(model)
    }


    inner class ItemNowPlayingViewHolder(private val binding: ItemNowPlayingBinding) :
        DelegateViewHolder<ItemNowPlayingBase>(binding) {

        private val adapterImages = DelegateAdapter.Builder()
            .setDelegates(ItemNowPlayingPosterDelegate())
            .setActionProcessor(action)
            .build()

        init {
            initViewPager()
        }

        override fun bind(item: ItemNowPlayingBase) {
            setListImages(item.listImages)
            setTitle(item.title)
        }

        fun setTitle(title: Text) {
            binding.title.text = title.getStringText(context)
        }

        override fun setOnClickListeners(item: ItemNowPlayingBase) {
            binding.allMovies.setOnClickListener {

            }
        }

        fun setListImages(listImages: List<BaseUiModel>) {
            adapterImages.submitList(listImages)
            if (adapterImages.currentList.size > 0) {
                binding.movieTitle.text =
                    (adapterImages.currentList[binding.viewPager.currentItem] as ItemNowPlayingPoster).titleMovie
                binding.genres.text =
                    (adapterImages.currentList[binding.viewPager.currentItem] as ItemNowPlayingPoster).genres.capitalizeFirst()
                binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                    override fun onPageSelected(position: Int) {
                        binding.movieTitle.setTextAnimation((adapterImages.currentList[position] as ItemNowPlayingPoster).titleMovie)
                        binding.genres.setTextAnimation(
                            (adapterImages.currentList[position] as ItemNowPlayingPoster).genres.capitalizeFirst()
                        )
                    }
                })
            }
        }

        private fun initViewPager() {
            binding.viewPager.apply {
                adapter = adapterImages
                offscreenPageLimit = 3
                setPageTransformer { page, position ->
                    val scaleFactor = 0.9f + (1 - 0.9f) * (1 - kotlin.math.abs(position))
                    page.apply {
                        pivotX = width * 0.85f
                        pivotY = height * 0.85f
                        scaleX = scaleFactor
                        scaleY = scaleFactor
                        alpha = kotlin.math.max(0.5f, 1 - kotlin.math.abs(position) * 0.65f)
                    }
                    (getChildAt(0) as RecyclerView).overScrollMode = RecyclerView.OVER_SCROLL_NEVER
                }
            }
            binding.dots.attachTo(binding.viewPager)
        }

    }
}