package com.example.mockprojectfinal.utils

import android.animation.ObjectAnimator
import android.view.View
import android.widget.TextView
import com.example.mockprojectfinal.utils.Constant.LONG_ANIMATION_DURATION
import com.example.mockprojectfinal.utils.Constant.TEXT_ALPHA_FADE
import com.example.mockprojectfinal.utils.Constant.TEXT_ALPHA_SHOW
import com.example.mockprojectfinal.utils.Constant.TEXT_TRANSITION_BOTTOM
import com.example.mockprojectfinal.utils.Constant.TEXT_TRANSITION_DEFAULT
import com.example.mockprojectfinal.utils.Constant.TEXT_TRANSITION_TOP

fun TextView.showUpFromBottom() {
    ObjectAnimator.ofFloat(
        this,
        View.TRANSLATION_Y,
        TEXT_TRANSITION_BOTTOM,
        TEXT_TRANSITION_DEFAULT
    ).apply {
        duration = LONG_ANIMATION_DURATION
        start()
    }
    ObjectAnimator.ofFloat(this, View.ALPHA, TEXT_ALPHA_FADE, TEXT_ALPHA_SHOW).apply {
        duration = LONG_ANIMATION_DURATION
        start()
    }
}

fun TextView.showUpFromTop() {
    ObjectAnimator.ofFloat(
        this,
        View.TRANSLATION_Y,
        TEXT_TRANSITION_TOP,
        TEXT_TRANSITION_DEFAULT
    ).apply {
        duration = LONG_ANIMATION_DURATION
        start()
    }
    ObjectAnimator.ofFloat(this, View.ALPHA, TEXT_ALPHA_FADE, TEXT_ALPHA_SHOW).apply {
        duration = LONG_ANIMATION_DURATION
        start()
    }
}

fun TextView.getBackToBottom() {
    ObjectAnimator.ofFloat(
        this,
        View.TRANSLATION_Y,
        TEXT_TRANSITION_DEFAULT,
        TEXT_TRANSITION_BOTTOM
    ).apply {
        duration = LONG_ANIMATION_DURATION
        start()
    }
    ObjectAnimator.ofFloat(this, View.ALPHA, TEXT_ALPHA_SHOW, TEXT_ALPHA_FADE).apply {
        duration = LONG_ANIMATION_DURATION
        start()
    }
}

fun TextView.getBackToTop() {
    ObjectAnimator.ofFloat(
        this,
        View.TRANSLATION_Y,
        TEXT_TRANSITION_DEFAULT,
        TEXT_TRANSITION_TOP
    ).apply {
        duration = LONG_ANIMATION_DURATION
        start()
    }
    ObjectAnimator.ofFloat(this, View.ALPHA, TEXT_ALPHA_SHOW, TEXT_ALPHA_FADE).apply {
        duration = LONG_ANIMATION_DURATION
        start()
    }
}