package com.top1shvetsvadim1.jarvis.presentation.utils.custo_views

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.annotation.DimenRes
import androidx.recyclerview.widget.RecyclerView
import com.top1shvetsvadim1.jarvis.presentation.utils.extentions.intDimen

class SpaceDecorator(context: Context, @DimenRes verticalSpace: Int) : RecyclerView.ItemDecoration() {

    private val verticalSpaceHeight = context.intDimen(verticalSpace)

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        outRect.bottom = verticalSpaceHeight
    }
}