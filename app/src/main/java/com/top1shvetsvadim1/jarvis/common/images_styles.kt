package com.top1shvetsvadim1.jarvis.common

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import coil.load
import coil.request.ImageRequest
import com.example.coreutills.rendering.TilingDrawable

sealed class Image {

    abstract fun toData(context: Context): Any?

    data class Resource(@DrawableRes val resource: Int) : Image() {
        override fun toData(context: Context): Any? {
            return ContextCompat.getDrawable(context, resource)
        }
    }

    data class Network(val url: String) : Image() {
        override fun toData(context: Context): Any? {
            return url
        }
    }

    data class BitmapResource(val bitmap: Bitmap) : Image() {
        override fun toData(context: Context): Any? {
            return bitmap
        }
    }

    /**
     * If you to load [Image.Tile], be aware to put [ImageRequest.Builder.allowHardware] value to false, if you want to
     * use any software rendering beside it.
     */
    data class Tile(@DrawableRes val resource: Int) : Image() {
        fun getDrawable(context: Context) =
            TilingDrawable(AppCompatResources.getDrawable(context, resource)!!) as Drawable

        override fun toData(context: Context): Any? {
            return getDrawable(context)
        }
    }
}

/**
 * If you to load [Image.Tile], be aware to put [ImageRequest.Builder.allowHardware] value to false, if you want to
 * use any software rendering beside it.
 */
fun Image.loadImage(imageView: ImageView, extension: ImageRequest.Builder.() -> Unit = {}) {
    when (this) {
        is Image.Resource -> {
            imageView.setImageDrawable(
                AppCompatResources.getDrawable(
                    imageView.context,
                    this.resource
                )
            )
        }

        is Image.Network -> {
            imageView.load(this.url) {
                extension(this)
                allowHardware(false)
            }
        }

        is Image.BitmapResource -> {
            imageView.load(this.bitmap) {
                extension(this)
                allowHardware(false)
            }
        }

        is Image.Tile -> {
            val tiledDrawable = TilingDrawable(AppCompatResources.getDrawable(imageView.context, resource)!!)
            imageView.load(tiledDrawable) {
                extension(this)
                allowHardware(true)
            }
        }
    }
}
