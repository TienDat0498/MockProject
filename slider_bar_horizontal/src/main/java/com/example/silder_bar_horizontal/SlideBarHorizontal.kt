package com.example.slidebar

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.HorizontalScrollView
import androidx.core.content.withStyledAttributes
import com.example.silder_bar_horizontal.R
import kotlin.math.floor

private const val ANCHOR_STROKE_WIDTH = 16f
private val ANCHOR_COLOR = Color.parseColor("#4ce2eb")

class SlideBarHorizontal @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : HorizontalScrollView(context, attrs, defStyleAttr) {

    interface OnClickSlideBarListener {
        fun onClickStart()
        fun onClickProgress(moneyCur: Float)
        fun onClickStop()
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

    private var motionTouchEventX: Float = 0f
    private var motionTouchEventY: Float = 0f
    private var onClickListener: OnClickSlideBarListener? = null

    fun setOnSliderBarChangeListener(listener: OnClickSlideBarListener){
        onClickListener = listener
    }

    var curMoney: Float = 0f
        private set
        get() = (floor(scrollX / gapSize) * gapSize).toInt() / gapSize * SlideBar.GAP_VALUE

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

    override fun onTouchEvent(event: MotionEvent): Boolean {
        super.onTouchEvent(event)
        motionTouchEventX = event.x
        motionTouchEventY = event.y

        when (event.action) {
            MotionEvent.ACTION_DOWN -> touchStart()
            MotionEvent.ACTION_MOVE -> touchMove()
            MotionEvent.ACTION_UP -> touchUp()
        }
        return true
    }

    private fun touchStart() {
        onClickListener?.onClickStart()
    }

    private fun touchMove() {
        onClickListener?.onClickProgress(curMoney)
    }

    private fun touchUp() {
        smoothScrollTo((floor(scrollX / gapSize) * gapSize).toInt(), scrollY)
        onClickListener?.onClickStop()

    }

    class SlideBar @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
    ) : View(context, attrs, defStyleAttr) {
        companion object {
            const val GAP_VALUE: Int = 10
            private const val STROKE_WIDTH = 4f
        }


        var moneyValue: Float = 1024f
        var gapSize: Float = 24f
            set(value) {
                field = value
                shortBar = gapSize / 8
                longBar = gapSize / 2
            }
        private var shortBar: Float = gapSize / 8
        private var longBar: Float = gapSize / 2
        var startPadding: Float = 80f
        var barColor: Int = Color.WHITE
            set(value) {
                field = value
                paint.color = value
            }


        private val paint = Paint().apply {
            color = barColor
            isAntiAlias = true
            style = Paint.Style.STROKE
            strokeCap = Paint.Cap.ROUND
            strokeWidth = STROKE_WIDTH
        }

        init {
            context.withStyledAttributes(attrs, R.styleable.SlideBar) {
                moneyValue = getFloat(R.styleable.SlideBar_moneyValueSlideBar, 1024f)
                gapSize = getDimension(R.styleable.SlideBar_gapSizeSlideBar, 24f)
                startPadding = getDimension(R.styleable.SlideBar_startPaddingSlideBar, 80f)
                barColor = getColor(R.styleable.SlideBar_barColorSlideBar, Color.WHITE)
            }
        }


        override fun onDraw(canvas: Canvas) {
            super.onDraw(canvas)
            var xDimen = startPadding
            for (i in 0..(moneyValue / GAP_VALUE).toInt()) {
                if (i % 5 == 0)
                    canvas.drawLine(
                        xDimen,
                        (height - longBar) / 2,
                        xDimen,
                        (height + longBar) / 2,
                        paint
                    )
                else
                    canvas.drawLine(
                        xDimen,
                        (height - shortBar) / 2,
                        xDimen,
                        (height + shortBar) / 2,
                        paint
                    )
                xDimen += gapSize

            }
        }

        override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {

            val widthMode = MeasureSpec.getMode(widthMeasureSpec)
            val widthSize = MeasureSpec.getSize(widthMeasureSpec)
            val heightMode = MeasureSpec.getMode(heightMeasureSpec)
//        val heightSize = MeasureSpec.getSize(heightMeasureSpec)

            val width: Int
            val height: Int

            //Measure Width
            when(widthMode){
                MeasureSpec.EXACTLY -> {
                    // For further implementation, complete this path, by default
                    // only use self define height and width
                    throw  NotImplementedError(
                        "Not support this width mode, only support " +
                                "wrap_content of horizontal scroll view"
                    )
                }
                MeasureSpec.AT_MOST -> {
                    // For further implementation, complete this path, by default
                    // only use self define height and width
                    throw  NotImplementedError(
                        "Not support this width mode, only support " +
                                "wrap_content of horizontal scroll view"
                    )
                }
                else -> //Be whatever you want
                    width = widthSize + (moneyValue / GAP_VALUE * gapSize).toInt()
            }

            //Measure Height
            when(heightMode){
                MeasureSpec.EXACTLY -> {
                    // For further implementation, complete this path, by default
                    // only use self define height and width
                    throw  NotImplementedError(
                        "Not support this height mode, only support " +
                                "wrap_content of horizontal scroll view"
                    )
                }
                MeasureSpec.AT_MOST-> {
                    // for using in horizontal scroll view, mode will be at most of height
                    height = (gapSize * 6).toInt()
                }

                else -> {
                    // For further implementation, complete this path, by default
                    // only use self define height and width
                    throw  NotImplementedError(
                        "Not support this height mode, only support " +
                                "wrap_content of horizontal scroll view"
                    )
                }
            }
            setMeasuredDimension(width, height)
        }
    }
}