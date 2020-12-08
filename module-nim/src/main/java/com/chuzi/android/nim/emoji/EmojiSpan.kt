package com.chuzi.android.nim.emoji

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Paint.FontMetricsInt
import android.graphics.drawable.Drawable
import android.text.style.DynamicDrawableSpan
import java.lang.ref.WeakReference

internal class EmojiSpan(
    private val mContext: Context,
    private val mEmojiText: String,
    private val mSize: Int,
    textSize: Int
) : DynamicDrawableSpan(
    ALIGN_BASELINE
) {

    private val mTextSize: Int
    private var mHeight: Int
    private var mWidth: Int
    private var mTop = 0
    private var mDrawable: Drawable? = null

    private var mDrawableRef: WeakReference<Drawable?>? = null

    // 手动偏移值
    private var mTranslateY = 0

    override fun getSize(
        paint: Paint,
        text: CharSequence,
        start: Int,
        end: Int,
        fm: FontMetricsInt?
    ): Int {
        val drawable = cachedDrawable ?: return 0
        val rect = drawable.bounds
        if (fm != null) {
            val pfm = paint.fontMetricsInt
            fm.ascent = pfm.ascent
            fm.descent = pfm.descent
            fm.top = pfm.top
            fm.bottom = pfm.bottom
        }
        return rect.right
    }

    override fun getDrawable(): Drawable? {
        if (mDrawable == null) {
            mDrawable = EmojiManager.getDrawable(mContext, mEmojiText)?.apply {
                mHeight = mSize
                mWidth = mHeight * this.intrinsicWidth / this.intrinsicHeight
                mTop = (mTextSize - mHeight) / 2
                this.setBounds(0, mTop, mWidth, mTop + mHeight)
            }
        }
        return mDrawable
    }

    override fun draw(
        canvas: Canvas,
        text: CharSequence,
        start: Int,
        end: Int,
        x: Float,
        top: Int,
        y: Int,
        bottom: Int,
        paint: Paint
    ) {
        cachedDrawable?.let {
            val count = canvas.save()
            val fontMetricesTop = y + paint.fontMetricsInt.top
            val fontMetricesBottom =
                fontMetricesTop + (paint.fontMetricsInt.bottom - paint.fontMetricsInt.top)
            var transY =
                fontMetricesTop + (fontMetricesBottom - fontMetricesTop) / 2 - (it.bounds.bottom - it.bounds.top) / 2 - mTop
            transY += mTranslateY
            canvas.translate(x, transY.toFloat())
            it.draw(canvas)
            canvas.restoreToCount(count)
        }
    }

    private val cachedDrawable: Drawable?
        get() {
            if (mDrawableRef == null || mDrawableRef?.get() == null) {
                mDrawableRef = WeakReference(drawable)
            }
            return mDrawableRef?.get()
        }

    fun setTranslateY(translateY: Int) {
        mTranslateY = translateY
    }

    init {
        mHeight = mSize
        mWidth = mHeight
        mTextSize = textSize
    }
}