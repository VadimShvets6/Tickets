package com.top1shvetsvadim1.jarvis.presentation.utils.recycler_utils

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding


@Suppress("unused")
abstract class DelegateViewHolder<I : BaseUiModel>(binding: ViewBinding) : RecyclerView.ViewHolder(binding.root) {

    protected val context: Context = binding.root.context

    open fun bind(item: I) {
        this.adapterPosition
    }

    open fun setOnClickListeners(item: I) {

    }

    

}


interface RecyclerViewHoldersController {

    fun receiveMessage(message: ControllerMessage)

    fun attachToRecycler(recyclerView: RecyclerView)

    interface ControllerMessage

}