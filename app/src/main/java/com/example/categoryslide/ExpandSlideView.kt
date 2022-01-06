package com.example.categoryslide

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import java.text.DecimalFormat
import java.text.NumberFormat
import kotlin.math.max

class ExpandSlideView
    @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0)
: ViewGroup(context, attrs, defStyleAttr){

    private var currentItem: Int = 0
    private val listViewHolder = mutableListOf<ViewHolder>()

    var data: List<SingleItem>? = null
        set(value) {
            if(value==null) return
            for (singleItem in value){
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
        var curWidth =0
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

        for (idx in 0 until childCount){
            val childView: View = getChildAt(idx)
            if (childView.visibility == View.GONE){
                continue
            }

            childView.measure(MeasureSpec.makeMeasureSpec(childWidth, MeasureSpec.AT_MOST),
                MeasureSpec.makeMeasureSpec(childHeight, MeasureSpec.AT_MOST))

            curWidth = childView.measuredWidth
            curHeight = childView.measuredHeight
            childView.layout(curLeft, curTop, curLeft+curWidth, curTop+curHeight)
            curLeft+= curWidth

        }
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var maxHeight: Int = 0
        var maxWidth: Int = 0

        val paddingVertical = this.paddingTop+ this.paddingBottom
        val paddingHorizontal = this.paddingStart+ this.paddingEnd

        for (idx in 0 until childCount) {
            val childView: View = getChildAt(idx)
            if (childView.visibility == View.GONE){
                continue
            }

            measureChild(childView, widthMeasureSpec, heightMeasureSpec)
            maxHeight = max(childView.measuredHeight, maxHeight)
            maxWidth += childView.measuredWidth
        }

        setMeasuredDimension(maxWidth+paddingHorizontal, maxHeight+paddingVertical)

    }

    fun nextItem(){
        Log.d("currentItem", currentItem.toString())
        if (currentItem >= childCount-1){
            return
        }
        listViewHolder[currentItem].unsetCurrentView()
        currentItem += 1
        listViewHolder[currentItem].setCurrentView()
        requestLayout()
    }

    fun previousItem(){
        Log.d("currentItem", currentItem.toString())
        if (currentItem == 0) return
        listViewHolder[currentItem].unsetCurrentView()
        currentItem -=1
        listViewHolder[currentItem].setCurrentView()
        requestLayout()
    }

    class ViewHolder private constructor(val view: View){
        private val imageIconNotCurrent: ImageView = view.findViewById(R.id.imageIcon)
        private val layoutCurrent: ConstraintLayout = view.findViewById(R.id.constraintCurrent)
        private val imageIconCurrent: ImageView = view.findViewById(R.id.imageIconCurrent)
        private val textCategoryName: TextView = view.findViewById(R.id.textCategoryName)
        private val textCategoryExpand: TextView = view.findViewById(R.id.textCategoryExpand)

        private val fadeInAnimatorAlphaChosen = ObjectAnimator.ofFloat(layoutCurrent, "alpha", 0f, 1f)
        private val fadeOutAnimatorAlphaChosen = ObjectAnimator.ofFloat(layoutCurrent, "alpha", 1f, 0f)
        private val scaleUpXAnimatorChosen = ObjectAnimator.ofFloat(layoutCurrent, "scaleX", 0.5f, 1f)
        private val scaleUpYAnimatorChosen = ObjectAnimator.ofFloat(layoutCurrent, "scaleY", 0.7f, 1f)
        private val scaleDownXAnimatorChosen = ObjectAnimator.ofFloat(layoutCurrent, "scaleX", 1f, 0f)
        private val scaleDownYAnimatorChosen = ObjectAnimator.ofFloat(layoutCurrent, "scaleY", 1f, 0.5f)

        private var isCurrent = false

        private val showUpAnimator = AnimatorSet().apply {
            playTogether(fadeInAnimatorAlphaChosen, scaleUpXAnimatorChosen,scaleUpYAnimatorChosen)
            addListener(object: Animator.AnimatorListener{
                override fun onAnimationStart(p0: Animator?) {
                    layoutCurrent.visibility = View.VISIBLE
                }

                override fun onAnimationEnd(p0: Animator?) {
                }

                override fun onAnimationCancel(p0: Animator?) {
                }

                override fun onAnimationRepeat(p0: Animator?) {
                }

            })
        }

        private val getDownAnimator = AnimatorSet().apply {
            playTogether(fadeOutAnimatorAlphaChosen, scaleDownXAnimatorChosen,scaleDownYAnimatorChosen)
            addListener(object: Animator.AnimatorListener{
                override fun onAnimationStart(p0: Animator?) {

                }

                override fun onAnimationEnd(p0: Animator?) {
                    layoutCurrent.visibility = View.GONE
                }

                override fun onAnimationCancel(p0: Animator?) {
                }

                override fun onAnimationRepeat(p0: Animator?) {
                }

            })
        }

        companion object {
            fun create(singleItem: SingleItem, parent: ViewGroup): ViewHolder {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.sliding_category_item, parent, false)
                return ViewHolder(view).apply {
                    initView(singleItem, parent.context)
                }
            }
        }

        private fun initView(singleItem: SingleItem, context: Context){
            imageIconNotCurrent.setImageResource(singleItem.icon)
            imageIconCurrent.setImageResource(singleItem.icon)
            imageIconNotCurrent.setColorFilter(Color.parseColor(singleItem.colorFirst))
            layoutCurrent.setBackgroundColor(Color.parseColor(singleItem.colorFirst))
            imageIconCurrent.setBackgroundColor(Color.parseColor(singleItem.colorSecond))
            textCategoryName.text = singleItem.name

            val formatter: NumberFormat = DecimalFormat("#,###")
            val formattedNumber: String = formatter.format(singleItem.value)
            textCategoryExpand.text = context.resources.getString(R.string.text_category_value, formattedNumber)

        }

        fun setCurrentView(){
            if (!isCurrent){
                showUpAnimator.start()
                isCurrent = true
            }

        }

        fun unsetCurrentView(){
            if (isCurrent){
                getDownAnimator.start()
                isCurrent = false
            }

        }
    }

    data class SingleItem(
        val name: String,
        val icon: Int,
        val value: Int,
        val colorFirst: String,
        val colorSecond: String
    )
}