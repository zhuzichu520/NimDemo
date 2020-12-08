package com.chuzi.android.nim.emoji

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Paint.FontMetricsInt
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.text.style.ImageSpan
import java.lang.ref.WeakReference

class ImageSpanAlignCenter : ImageSpan {

    constructor(context: Context, resourceId: Int) : super(context, resourceId)

    constructor(d: Drawable) : super(d)

    override fun getSize(
        paint: Paint, text: CharSequence,
        start: Int, end: Int,
        fm: FontMetricsInt?
    ): Int {
        val d = getCachedDrawable(paint)
        val rect = d!!.bounds
        if (fm != null) {
            val fontMetrics = FontMetricsInt()
            paint.getFontMetricsInt(fontMetrics)
            fm.ascent = fontMetrics.ascent
            fm.descent = fontMetrics.descent
            fm.top = fontMetrics.top
            fm.bottom = fontMetrics.bottom
        }
        return rect.right
    }

    override fun draw(
        canvas: Canvas, text: CharSequence, start: Int, end: Int, x: Float, top: Int, y: Int,
        bottom: Int, paint: Paint
    ) {
        val s = text.toString()
        val subS = s.substring(start, end)
        if (ELLIPSIS_NORMAL[0] == subS[0] || ELLIPSIS_TWO_DOTS[0] == subS[0]) {
            canvas.save()
            canvas.drawText(subS, x, y.toFloat(), paint)
            canvas.restore()
        } else {
            getCachedDrawable(paint)?.let {
                canvas.save()
                val transY: Int
                val fontMetrics = FontMetricsInt()
                paint.getFontMetricsInt(fontMetrics)
                transY = y + fontMetrics.ascent
                canvas.translate(x, transY.toFloat())
                it.draw(canvas)
                canvas.restore()
            }
        }
    }

    private fun getCachedDrawable(paint: Paint): Drawable? {
        val wr = mDrawableRef
        var d: Drawable? = null
        if (wr != null) d = wr.get()
        if (d == null) {
            d = drawable
            d.bounds =
                Rect(0, 0, paint.getFontMetricsInt(null), paint.getFontMetricsInt(null))
            mDrawableRef = WeakReference(d)
        }
        return d
    }

    private var mDrawableRef: WeakReference<Drawable?>? = null

    companion object {
        private val ELLIPSIS_NORMAL = charArrayOf('\u2026')
        private val ELLIPSIS_TWO_DOTS = charArrayOf('\u2025')
    }
}