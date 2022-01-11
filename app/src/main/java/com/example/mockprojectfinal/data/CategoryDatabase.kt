package com.example.mockprojectfinal.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.mockprojectfinal.data.entity.Category
import com.example.mockprojectfinal.utils.DefaultCategory
import kotlinx.coroutines.*


@Database(entities = [Category::class], version = 1, exportSchema = false)
abstract class CategoryDatabase : RoomDatabase() {
    abstract val categoryDao: CategoryDao

    companion object {
        const val DATABASE_NAME ="category_database"
    }


    fun initData(){
        val job = Job()
        val coroutineScope = CoroutineScope(Dispatchers.Main + job)
        if (categoryDao.count() == 0){
            coroutineScope.launch {
                withContext(Dispatchers.IO){
                    for (singleItem in DefaultCategory.listSingleItem){
                        categoryDao.insertCategory(Category.fromSingleItem(singleItem))
                    }
                }
            }
        }
    }
}