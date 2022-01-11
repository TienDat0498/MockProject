package com.example.mockprojectfinal.utils

import android.hardware.SensorAdditionalInfo

object Constant {
    const val PREFERENCE_FILE_KEY = "com.example.mockprojectfinal.SHARE_PREFERENCES_FILE_KEY"
    const val KEY_BUDGET = "budget_goal"
    const val DEFAULT_BUDGET = 1500

    enum class BudgetSpendInformation(val critical: String, val additionalInfo: String){
        NORMAL("Normal", "It's ok to eat at your place\nUp to 5% of economy"),
        LOT("That's a lot", "Sometimes, you can eat in cafe\nUp to 3% of economy"),
        CRAZY("Are you crazy", "Eat in restaurants everyday\nBlow off 30% of money")
    }
}