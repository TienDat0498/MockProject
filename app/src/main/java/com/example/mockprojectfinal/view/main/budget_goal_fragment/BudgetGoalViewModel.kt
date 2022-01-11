package com.example.mockprojectfinal.view.main.budget_goal_fragment

import androidx.lifecycle.ViewModel
import com.example.mockprojectfinal.repo.RepositoryBudgetGoal
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class BudgetGoalViewModel @Inject constructor(
    private val repo: RepositoryBudgetGoal
): ViewModel() {

}