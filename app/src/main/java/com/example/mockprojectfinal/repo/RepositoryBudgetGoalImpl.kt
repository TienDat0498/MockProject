package com.example.mockprojectfinal.repo

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import com.example.mockprojectfinal.data.CategoryDao
import com.example.mockprojectfinal.data.CategoryDatabase
import com.example.mockprojectfinal.data.entity.Category
import com.example.mockprojectfinal.utils.Constant
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RepositoryBudgetGoalImpl (
    private val sharedPreferences: SharedPreferences,
    private val databaseDao: CategoryDao
): RepositoryBudgetGoal {
    override suspend fun getCategories(): LiveData<List<Category>> {
        return withContext(Dispatchers.IO){
            databaseDao.getAllCategories()
        }
    }

    override suspend fun saveCategory(category: Category) {
        withContext(Dispatchers.IO){
            databaseDao.insertCategory(category)
        }
    }

    override fun readBudget(): Int {
        if (!sharedPreferences.contains(Constant.KEY_BUDGET)){
            val editor = sharedPreferences.edit()
            editor.putInt(Constant.KEY_BUDGET, Constant.DEFAULT_BUDGET)
            editor.apply()
        }
        return sharedPreferences.getInt(Constant.KEY_BUDGET, Constant.DEFAULT_BUDGET)
    }

    override fun putBudget(budget_value: Int) {
        val editor = sharedPreferences.edit()
        editor.putInt(Constant.KEY_BUDGET, budget_value)
        editor.apply()
    }
}