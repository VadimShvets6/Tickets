package com.top1shvetsvadim1.jarvis.presentation.utils.recycler_utils

import androidx.recyclerview.widget.DiffUtil
import com.flexeiprata.novalles.interfaces.UIModelHelper

class DefaultDiffUtil(delegates: List<ItemDelegateBase<BaseUiModel, DelegateViewHolder<BaseUiModel>>>) :
    DiffUtil.ItemCallback<BaseUiModel>() {

    private val uiModelHelpers = delegates.associate { it.uiModelClass to it.uiModelHelper }

    override fun areItemsTheSame(oldItem: BaseUiModel, newItem: BaseUiModel): Boolean {
        return oldItem.areItemsTheSame(newItem, uiModelHelpers[oldItem::class] ?: return false)
    }

    override fun areContentsTheSame(oldItem: BaseUiModel, newItem: BaseUiModel): Boolean {
        return oldItem.areContentTheSame(newItem, uiModelHelpers[oldItem::class] ?: return false)
    }

    override fun getChangePayload(oldItem: BaseUiModel, newItem: BaseUiModel): Any {
        return oldItem.changePayload(newItem, uiModelHelpers[oldItem::class] ?: return listOf<Any>())
    }

}

abstract class BaseUiModel {

    fun areItemsTheSame(other: BaseUiModel, helper: UIModelHelper<BaseUiModel>): Boolean {
        return helper.areItemsTheSame(this, other)
    }

    fun areContentTheSame(other: BaseUiModel, helper: UIModelHelper<BaseUiModel>): Boolean {
        return helper.areContentsTheSame(this, other)
    }

    fun changePayload(other: BaseUiModel, helper: UIModelHelper<BaseUiModel>): List<Any> {
        return helper.changePayloads(this, other)
    }

}