package com.example.slidebar

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.HorizontalScrollView
import androidx.core.content.withStyledAttributes
import com.example.slider_bar_horizontal.R
import kotlin.math.floor

private const val ANCHOR_STROKE_WIDTH = 16f
private val ANCHOR_COLOR = Color.parseColor("#4ce2eb")

class SlideBarHorizontal @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : HorizontalScrollView(context, attrs, defStyleAttr) {

    interface OnValueChangeListener {
        fun onValueChange(currentValue: Float)
    }

    private var moneyValue: Float = 1024f
    private var gapSize: Float = 24f
    private var startPadding: Float = 80f
    private var barColor: Int = Color.WHITE
    private var anchorColor: Int = ANCHOR_COLOR
        set(value) {
            field = value
            paint.color = value
        }
    private val slideBar = SlideBar(context)
    private var anchorBarLength: Float
    private val anchorX: Float
        get() = startPadding + scrollX

//    private var motionTouchEventX: Float = 0f
//    private var motionTouchEventY: Float = 0f

    private var onValueChangeListener: OnValueChangeListener? = null

    fun setOnValueChangeListener(listener: OnValueChangeListener) {
        onValueChangeListener = listener
        viewTreeObserver.addOnScrollChangedListener {
            onValueChangeListener?.onValueChange(curMoney)
        }
    }

    var curMoney: Float = 0f
        set(value){
            smoothScrollTo((value/SlideBar.GAP_VALUE*gapSize).toInt(), scrollY)
            field = value
        }
        get() = scrollX/ gapSize * SlideBar.GAP_VALUE

    private val paint = Paint().apply {
        textSize = 15f
        color = barColor
        isAntiAlias = true
        style = Paint.Style.STROKE
        strokeCap = Paint.Cap.ROUND
        strokeWidth = ANCHOR_STROKE_WIDTH
    }


    init {
        isClickable = true
        context.withStyledAttributes(attrs, R.styleable.SlideBarHorizontal) {
            moneyValue = getFloat(R.styleable.SlideBarHorizontal_moneyValue, 1024f)
            gapSize = getDimension(R.styleable.SlideBarHorizontal_gapSize, 24f)
            startPadding = getDimension(R.styleable.SlideBarHorizontal_startPadding, 80f)
            barColor = getColor(R.styleable.SlideBarHorizontal_barColor, Color.WHITE)
            anchorColor = getColor(R.styleable.SlideBarHorizontal_anchorColor, ANCHOR_COLOR)
        }
        slideBar.layoutParams =
            LayoutParams(LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        slideBar.moneyValue = moneyValue
        slideBar.gapSize = gapSize
        slideBar.startPadding = startPadding
        slideBar.barColor = barColor
        addView(slideBar)
        anchorBarLength = gapSize * 2
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawLine(
            anchorX, (height - anchorBarLength) / 2,
            anchorX, (height + anchorBarLength) / 2, paint
        )
    }

//    override fun onTouchEvent(event: MotionEvent): Boolean {
//        super.onTouchEvent(event)
//        motionTouchEventX = event.x
//        motionTouchEventY = event.y
//
//        when (event.action) {
//            MotionEvent.ACTION_DOWN -> touchStart()
//            MotionEvent.ACTION_MOVE -> touchMove()
//            MotionEvent.ACTION_UP -> touchUp()
//        }
//        return true
//    }
//
//    private fun touchStart() {
//        onSliderBarChangeListener?.onClickStart()
//    }
//
//    private fun touchMove() {
//        onSliderBarChangeListener?.onClickProgress(curMoney)
//    }
//
//    private fun touchUp() {
////        smoothScrollTo((floor(scrollX / gapSize) * gapSize).toInt(), scrollY)
//        onSliderBarChangeListener?.onClickStop()
//
//    }
}