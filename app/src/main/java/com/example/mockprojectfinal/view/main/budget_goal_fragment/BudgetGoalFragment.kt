package com.example.mockprojectfinal.view.main.budget_goal_fragment

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.category_slider.CategoryScrollView
import com.example.category_slider.SingleItem
import com.example.mockprojectfinal.databinding.FragmentBudgetGoalBinding
import com.example.mockprojectfinal.utils.*
import com.example.slidebar.SlideBarHorizontal
import dagger.hilt.android.AndroidEntryPoint
import java.text.DecimalFormat
import java.text.NumberFormat

@AndroidEntryPoint
class BudgetGoalFragment : Fragment() {
    companion object {
        const val FADE_ALPHA_MONEY_TEXT = 0.1f
        const val FULL_ALPHA = 1f
        const val LONG_ANIMATION_DURATION = 600L
        const val ANCHOR_MAX_SCALE = 1.15f
        const val ANCHOR_NORMAL_SCALE = 1f
    }

    private val budgetGoalViewModel: BudgetGoalViewModel by viewModels()

    private lateinit var binding: FragmentBudgetGoalBinding
    private lateinit var textViewAlphaAnimator: ValueAnimator
    private var textViewMoneyAnimator: ValueAnimator? = null
    private var anchorScaleAnimator: AnimatorSet? = null
    private val decelerateInterpolator = DecelerateInterpolator()

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
        initAnimator()
        initObserver()
        initView()
    }

    private fun initObserver() {
        budgetGoalViewModel.listSingleItem.observe(viewLifecycleOwner, { listSingleItem ->
            binding.horizontalScrollView.data = listSingleItem
        })
        budgetGoalViewModel.curStatus.observe(viewLifecycleOwner, { moneySpendInformation ->
            when (moneySpendInformation) {
                MoneySpendInformation.NORMAL -> {
                    binding.statusTextNormal.showUpFromBottom()
                    binding.statusAdditionalTextNormal.showUpFromBottom()
                    if (budgetGoalViewModel.lastStatus == MoneySpendInformation.LOT) {
                        binding.statusTextLot.getBackToTop()
                        binding.statusAdditionalTextLot.getBackToTop()
                    } else if (budgetGoalViewModel.lastStatus == MoneySpendInformation.CRAZY) {
                        binding.statusTextCrazy.getBackToTop()
                        binding.statusAdditionalTextCrazy.getBackToTop()
                    }
                }
                MoneySpendInformation.LOT -> {
                    when (budgetGoalViewModel.lastStatus) {
                        MoneySpendInformation.CRAZY -> {
                            binding.statusTextLot.showUpFromBottom()
                            binding.statusAdditionalTextLot.showUpFromBottom()
                            binding.statusTextCrazy.getBackToTop()
                            binding.statusAdditionalTextCrazy.getBackToTop()
                        }
                        MoneySpendInformation.NORMAL -> {
                            binding.statusTextLot.showUpFromTop()
                            binding.statusAdditionalTextLot.showUpFromTop()
                            binding.statusTextNormal.getBackToBottom()
                            binding.statusAdditionalTextNormal.getBackToBottom()
                        }
                        else -> {
                            binding.statusTextLot.showUpFromTop()
                            binding.statusAdditionalTextLot.showUpFromTop()
                        }
                    }
                }
                MoneySpendInformation.CRAZY -> {
                    binding.statusTextCrazy.showUpFromTop()
                    binding.statusAdditionalTextCrazy.showUpFromTop()
                    if (budgetGoalViewModel.lastStatus == MoneySpendInformation.NORMAL) {
                        binding.statusTextNormal.getBackToBottom()
                        binding.statusAdditionalTextNormal.getBackToBottom()
                    } else if (budgetGoalViewModel.lastStatus == MoneySpendInformation.LOT) {
                        binding.statusTextLot.getBackToBottom()
                        binding.statusAdditionalTextLot.getBackToBottom()
                    }
                }
                else -> {
                }
            }
        })
        budgetGoalViewModel.curValue.observe(viewLifecycleOwner, { moneyValue ->
            budgetGoalViewModel.updateCurStatus(moneyValue)
            updateMoneyTextView(moneyValue)
        })
    }

    private fun initAnimator() {
        textViewAlphaAnimator =
            ValueAnimator.ofFloat(binding.moneyText.alpha, FADE_ALPHA_MONEY_TEXT, FULL_ALPHA)
                .apply {
                    addUpdateListener { updateAnimation ->
                        binding.moneyText.alpha = updateAnimation.animatedValue as Float
                    }
                    interpolator = decelerateInterpolator
                    duration = LONG_ANIMATION_DURATION
                }
    }

    private fun initView() {
        binding.buttonSave.setOnClickListener {
            budgetGoalViewModel.updateDatabase(binding.sliderBar.curMoney)
        }

        binding.horizontalScrollView.onSwipeListener = object : CategoryScrollView.OnSwipeListener {
            override fun onCategoryChange(item: SingleItem) {
                budgetGoalViewModel.setCurrentValue(item.value)
                binding.sliderBar.curMoney = item.value
                budgetGoalViewModel.currentCategory.value = item
            }
        }

        binding.sliderBar.setOnValueChangeListener(object :
            SlideBarHorizontal.OnValueChangeListener {
            override fun onValueChange(currentValue: Int) {
                updateMoneyTextView(currentValue)
                animationAnchor()
                budgetGoalViewModel.setCurrentValue(currentValue)
            }
        })
    }

    private fun animationAnchor() {
        anchorScaleAnimator?.cancel()
        val anchorScaleXAnimator =
            ObjectAnimator.ofFloat(
                binding.anchor,
                View.SCALE_X,
                ANCHOR_MAX_SCALE,
                ANCHOR_NORMAL_SCALE
            )
        val anchorScaleYAnimator =
            ObjectAnimator.ofFloat(
                binding.anchor,
                View.SCALE_Y,
                ANCHOR_MAX_SCALE,
                ANCHOR_NORMAL_SCALE
            )

        anchorScaleAnimator = AnimatorSet().apply {
            playTogether(anchorScaleXAnimator, anchorScaleYAnimator)
            interpolator = decelerateInterpolator
            start()
        }
    }

    private fun updateMoneyTextView(newValue: Int) {
        textViewAlphaAnimator.cancel()
        textViewMoneyAnimator?.cancel()

        textViewMoneyAnimator =
            ValueAnimator.ofInt(binding.moneyText.text.toString().toInt(), newValue).apply {
                addUpdateListener { updateAnimation ->
                    binding.moneyText.text = (updateAnimation.animatedValue as Int).toString()
                }
                interpolator = decelerateInterpolator
                duration = LONG_ANIMATION_DURATION
            }
        textViewMoneyAnimator?.start()
        textViewAlphaAnimator.start()
    }
}