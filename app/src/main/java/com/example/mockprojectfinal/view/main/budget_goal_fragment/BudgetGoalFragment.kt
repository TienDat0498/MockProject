package com.example.mockprojectfinal.view.main.budget_goal_fragment

import android.animation.ValueAnimator
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.example.category_slider.CategoryScrollView
import com.example.category_slider.SingleItem
import com.example.mockprojectfinal.R
import com.example.mockprojectfinal.databinding.FragmentBudgetGoalBinding
import com.example.slidebar.SlideBarHorizontal
import dagger.hilt.android.AndroidEntryPoint

private val statusText = listOf<String>("Normal", "That's a lot", "Are you crazy")
private val additionalInfoText = listOf<String>(
    "It's ok to eat at your place\nUp to 5% of economy",
    "Sometimes, you can eat in cafe\nUp to 3% of economy",
    "Eat in restaurants everyday\nBlow off 30% of money"
)

@AndroidEntryPoint
class BudgetGoalFragment : Fragment() {
    private val budgetGoalViewModel: BudgetGoalViewModel by viewModels()

    private lateinit var binding: FragmentBudgetGoalBinding
    private lateinit var textViewAlphaAnimator: ValueAnimator
    private var textViewMoneyAnimator: ValueAnimator? = null
    private val decelerateInterpolator = DecelerateInterpolator()
    private var currentStatus = 0
    private var curValue = 0


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBudgetGoalBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObserver()
        initView()

    }

    private fun initObserver() {

    }

    private fun initView() {
        textViewAlphaAnimator = ValueAnimator.ofFloat(binding.moneyText.alpha, 0.3f, 1f).apply {
            addUpdateListener { updateAnimation ->
                binding.moneyText.alpha = updateAnimation.animatedValue as Float
            }
            interpolator = decelerateInterpolator
            duration = 600
        }

        val listData = listOf<SingleItem>(
            SingleItem("Cafe", R.drawable.restaurant, 400, "#4238ed", "#2b22b6"),
            SingleItem("House", R.drawable.home, 600, "#35e9d4", "#37dbc3"),
            SingleItem("Taxi", R.drawable.taxi, 900, "#fcbc40", "#e8a82a"),
            SingleItem("Gym", R.drawable.weightlifter, 500, "#FF8A65", "#FF5722"),
            SingleItem("Love", R.drawable.relationship, 700, "#EF5350", "#E53935"),
            SingleItem("Other", R.drawable.ic_other, 800, "#BDBDBD", "#757575"),
        )
        binding.horizontalScrollView.data = listData

        binding.horizontalScrollView.onSwipeListener = object: CategoryScrollView.OnSwipeListener{
            override fun onCategoryChange(item: SingleItem) {
                curValue = item.value
                updateMoneyTextView(curValue)
                binding.sliderBar.curMoney = curValue.toFloat()
            }
        }

        binding.sliderBar.curMoney = curValue.toFloat()

        binding.sliderBar.setOnValueChangeListener(object :
            SlideBarHorizontal.OnValueChangeListener {
            override fun onValueChange(currentValue: Float) {
                updateMoneyTextView(currentValue.toInt())
                when(currentValue){
                    in 0f..curValue.toFloat() -> {
                        if (currentStatus!= 0) {
                            binding.statusText.setText(statusText[0])
                            binding.statusAdditionalText.setText(additionalInfoText[0])
                            currentStatus = 0

                        }

                    }
                    in 1f*curValue.. 2f*curValue-> {
                        if (currentStatus != 1){
                            binding.statusText.setText(statusText[1])
                            binding.statusAdditionalText.setText(additionalInfoText[1])
                            currentStatus = 1
                        }

                    }
                    else -> {
                        if (currentStatus != 2){
                            binding.statusText.setText(statusText[2])
                            binding.statusAdditionalText.setText(additionalInfoText[2])
                            currentStatus = 2
                        }
                    }
                }
            }
        })

        binding.statusText.setFactory {
            val textView = TextView(requireContext())
            textView.setTextColor(Color.WHITE)
            textView.textSize = resources.getDimension(R.dimen.text_size_small)/ resources.displayMetrics.density
            textView.setTypeface(null, Typeface.BOLD);
            textView
        }

        binding.statusAdditionalText.setFactory {
            val textView = TextView(requireContext())
            textView.setTextColor(Color.WHITE)
            textView.textSize = resources.getDimension(R.dimen.text_size_super_small)/ resources.displayMetrics.density
            textView
        }
    }

    private fun updateMoneyTextView(newValue: Int) {
        textViewAlphaAnimator.cancel()
        textViewMoneyAnimator?.cancel()

        textViewMoneyAnimator = ValueAnimator.ofInt(binding.moneyText.text.toString().toInt(), newValue).apply {
            addUpdateListener { updateAnimation ->
                binding.moneyText.text = (updateAnimation.animatedValue as Int).toString()
            }
            interpolator = decelerateInterpolator
            duration = 600
        }

        textViewMoneyAnimator?.start()
        textViewAlphaAnimator.start()

    }
}