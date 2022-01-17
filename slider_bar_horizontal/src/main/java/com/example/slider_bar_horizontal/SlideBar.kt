package com.example.slidebar

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import androidx.core.content.withStyledAttributes
import com.example.slider_bar_horizontal.R


internal class SlideBar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    companion object {
        const val GAP_SIZE = 40f
        const val GAP_VALUE: Int = 25
        private const val STROKE_WIDTH = 4f
        const val SHORT_BAR_GAP_SIZE_PROPORTION = 1f / 10f
        const val LONG_BAR_GAP_SIZE_PROPORTION = 1f / 3f
        const val LONG_BAR_PER_BAR = 4
        const val VIEW_HEIGHT_GAP_SIZE_PROPORTION = 3

        const val DEFAULT_MONEY = 1024f
        const val DEFAULT_START_PADDING = 80f
        const val DEFAULT_BAR_COLOR = Color.WHITE

    }

    var moneyValue: Float = DEFAULT_MONEY
    var gapSize: Float = GAP_SIZE
        set(value) {
            field = value
            shortBar = gapSize * SHORT_BAR_GAP_SIZE_PROPORTION
            longBar = gapSize * LONG_BAR_GAP_SIZE_PROPORTION
        }
    private var shortBar: Float = gapSize * SHORT_BAR_GAP_SIZE_PROPORTION
    private var longBar: Float = gapSize * LONG_BAR_GAP_SIZE_PROPORTION
    var startPadding: Float = DEFAULT_START_PADDING
    var barColor: Int = DEFAULT_BAR_COLOR
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
            moneyValue = getFloat(R.styleable.SlideBar_moneyValueSlideBar, DEFAULT_MONEY)
            gapSize = getDimension(R.styleable.SlideBar_gapSizeSlideBar, GAP_SIZE)
            startPadding =
                getDimension(R.styleable.SlideBar_startPaddingSlideBar, DEFAULT_START_PADDING)
            barColor = getColor(R.styleable.SlideBar_barColorSlideBar, DEFAULT_BAR_COLOR)
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        var xDimen = startPadding
        for (i in 0..(moneyValue / GAP_VALUE).toInt()) {
            if (i % LONG_BAR_PER_BAR == 0)
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
        when (widthMode) {
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
        when (heightMode) {
            MeasureSpec.EXACTLY -> {
                // For further implementation, complete this path, by default
                // only use self define height and width
                throw  NotImplementedError(
                    "Not support this height mode, only support " +
                            "wrap_content of horizontal scroll view"
                )
            }
            MeasureSpec.AT_MOST -> {
                // for using in horizontal scroll view, mode will be at most of height
                height = (gapSize * VIEW_HEIGHT_GAP_SIZE_PROPORTION).toInt()
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