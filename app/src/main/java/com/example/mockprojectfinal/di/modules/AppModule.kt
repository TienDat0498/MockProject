package com.example.mockprojectfinal.di.modules

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import com.example.mockprojectfinal.data.CategoryDatabase
import com.example.mockprojectfinal.repo.RepositoryBudgetGoal
import com.example.mockprojectfinal.repo.RepositoryBudgetGoalImpl
import com.example.mockprojectfinal.utils.Constant
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class AppModule {

    @Singleton
    @Provides
    fun provideCategoryDatabase(application: Application):CategoryDatabase{
        val database = Room.databaseBuilder(
            application,
            CategoryDatabase::class.java,
            CategoryDatabase.DATABASE_NAME
        ).build()
        database.initData()
        return database
    }

    @Singleton
    @Provides
    fun provideSharePreferences(application: Application): SharedPreferences{
        return application.getSharedPreferences(Constant.PREFERENCE_FILE_KEY, Context.MODE_PRIVATE)
    }

    @Provides
    @Singleton
    fun provideNoteRepository(sharedPreferences: SharedPreferences, db: CategoryDatabase): RepositoryBudgetGoal {
        return RepositoryBudgetGoalImpl(sharedPreferences, db.categoryDao)
    }
}