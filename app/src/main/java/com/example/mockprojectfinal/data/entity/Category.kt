package com.example.mockprojectfinal.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.example.category_slider.SingleItem

@Entity
data class Category (
    val icon: Int,
    val value: Int,
    val colorFirst: String,
    val colorSecond: String,
    @PrimaryKey
    val name: String
){

    companion object{
        @Ignore
        fun fromSingleItem(singleItem: SingleItem): Category{
            return Category(
                singleItem.icon,
                singleItem.value,
                singleItem.colorFirst,
                singleItem.colorSecond,
                singleItem.name
            )
        }
    }

    @Ignore
    fun toSingleItem(): SingleItem{
        return SingleItem(
            name,
            icon,
            value,
            colorFirst,
            colorSecond
        )
    }
}