package com.top1shvetsvadim1.jarvis.presentation.ui.main_tabs.details.ui_items

import android.view.LayoutInflater
import android.view.ViewGroup
import com.flexeiprata.novalles.annotations.BindViewHolder
import com.flexeiprata.novalles.annotations.Instruction
import com.flexeiprata.novalles.annotations.PrimaryTag
import com.flexeiprata.novalles.annotations.UIModel
import com.flexeiprata.novalles.interfaces.Instructor
import com.top1shvetsvadim1.jarvis.databinding.ItemMovieGeneralInforamtionBinding
import com.top1shvetsvadim1.jarvis.presentation.utils.extentions.capitalizeFirst
import com.top1shvetsvadim1.jarvis.presentation.utils.recycler_utils.BaseUiModel
import com.top1shvetsvadim1.jarvis.presentation.utils.recycler_utils.DelegateViewHolder
import com.top1shvetsvadim1.jarvis.presentation.utils.recycler_utils.ItemSimpleDelegate
import java.text.NumberFormat
import java.util.Locale

@UIModel
data class ItemGeneralInformation(
    @PrimaryTag val tag: String,
    val originalTitle: String,
    val originalLanguage: String,
    val budget: Long,
    val revenue: Long,
    val country: String?
) : BaseUiModel()

@BindViewHolder(ItemGeneralInformationDelegate.ItemGeneralInformationViewHolder::class)
@Instruction(ItemGeneralInformation::class)
class ItemGeneralInformationInstructor : Instructor

@Suppress("unused")
class ItemGeneralInformationDelegate :
    ItemSimpleDelegate<ItemGeneralInformation, ItemGeneralInformationDelegate.ItemGeneralInformationViewHolder>(
        ItemGeneralInformation::class
    ) {

    override fun createViewHolder(inflater: LayoutInflater, parent: ViewGroup): ItemGeneralInformationViewHolder {
        return ItemGeneralInformationViewHolder(ItemMovieGeneralInforamtionBinding.inflate(inflater, parent, false))
    }

    override fun provideInstructor(
        holder: ItemGeneralInformationViewHolder,
        item: ItemGeneralInformation,
        payload: Any
    ): Instructor {
        return ItemGeneralInformationInstructor()
    }

    inner class ItemGeneralInformationViewHolder(private val binding: ItemMovieGeneralInforamtionBinding) :
        DelegateViewHolder<ItemGeneralInformation>(binding) {

        fun setOriginalTitle(originalTitle: String) {
            binding.originalTitle.text = originalTitle
        }

        fun setOriginalLanguage(originalLanguage: String) {
            binding.originalLanguage.text = Locale(originalLanguage).displayLanguage.capitalizeFirst()
        }

        fun setBudget(budget: Long) {
            binding.budget.text = String.format("$%s", NumberFormat.getInstance().format(budget))
        }

        fun setRevenue(revenue: Long) {
            binding.revenue.text = String.format("$%s", NumberFormat.getInstance().format(revenue))
        }

        fun setCountry(country: String?) {
            binding.productionCountry.text = country
        }
    }
}