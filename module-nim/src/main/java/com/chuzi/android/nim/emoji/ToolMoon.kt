package com.chuzi.android.nim.emoji

import android.content.Context
import android.graphics.drawable.Drawable
import android.net.Uri
import android.text.Spannable
import android.text.SpannableString
import android.text.TextPaint
import android.text.TextUtils
import android.text.style.ClickableSpan
import android.view.View
import android.widget.EditText
import android.widget.TextView
import com.chuzi.android.nim.tools.ToolTeamMemberAit
import java.util.*
import java.util.regex.Pattern

object ToolMoon {

    /**
     * 表情的放大倍数
     */
    private const val EMOJIICON_SCALE = 1.2f

    fun identifyFaceExpression(context: Context, textView: TextView, value: CharSequence) {
        identifyFaceExpression(
            context,
            textView,
            value,
            (textView.textSize * EMOJIICON_SCALE).toInt()
        )
    }

    fun identifyFaceExpressionAndATags(context: Context, textView: TextView, value: CharSequence) {
        val mSpannableString = makeSpannableStringTags(
            context,
            value,
            (textView.textSize * EMOJIICON_SCALE).toInt()
        )
        viewSetText(textView, mSpannableString)
    }

    /**
     * 具体类型的view设置内容
     *
     * @param textView
     * @param mSpannableString
     */
    private fun viewSetText(textView: View, mSpannableString: SpannableString) {
        val tv = textView as TextView
        tv.text = mSpannableString
    }

    fun identifyFaceExpression(context: Context, textView: View, value: CharSequence, size: Int) {
        val mSpannableString = replaceEmoticons(context, value, size)
        viewSetText(textView, mSpannableString)
    }

    fun identifyRecentVHFaceExpressionAndTags(
        context: Context,
        textView: View,
        value: String,
        size: Int
    ) {
        val mSpannableString = makeSpannableStringTags(context, value, size, false)
        ToolTeamMemberAit.replaceAitForeground(value, mSpannableString)
        viewSetText(textView, mSpannableString)
    }

    /**
     * lstmsgviewholder类使用,只需显示a标签对应的文本
     */
    fun identifyFaceExpressionAndTags(context: Context, textView: View, value: CharSequence, size: Int) {
        val mSpannableString = makeSpannableStringTags(context, value, size, false)
        viewSetText(textView, mSpannableString)
    }

    private fun replaceEmoticons(context: Context, text: CharSequence, size: Int): SpannableString {
        val mSpannableString = SpannableString(text)
        val matcher = EmojiManager.pattern.matcher(text)
        while (matcher.find()) {
            val start = matcher.start()
            val end = matcher.end()
            val emot = text.substring(start, end)
            val span = EmojiSpan(context, emot, size, size)
            if (span.drawable != null) {
                mSpannableString.setSpan(span, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            }
        }
        return mSpannableString
    }

    private val mATagPattern = Pattern.compile("<a.*?>.*?</a>")

    @JvmOverloads
    fun makeSpannableStringTags(
        context: Context,
        text: CharSequence,
        size: Int,
        bTagClickable: Boolean = true
    ): SpannableString {
        var value = text
        val tagSpans = ArrayList<ATagSpan>()
        if (TextUtils.isEmpty(value)) {
            value = ""
        }
        var aTagMatcher = mATagPattern.matcher(value)
        var start: Int
        var end: Int
        while (aTagMatcher.find()) {
            start = aTagMatcher.start()
            end = aTagMatcher.end()
            val atagString = value.substring(start, end)
            val tagSpan = getTagSpan(atagString)
            value = value.substring(0, start) + tagSpan.tag + value.substring(end)
            tagSpan.setRange(start, start + tagSpan.tag.length)
            tagSpans.add(tagSpan)
            aTagMatcher = mATagPattern.matcher(value)
        }
        val mSpannableString = SpannableString(value)
        val matcher = EmojiManager.pattern.matcher(value)
        while (matcher.find()) {
            start = matcher.start()
            end = matcher.end()
            val emot = value.substring(start, end)
            val span = EmojiSpan(context, emot, size, size)
            mSpannableString.setSpan(span, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
        if (bTagClickable) {
            for (tagSpan in tagSpans) {
                mSpannableString.setSpan(
                    tagSpan,
                    tagSpan.start,
                    tagSpan.end,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }
        }
        return mSpannableString
    }

    fun replaceEmoticons(context: Context, editText: EditText, start: Int, count: Int) {
        val editable = editText.text
        if (count <= 0 || editable.length < start + count) return
        val s = editable.subSequence(start, start + count)
        val matcher = EmojiManager.pattern.matcher(s)
        val emojiSize = (editText.textSize * EMOJIICON_SCALE).toInt()
        while (matcher.find()) {
            val from = start + matcher.start()
            val to = start + matcher.end()
            val emot = editable.subSequence(from, to).toString()
            val span = EmojiSpan(context, emot, emojiSize, emojiSize)
            editable.setSpan(span, from, to, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
    }

    private fun getEmotDrawable(context: Context, text: String, scale: Float): Drawable? {
        val drawable = EmojiManager.getDrawable(context, text)
        if (drawable != null) {
            val width = (drawable.intrinsicWidth * scale).toInt()
            val height = (drawable.intrinsicHeight * scale).toInt()
            drawable.setBounds(0, 0, width, height)
        }
        return drawable
    }

    private fun getTagSpan(text: String): ATagSpan {
        var href = ""
        var tag = ""
        if (text.toLowerCase().contains("href")) {
            val start = text.indexOf("\"")
            val end = text.indexOf("\"", start + 1)
            if (end > start) href = text.substring(start + 1, end)
        }
        val start = text.indexOf(">")
        val end = text.indexOf("<", start)
        if (end > start) tag = text.substring(start + 1, end)
        return ATagSpan(tag, href)
    }

    private class ATagSpan(val tag: String, private var mUrl: String) : ClickableSpan() {

        var start = 0
        var end = 0

        override fun updateDrawState(ds: TextPaint) {
            super.updateDrawState(ds)
            ds.isUnderlineText = true
        }

        fun setRange(start: Int, end: Int) {
            this.start = start
            this.end = end
        }

        override fun onClick(widget: View) {
            try {
                if (TextUtils.isEmpty(mUrl)) return
                val uri = Uri.parse(mUrl)
                val scheme = uri.scheme
                if (TextUtils.isEmpty(scheme)) {
                    mUrl = "http://$mUrl"
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}