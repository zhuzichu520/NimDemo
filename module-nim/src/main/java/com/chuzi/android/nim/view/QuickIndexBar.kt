package com.chuzi.android.nim.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.core.content.ContextCompat
import com.chuzi.android.nim.R

class QuickIndexBar @JvmOverloads constructor(
    paramContext: Context,
    paramAttributeSet: AttributeSet? = null,
    paramInt: Int = 0
) : View(paramContext, paramAttributeSet, paramInt) {
    private var listener: OnTouchingLetterChangedListener? = null
    private var letters: Array<String>
    private val mPaint: Paint = Paint()
    private var offset: Float
    private var hit: Boolean
    private var normalColor: Int
    private val touchColor: Int
    private val hintDrawable: Drawable?
    private val pressColor = Color.parseColor("#33000000")

    init {
        offset = 0.0f
        hit = false
        touchColor = Color.WHITE
        hintDrawable = ContextCompat.getDrawable(paramContext, R.drawable.nim_letter_hit_point)
        hintDrawable?.setBounds(0, 0, hintDrawable.intrinsicWidth, hintDrawable.intrinsicHeight)
        val typedArray =
            paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.QuickIndexBar)
        normalColor =
            typedArray.getColor(R.styleable.QuickIndexBar_QuickIndexBar_textColor, Color.GRAY)
        typedArray.recycle()
        mPaint.isAntiAlias = true
        mPaint.textAlign = Paint.Align.CENTER
        mPaint.color = normalColor
        letters = paramContext.resources.getStringArray(R.array.letter_list)
    }

    fun setOnTouchingLetterChangedListener(onTouchingLetterChangedListener: OnTouchingLetterChangedListener?) {
        listener = onTouchingLetterChangedListener
    }

    fun setLetters(letters: Array<String>) {
        this.letters = letters
    }

    fun setNormalColor(color: Int) {
        normalColor = color
        mPaint.color = normalColor
    }

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                hit = true
                setBackgroundColor(pressColor)
                mPaint.color = touchColor
                onHit(event.y)
            }
            MotionEvent.ACTION_MOVE -> onHit(event.y)
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> onCancel()
        }
        invalidate()
        return true
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val letterHeight = height.toFloat() / letters.size
        val width = width.toFloat()
        val textSize = letterHeight * 2 / 3
        mPaint.textSize = textSize
        for (i in letters.indices) {
            val halfWidth = width / 2
            val letterPosY = letterHeight * i + textSize
            canvas.drawText(letters[i], halfWidth, letterPosY, mPaint)
        }
        if (hit) {
            val halfWidth = getWidth() / 2
            val halfDrawWidth = hintDrawable!!.intrinsicWidth / 2
            val translateX = (halfWidth - halfDrawWidth).toFloat()
            val halfDrawHeight = hintDrawable.intrinsicHeight / 2
            val translateY = offset - halfDrawHeight
            canvas.save()
            canvas.translate(translateX, translateY)
            hintDrawable.draw(canvas)
            canvas.restore()
        }
    }

    private fun onHit(offset: Float) {
        this.offset = offset
        if (hit && listener != null) {
            var index = (offset / height * letters.size).toInt()
            index = index.coerceAtLeast(0)
            index = index.coerceAtMost(letters.size - 1)
            val str = letters[index]
            listener!!.onHit(str)
        }
    }

    private fun onCancel() {
        hit = false
        setBackgroundColor(Color.TRANSPARENT)
        mPaint.color = normalColor
        refreshDrawableState()
        if (listener != null) {
            listener!!.onCancel()
        }
    }

    interface OnTouchingLetterChangedListener {
        fun onHit(letter: String?)
        fun onCancel()
    }

}