package com.example.categoryslide

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val expandSlideView = findViewById<ExpandSlideView>(R.id.expandSlideView)

        val listData = listOf<ExpandSlideView.SingleItem>(
            ExpandSlideView.SingleItem("Cafe", R.drawable.restaurant, 400, "#4238ed", "#2b22b6"),
            ExpandSlideView.SingleItem("House", R.drawable.home, 600, "#35e9d4", "#37dbc3"),
            ExpandSlideView.SingleItem("Taxi", R.drawable.taxi, 900, "#fcbc40", "#e8a82a"),
            ExpandSlideView.SingleItem("Gym", R.drawable.weightlifter, 500, "#FF8A65", "#FF5722"),
            ExpandSlideView.SingleItem("Love", R.drawable.relationship, 700, "#EF5350", "#E53935"),
            ExpandSlideView.SingleItem("Other", R.drawable.ic_other, 800, "#BDBDBD", "#757575"),
        )
        expandSlideView.data = listData

        findViewById<Button>(R.id.buttonPrev).setOnClickListener {
            expandSlideView.previousItem()
        }
        findViewById<Button>(R.id.buttonNext).setOnClickListener {
            expandSlideView.nextItem()
        }
    }


}