package com.example.mockprojectfinal.repo

import androidx.lifecycle.LiveData
import com.example.mockprojectfinal.data.entity.Category

interface RepositoryBudgetGoal {
    suspend fun getCategories(): LiveData<List<Category>>

    suspend fun saveCategory(category: Category)

    fun readBudget(): Int

    fun putBudget(value: Int)

}