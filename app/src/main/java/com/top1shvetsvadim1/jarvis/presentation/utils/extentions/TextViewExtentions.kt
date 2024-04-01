package com.top1shvetsvadim1.jarvis.presentation.utils.extentions

import android.graphics.Color
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.AppCompatTextView
import com.top1shvetsvadim1.jarvis.common.KTextSpan
import com.top1shvetsvadim1.jarvis.common.Text
import java.util.Locale


fun TextView.setClickableSpan(fullText: String, spans: List<KTextSpan>) {
    try {
        val spannableString = SpannableString(fullText)
        spans.forEach { kTextSpan ->
            val clickableSpan = object : ClickableSpan() {
                override fun onClick(widget: View) {
                    kTextSpan.action()
                }

                override fun updateDrawState(ds: TextPaint) {
                    super.updateDrawState(ds)
                    ds.color = kTextSpan.foregroundColor
                    ds.isUnderlineText = kTextSpan.underline
                }
            }
            val clickableText = when (kTextSpan.clickableText) {
                is Text.Simple -> kTextSpan.clickableText.value;
                is Text.Resource -> this.context.getString(kTextSpan.clickableText.resource)
            }
            val startIndex = fullText.lowercase(Locale.getDefault())
                .indexOf(clickableText.lowercase(Locale.getDefault()))
            try {
                spannableString.setSpan(
                    clickableSpan,
                    startIndex,
                    startIndex + clickableText.length,
                    Spanned.SPAN_INCLUSIVE_INCLUSIVE
                )
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
        }

        this.highlightColor = Color.parseColor("#00000000")
        this.movementMethod = LinkMovementMethod.getInstance()
        this.text = spannableString
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

fun AppCompatTextView.setTextAnimation(text: String, duration: Long = 200, completion: (() -> Unit)? = null) {
    fadOutAnimation(duration) {
        this.text = text
        fadInAnimation(duration) {
            completion?.let {
                it()
            }
        }
    }
}

fun View.fadOutAnimation(duration: Long = 200, visibility: Int = View.INVISIBLE, completion: (() -> Unit)? = null) {
    animate()
        .alpha(0f)
        .setDuration(duration)
        .withEndAction {
            this.visibility = visibility
            completion?.let {
                it()
            }
        }
}

fun View.fadInAnimation(duration: Long = 200, completion: (() -> Unit)? = null) {
    alpha = 0f
    visibility = View.VISIBLE
    animate()
        .alpha(1f)
        .setDuration(duration)
        .withEndAction {
            completion?.let {
                it()
            }
        }
}