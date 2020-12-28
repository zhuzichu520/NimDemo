package com.chuzi.android.nim.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatSeekBar
import com.chuzi.android.libs.tool.dp2px
import com.chuzi.android.libs.tool.toFloat

class NimSeekBar @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : AppCompatSeekBar(context, attrs, defStyleAttr) {

    private val paint by lazy {
        Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.STROKE
            color = Color.parseColor("#E7E7E7")
            strokeWidth = toFloat(dp2px(context, 1f))
        }
    }

    override fun onDraw(canvas: Canvas?) {
        canvas?.let {
            drawScaleLine(it)
        }
        super.onDraw(canvas)
    }

    /**
     * 绘制刻度线
     */
    private fun drawScaleLine(canvas: Canvas) {
        val measuredHeight = measuredHeight
        val measuredWidth = measuredWidth
        val paddingLeft = paddingLeft
        val itemWidth: Int = (measuredWidth - 2 * paddingLeft) / max
        for (i in 0 until max + 2) {
            canvas.drawLine(
                toFloat(paddingLeft + i * itemWidth), toFloat(measuredHeight / 2),
                toFloat(paddingLeft + i * itemWidth),
                toFloat(measuredHeight) / 2 - toFloat(dp2px(context, 8f)), paint
            )
        }
    }
}