package com.example.slidebar

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.HorizontalScrollView
import androidx.core.content.withStyledAttributes
import com.example.slider_bar_horizontal.R
import kotlin.math.ceil


class SlideBarHorizontal @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : HorizontalScrollView(context, attrs, defStyleAttr) {

    interface OnValueChangeListener {
        fun onValueChange(currentValue: Int)
    }

    companion object {
        const val DEFAULT_MONEY = 1024f
        const val DEFAULT_GAP_SIZE = 40f
        const val DEFAULT_START_PADDING = 80f
        const val DEFAULT_BAR_COLOR = Color.WHITE
    }

    private var moneyValue: Float = DEFAULT_MONEY
    private var gapSize: Float = DEFAULT_GAP_SIZE
    private var startPadding: Float = DEFAULT_START_PADDING
    private var barColor: Int = DEFAULT_BAR_COLOR

    private val slideBar = SlideBar(context)
    private var anchorBarLength: Float

    private var onValueChangeListener: OnValueChangeListener? = null

    fun setOnValueChangeListener(listener: OnValueChangeListener) {
        onValueChangeListener = listener
        viewTreeObserver.addOnScrollChangedListener {
            onValueChangeListener?.onValueChange(curMoney)
        }
    }

    var curMoney: Int = 0
        set(value) {
            smoothScrollTo(ceil(value.toFloat() / SlideBar.GAP_VALUE * gapSize).toInt(), scrollY)
            field = value
        }
        get() = ((scrollX.toFloat() / gapSize) * SlideBar.GAP_VALUE).toInt()

    init {
        isClickable = true
        context.withStyledAttributes(attrs, R.styleable.SlideBarHorizontal) {
            moneyValue = getFloat(R.styleable.SlideBarHorizontal_moneyValue, 1024f)
            gapSize = getDimension(R.styleable.SlideBarHorizontal_gapSize, 24f)
            startPadding = getDimension(R.styleable.SlideBarHorizontal_startPadding, 80f)
            barColor = getColor(R.styleable.SlideBarHorizontal_barColor, Color.WHITE)
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
    }
}