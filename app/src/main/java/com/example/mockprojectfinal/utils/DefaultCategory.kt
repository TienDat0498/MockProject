package com.example.mockprojectfinal.utils

import com.example.category_slider.SingleItem
import com.example.mockprojectfinal.R

object DefaultCategory {
    val listSingleItem = listOf(
        SingleItem("Cafe", R.drawable.ic_restaurant, 400, "#4238ed", "#2b22b6"),
        SingleItem("House", R.drawable.ic_home, 600, "#35e9d4", "#37dbc3"),
        SingleItem("Taxi", R.drawable.ic_taxi, 900, "#fcbc40", "#e8a82a"),
        SingleItem("Gym", R.drawable.ic_weightlifter, 300, "#FF8A65", "#FF5722"),
        SingleItem("Love", R.drawable.ic_relationship, 700, "#EF5350", "#E53935"),
        SingleItem("Other", R.drawable.ic_other, 800, "#BDBDBD", "#757575"),
    )

    val CATEGORY_ORDER = listOf("Cafe", "House", "Taxi", "Gym", "Love", "Other")
}