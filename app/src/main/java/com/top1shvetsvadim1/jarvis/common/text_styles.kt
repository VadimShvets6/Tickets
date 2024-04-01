package com.top1shvetsvadim1.jarvis.common

import android.content.Context
import android.graphics.Color
import android.text.Spannable
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.text.style.UnderlineSpan
import android.view.View
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.annotation.FontRes
import androidx.annotation.StringRes
import androidx.core.content.res.ResourcesCompat
import com.example.coreutills.utils.CustomTypefaceSpan
import com.top1shvetsvadim1.jarvis.presentation.utils.extentions.setClickableSpan
import java.util.Locale

sealed class Text {
    data class Simple(val value: String) : Text()
    data class Resource(@StringRes val resource: Int, val arguments: List<Text> = mutableListOf()) :
        Text()
}

fun Text.getStringText(context: Context): String {
    return when (this) {
        is Text.Resource -> {
            if (arguments.isNotEmpty()) {
                context.getStringWithArgs(this.resource, arguments)
            } else {
                context.getString(this.resource)
            }
        }

        is Text.Simple -> this.value
    }
}

fun Text.Simple.append(value: String): Text {
    return Text.Simple(StringBuilder(this.value).append(value).toString())
}


fun Context.getStringWithArgs(@StringRes resourceId: Int, arguments: List<Text>): String {
    return if (arguments.isNotEmpty()) {
        val mappedArguments = arguments.map {
            when (it) {
                is Text.Simple -> it.value
                is Text.Resource -> this.getStringWithArgs(it.resource, it.arguments)
            }
        }
        String.format(this.getString(resourceId), *mappedArguments.toTypedArray())
    } else {
        this.getString(resourceId)
    }
}

sealed class SpanText() {
    data class Click(
        val text: Text.Resource,
        val span: Text,
        @ColorInt val foregroundColor: Int,
        val isUnderline: Boolean
    ) : SpanText()

    data class Foreground(
        val text: Text.Resource,
        val span: Text,
        @ColorInt val foregroundColor: Int,
        val isUnderline: Boolean = false
    ) : SpanText()

    data class FontSpan(
        val text: Text.Resource,
        val span: Text,
        @ColorInt val foregroundColor: Int,
        val isUnderline: Boolean = false,
        @FontRes val fontRes: Int
    ) : SpanText()

    data class Underline(
        val text: Text.Resource,
        val span: Text,
    ) : SpanText() {

    }
}

fun SpanText.getSpannableText(context: Context, action: () -> Unit = {}): Spannable {
    return when (this) {
        is SpanText.Foreground -> {
            val spannedText = this.span.getStringText(context)
            val fullTextString = context.getStringWithArgs(
                this.text.resource, mutableListOf(
                    Text.Simple(
                        spannedText
                    )
                )
            )
            val spannableText = SpannableString(fullTextString)
            val startIndex = fullTextString.lowercase(Locale.getDefault())
                .indexOf(spannedText.lowercase(Locale.getDefault()))
            spannableText.setSpan(
                ForegroundColorSpan(foregroundColor),
                startIndex,
                startIndex + spannedText.length,
                Spanned.SPAN_INCLUSIVE_INCLUSIVE
            )
            spannableText
        }

        is SpanText.Click -> {
            val spannedText = this.span.getStringText(context)
            val fullTextString =
                context.getStringWithArgs(this.text.resource, mutableListOf(this.span))
            val spannableText = SpannableString(fullTextString)

            val clickableSpan = object : ClickableSpan() {
                override fun onClick(widget: View) {
                    action.invoke()
                }

                override fun updateDrawState(ds: TextPaint) {
                    super.updateDrawState(ds)
                    ds.color = foregroundColor
                    ds.isUnderlineText = isUnderline
                }
            }
            val startIndex = fullTextString.lowercase(Locale.getDefault())
                .indexOf(spannedText.lowercase(Locale.getDefault()))

            try {
                spannableText.setSpan(
                    clickableSpan,
                    startIndex,
                    startIndex + spannedText.length,
                    Spanned.SPAN_INCLUSIVE_INCLUSIVE
                )
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }

            spannableText
        }

        is SpanText.FontSpan -> {
            val spannedText = this.span.getStringText(context)
            val fullTextString = context.getStringWithArgs(
                this.text.resource,
                mutableListOf(Text.Simple(spannedText))
            )
            val spannableText = SpannableString(fullTextString)
            val startIndex = fullTextString.lowercase(Locale.getDefault())
                .indexOf(spannedText.lowercase(Locale.getDefault()))
            val font = ResourcesCompat.getFont(context, fontRes)
            spannableText.setSpan(
                CustomTypefaceSpan(font!!),
                startIndex,
                startIndex + spannedText.length,
                Spanned.SPAN_INCLUSIVE_INCLUSIVE
            )
            spannableText
        }

        is SpanText.Underline -> {
            val spannedText = this.span.getStringText(context)
            val fullTextString = context.getStringWithArgs(
                this.text.resource,
                mutableListOf(Text.Simple(spannedText))
            )
            val spannableText = SpannableString(fullTextString)
            val startIndex = fullTextString.lowercase(Locale.getDefault())
                .indexOf(spannedText.lowercase(Locale.getDefault()))
            spannableText.setSpan(
                UnderlineSpan(),
                startIndex,
                startIndex + spannedText.length,
                Spanned.SPAN_INCLUSIVE_INCLUSIVE
            )
            spannableText
        }

    }
}

fun TextView.setSpanViews(
    spanText: String,
    fullText: String,
    underline: Boolean = false,
    action: () -> Unit = {}
) {
    val clickableSpan = KTextSpan(
        clickableText = Text.Simple(spanText),
        foregroundColor = Color.parseColor("#0091FF"),
        underline = underline,
        action = action
    )
    this.setClickableSpan(String.format(fullText, spanText), arrayListOf(clickableSpan))
}

data class KTextSpan(
    val clickableText: Text,
    val underline: Boolean = true, @ColorInt val foregroundColor: Int,
    val action: () -> Unit = {}
)