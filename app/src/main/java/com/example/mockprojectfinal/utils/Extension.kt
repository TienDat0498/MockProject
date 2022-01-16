package com.example.mockprojectfinal.utils

import android.animation.ObjectAnimator
import android.view.View
import android.widget.TextView
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
    ).start()
    ObjectAnimator.ofFloat(this, View.ALPHA, TEXT_ALPHA_FADE, TEXT_ALPHA_SHOW).start()
}

fun TextView.showUpFromTop() {
    ObjectAnimator.ofFloat(
        this,
        View.TRANSLATION_Y,
        TEXT_TRANSITION_TOP,
        TEXT_TRANSITION_DEFAULT
    ).start()
    ObjectAnimator.ofFloat(this, View.ALPHA, TEXT_ALPHA_FADE, TEXT_ALPHA_SHOW).start()
}

fun TextView.getBackToBottom() {
    ObjectAnimator.ofFloat(
        this,
        View.TRANSLATION_Y,
        TEXT_TRANSITION_DEFAULT,
        TEXT_TRANSITION_BOTTOM
    ).start()
    ObjectAnimator.ofFloat(this, View.ALPHA, TEXT_ALPHA_SHOW, TEXT_ALPHA_FADE).start()
}

fun TextView.getBackToTop() {
    ObjectAnimator.ofFloat(
        this,
        View.TRANSLATION_Y,
        TEXT_TRANSITION_DEFAULT,
        TEXT_TRANSITION_TOP
    )
        .start()
    ObjectAnimator.ofFloat(this, View.ALPHA, TEXT_ALPHA_SHOW, TEXT_ALPHA_FADE).start()
}