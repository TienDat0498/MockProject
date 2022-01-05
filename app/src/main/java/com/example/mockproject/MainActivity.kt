package com.example.mockproject

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.graphics.Color
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.transition.AutoTransition
import android.transition.TransitionManager
import android.util.Log
import android.view.animation.LinearInterpolator
import android.widget.SeekBar
import android.widget.TextSwitcher
import android.widget.TextView
import androidx.core.animation.doOnEnd
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.example.slidebar.SlideBarHorizontal

private val statusText = listOf<String>("Normal", "That's a lot", "Are you crazy")
private val additionalInfoText = listOf<String>(
    "It's ok to eat at your place\nUp to 5% of economy",
    "Sometimes, you can eat in cafe\nUp to 3% of economy",
    "Eat in restaurants everyday\nBlow off 30% of money"
)

class MainActivity : AppCompatActivity() {
    private lateinit var sliderBar: SlideBarHorizontal
    private lateinit var moneyTextView: TextView
    private lateinit var moneyStatusText: TextSwitcher
    private lateinit var moneyStatusAdditional: TextSwitcher
    private var currentStatus = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val mainView = setContentView(R.layout.activity_main)
//        mainView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        hideSystemUI()
        sliderBar = findViewById(R.id.sliderBar)
        moneyTextView = findViewById(R.id.moneyText)

        setAnimator()
        init()
    }

    fun init(){
        moneyStatusText = findViewById(R.id.statusText)
        moneyStatusText.setFactory {
            val textView = TextView(this@MainActivity)
            textView.setTextColor(Color.WHITE)
            textView.textSize = resources.getDimension(R.dimen.text_size_small)/ resources.displayMetrics.density
            textView.setTypeface(null, Typeface.BOLD);
            textView
        }
        moneyStatusAdditional = findViewById(R.id.statusAdditionalText)
        moneyStatusAdditional.setFactory {
            val textView = TextView(this@MainActivity)
            textView.setTextColor(Color.WHITE)
            textView.textSize = resources.getDimension(R.dimen.text_size_super_small)/ resources.displayMetrics.density
            textView
        }
        moneyStatusText.setText(statusText[0])
        moneyStatusAdditional.setText(additionalInfoText[0])

    }

    private fun setAnimator(){
        val animatorFadeOut = ObjectAnimator.ofFloat(
            moneyTextView,
            "alpha",
            moneyTextView.alpha,
            0.3f
        ).apply {
            duration = 1000
        }

        val animatorFadeIn = ObjectAnimator.ofFloat(
            moneyTextView,
            "alpha",
            moneyTextView.alpha,
            1f
        ).apply {
            duration = 1000
        }

        sliderBar.setOnSliderBarChangeListener(object: SlideBarHorizontal.OnClickSlideBarListener{
            override fun onClickStart() {
                Log.d("AAAAA", moneyTextView.text.toString())

                animatorFadeOut.start()
            }

            override fun onClickProgress(moneyCur: Float) {
                moneyTextView.text = moneyCur.toInt().toString()

                when(moneyCur){
                    in 0f..1500f -> {
                        if (currentStatus!= 0) {
                            moneyStatusText.setText(statusText[0])
                            moneyStatusAdditional.setText(additionalInfoText[0])
                            currentStatus = 0

                        }

                    }
                    in 1500f..3000f -> {
                        if (currentStatus != 1){
                            moneyStatusText.setText(statusText[1])
                            moneyStatusAdditional.setText(additionalInfoText[1])
                            currentStatus = 1
                        }

                    }
                    else -> {
                        if (currentStatus != 2){
                            moneyStatusText.setText(statusText[2])
                            moneyStatusAdditional.setText(additionalInfoText[2])
                            currentStatus = 2
                        }
                    }
                }
            }

            override fun onClickStop() {
                val moneyProperty = ValueAnimator.ofInt(moneyTextView.text.toString().toInt(), sliderBar.curMoney.toInt()).apply {
                    duration = 150
                    addUpdateListener { updateAnimation ->
                        moneyTextView.text = (updateAnimation.animatedValue as Int).toString()
                    }
                    interpolator = LinearInterpolator()
                    start()
                }
                AnimatorSet().apply {
                    playSequentially(moneyProperty, animatorFadeIn)
                    start()
                }
            }

        })
    }

    override fun onResume() {
        super.onResume()

    }


    private fun hideSystemUI() {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        WindowInsetsControllerCompat(window, window.decorView).let { controller ->
            controller.hide(WindowInsetsCompat.Type.systemBars())
            controller.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
    }






}