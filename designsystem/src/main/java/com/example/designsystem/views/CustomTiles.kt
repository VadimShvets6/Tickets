package com.example.designsystem.views

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import com.example.designsystem.R
import com.example.designsystem.databinding.ViewCustomTilesBinding
import com.example.designsystem.views.extentions.getTypedArray

class CustomTiles @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0,
) : FrameLayout(context, attrs, defStyleAttr) {

    private val binding = ViewCustomTilesBinding.inflate(LayoutInflater.from(context), this, true)

    init {
        attrs?.let {
            initAttributes(context, attrs)
        }
    }

    private fun initAttributes(context: Context, attrs: AttributeSet) {
        val attr: TypedArray = getTypedArray(context, attrs, R.styleable.CustomTiles)
        attr.recycle()
    }

    fun setDefaultState(fullName: String) {
        val codeValue = fullName.map { it.code }.sum()
        val drawable = when (codeValue % 6) {
            0 -> R.drawable.shape_rectangle_blue
            1 -> R.drawable.shape_rectangle_green
            2 -> R.drawable.shape_rectangle_pink
            3 -> R.drawable.shape_rectangle_red
            4 -> R.drawable.shape_rectangle_violet
            5 -> R.drawable.shape_rectangle_yellow
            else -> return
        }
        binding.root.setBackgroundResource(drawable)
    }

    fun setText(text: String) {
        binding.title.text = text
        //setDefaultState(text)
    }

    fun setOnClickListener(onClick: (View) -> Unit){
        binding.root.setOnClickListener(onClick)
    }
}