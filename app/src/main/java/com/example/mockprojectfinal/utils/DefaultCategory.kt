package com.example.mockprojectfinal.utils

import com.example.category_slider.SingleItem
import com.example.mockprojectfinal.R

object DefaultCategory {
    val listSingleItem = listOf<SingleItem>(
        SingleItem("Cafe", R.drawable.restaurant, 400, "#4238ed", "#2b22b6"),
        SingleItem("House", R.drawable.home, 600, "#35e9d4", "#37dbc3"),
        SingleItem("Taxi", R.drawable.taxi, 900, "#fcbc40", "#e8a82a"),
        SingleItem("Gym", R.drawable.weightlifter, 500, "#FF8A65", "#FF5722"),
        SingleItem("Love", R.drawable.relationship, 700, "#EF5350", "#E53935"),
        SingleItem("Other", R.drawable.ic_other, 800, "#BDBDBD", "#757575"),
    )
}