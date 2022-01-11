package com.example.mockprojectfinal.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.mockprojectfinal.data.entity.Category

@Dao
interface CategoryDao {
    @Query("select * from category")
    fun getAllCategories(): LiveData<List<Category>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategory(category: Category)

    @Query("select count(*) from category")
    fun count(): Int
}