package com.example.category_slider

import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Color
import android.transition.ChangeBounds
import android.transition.Transition
import android.transition.TransitionManager
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import java.text.DecimalFormat
import java.text.NumberFormat
import kotlin.math.max
import kotlin.math.min

internal class ExpandSlideView
@JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ViewGroup(context, attrs, defStyleAttr) {

    private var currentItem: Int = 0
    private val listViewHolder = mutableListOf<ViewHolder>()
    var oneNormalItemWidth = Int.MAX_VALUE

    var data: List<SingleItem>? = null
        set(value) {
            if (value == null) return
            for (singleItem in value) {
                val newViewHolder = ViewHolder.create(singleItem, this)
                listViewHolder.add(newViewHolder)
                addView(newViewHolder.view)
            }
            field = value
            currentItem = 0
            listViewHolder[currentItem].setCurrentView()
            requestLayout()
        }


    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        var curWidth = 0
        var curHeight = 0
        var curLeft = 0
        var curTop = 0

        val childLeft = this.paddingLeft
        val childTop = this.paddingTop
        val childRight = this.measuredWidth - this.paddingRight
        val childBottom = this.measuredHeight - this.paddingBottom
        val childWidth = childRight - childLeft
        val childHeight = childBottom - childTop


        curLeft = childLeft
        curTop = childTop

        for (idx in 0 until childCount) {
            val childView: View = getChildAt(idx)
            if (childView.visibility == View.GONE) {
                continue
            }

            childView.measure(
                MeasureSpec.makeMeasureSpec(childWidth, MeasureSpec.AT_MOST),
                MeasureSpec.makeMeasureSpec(childHeight, MeasureSpec.AT_MOST)
            )

            curWidth = childView.measuredWidth
            oneNormalItemWidth = min(oneNormalItemWidth, curWidth)
            curHeight = childView.measuredHeight
            childView.layout(curLeft, curTop, curLeft + curWidth, curTop + curHeight)
            curLeft += curWidth
        }
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        var maxHeight: Int = 0
        var maxWidth: Int = 0

        val paddingVertical = this.paddingTop + this.paddingBottom
        val paddingHorizontal = this.paddingStart + this.paddingEnd

        for (idx in 0 until childCount) {
            val childView: View = getChildAt(idx)
            if (childView.visibility == View.GONE) {
                continue
            }
            measureChild(childView, widthMeasureSpec, heightMeasureSpec)
            maxHeight = max(childView.measuredHeight, maxHeight)
            maxWidth += childView.measuredWidth
        }
        setMeasuredDimension(maxWidth + paddingHorizontal + widthSize, maxHeight + paddingVertical)
    }

    fun nextItem() {
        if (currentItem >= childCount - 1) {
            return
        }
        listViewHolder[currentItem].unsetCurrentView()
        currentItem += 1
        listViewHolder[currentItem].setCurrentView()
        requestLayout()
    }

    fun previousItem() {
        if (currentItem == 0) return
        listViewHolder[currentItem].unsetCurrentView()
        currentItem -= 1
        listViewHolder[currentItem].setCurrentView()
        requestLayout()
    }

    class ViewHolder private constructor(val view: View) {
        private val rootLayout: ConstraintLayout = view.findViewById(R.id.rootLayout)
        private val layoutCurrent: ConstraintLayout = view.findViewById(R.id.constraintCurrent)
        private val imageIconCurrent: ImageView = view.findViewById(R.id.imageIconCurrent)
        private val textCategoryName: TextView = view.findViewById(R.id.textCategoryName)
        private val textCategoryExpand: TextView = view.findViewById(R.id.textCategoryExpand)
        private lateinit var item: SingleItem

        private lateinit var currentLayoutBackgroundAnimator: ValueAnimator

        private lateinit var currentIconTintAnimator: ValueAnimator

        private lateinit var currentIconBackGroundAnimator: ValueAnimator

        private lateinit var notCurrentLayoutBackgroundAnimator: ValueAnimator

        private lateinit var notCurrentIconTintAnimator: ValueAnimator

        private lateinit var notCurrentIconBackGroundAnimator: ValueAnimator


        private lateinit var showUpAnimator: AnimatorSet

        private lateinit var getDownAnimator: AnimatorSet

        private lateinit var revealTransition: Transition

        private lateinit var hideTransition: Transition

        private var isCurrent = false


        companion object {
            fun create(singleItem: SingleItem, parent: ViewGroup): ViewHolder {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.sliding_category_item, parent, false)
                return ViewHolder(view).apply {
                    initView(singleItem, parent.context)
                    initAnimator(parent.context)
                }
            }
        }

        private fun initView(singleItem: SingleItem, context: Context) {
            item = singleItem
            imageIconCurrent.setImageResource(singleItem.icon)
            imageIconCurrent.setColorFilter(Color.parseColor(singleItem.colorFirst))
            imageIconCurrent.setBackgroundColor(Color.WHITE)
            layoutCurrent.setBackgroundColor(Color.WHITE)
            textCategoryName.text = singleItem.name


            val formatter: NumberFormat = DecimalFormat("#,###")
            val formattedNumber: String = formatter.format(singleItem.value)
            textCategoryExpand.text =
                context.resources.getString(R.string.text_category_value, formattedNumber)
            textCategoryName.visibility = View.GONE
            textCategoryExpand.visibility = View.GONE
        }

        private fun initAnimator(context: Context) {
            currentLayoutBackgroundAnimator =
                ValueAnimator.ofArgb(Color.WHITE, Color.parseColor(item.colorFirst)).apply {
                    startDelay = 300
                    duration = 300
                    addUpdateListener { updateAnimation ->
                        layoutCurrent.setBackgroundColor(updateAnimation.animatedValue as Int)
                    }

                }

            currentIconTintAnimator =
                ValueAnimator.ofArgb(Color.parseColor(item.colorFirst), Color.WHITE).apply {
                    duration = 300
                    addUpdateListener { updateAnimation ->
                        imageIconCurrent.setColorFilter(updateAnimation.animatedValue as Int)
                    }
                }

            currentIconBackGroundAnimator =
                ValueAnimator.ofArgb(Color.WHITE, Color.parseColor(item.colorSecond)).apply {
                    startDelay = 300
                    duration = 300
                    addUpdateListener { updateAnimation ->
                        imageIconCurrent.setBackgroundColor(updateAnimation.animatedValue as Int)
                    }
                }

            notCurrentLayoutBackgroundAnimator =
                ValueAnimator.ofArgb(Color.parseColor(item.colorFirst), Color.WHITE).apply {
                    duration = 350
                    addUpdateListener { updateAnimation ->
                        layoutCurrent.setBackgroundColor(updateAnimation.animatedValue as Int)
                    }
                }

            notCurrentIconTintAnimator =
                ValueAnimator.ofArgb(Color.WHITE, Color.parseColor(item.colorFirst)).apply {
                    startDelay = 250
                    duration = 350
                    addUpdateListener { updateAnimation ->
                        imageIconCurrent.setColorFilter(updateAnimation.animatedValue as Int)
                    }
                }

            notCurrentIconBackGroundAnimator =
                ValueAnimator.ofArgb(Color.parseColor(item.colorSecond), Color.WHITE).apply {
                    duration = 250
                    addUpdateListener { updateAnimation ->
                        imageIconCurrent.setBackgroundColor(updateAnimation.animatedValue as Int)
                    }
                }

            val decelerateInterpolator = DecelerateInterpolator()
            showUpAnimator = AnimatorSet().apply {
                playTogether(
                    currentLayoutBackgroundAnimator,
                    currentIconTintAnimator,
                    currentIconBackGroundAnimator
                )
                interpolator = decelerateInterpolator
            }

            getDownAnimator = AnimatorSet().apply {
                playTogether(
                    notCurrentLayoutBackgroundAnimator,
                    notCurrentIconTintAnimator,
                    notCurrentIconBackGroundAnimator
                )
                interpolator = decelerateInterpolator
            }

            revealTransition = ChangeBounds().apply {
                interpolator = decelerateInterpolator
                duration = 600
            }

            hideTransition = ChangeBounds().apply {
                interpolator = decelerateInterpolator
                duration = 600
            }

        }

        fun setCurrentView() {
            if (!isCurrent) {
                showUpAnimator.start()
                TransitionManager.beginDelayedTransition(rootLayout, revealTransition)
                textCategoryName.visibility = View.VISIBLE
                textCategoryExpand.visibility = View.VISIBLE
                isCurrent = true
            }
        }

        fun unsetCurrentView() {
            if (isCurrent) {
                getDownAnimator.start()
                TransitionManager.beginDelayedTransition(rootLayout, hideTransition)
                textCategoryName.visibility = View.GONE
                textCategoryExpand.visibility = View.GONE
                isCurrent = false
            }
        }
    }
}