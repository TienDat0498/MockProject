package com.example.category_slider

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.ViewGroup
import android.widget.HorizontalScrollView
import kotlin.math.abs

class CategoryScrollView
@JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : HorizontalScrollView(context, attrs, defStyleAttr) {

    interface OnSwipeListener {
        fun onCategoryChange(item: SingleItem)
    }

    private var currentItem: Int = 0
    private var countChild = 0

    private var motionTouchEventXDown = 0f
    private var motionTouchEventXUp = 0f

    var onSwipeListener: OnSwipeListener? = null

    private val expandSlideView = ExpandSlideView(context)
    var data: List<SingleItem>? = null
        set(value) {

            if (value == null) return
            currentItem = 0
            countChild = value.size
            expandSlideView.data = value
            field = value
        }

    init {
        isHorizontalScrollBarEnabled = false
        isVerticalScrollBarEnabled = false

        expandSlideView.layoutParams =
            LayoutParams(LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
//        expandSlideView.setPadding(paddingLeft, 0, -paddingLeft, 0)
        addView(expandSlideView)
    }

    private fun onNextItem() {
        if (currentItem >= countChild - 1) return
        currentItem += 1
        if (onSwipeListener != null) {
            if (countChild > 0)
                onSwipeListener!!.onCategoryChange(data!![currentItem])
        }
        smoothScrollTo(expandSlideView.oneNormalItemWidth * currentItem, scrollY)
        expandSlideView.nextItem()

    }

    private fun onPrevItem() {
        if (currentItem == 0) return
        currentItem -= 1
        if (onSwipeListener != null) {
            if (countChild > 0)
                onSwipeListener!!.onCategoryChange(data!![currentItem])
        }
        smoothScrollTo(expandSlideView.oneNormalItemWidth * currentItem, scrollY)
        expandSlideView.previousItem()
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                motionTouchEventXDown = event.x
                return true
            }
            MotionEvent.ACTION_UP -> {
                motionTouchEventXUp = event.x
                val deltaX = motionTouchEventXDown - motionTouchEventXUp

                if (abs(deltaX) > 150) {
                    if (deltaX < 0) {
                        onPrevItem()
                    } else {
                        onNextItem()
                    }
                }
                return true
            }
        }
        return false
    }
}