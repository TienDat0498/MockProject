package com.example.mockprojectfinal.view.main.budget_goal_fragment

import androidx.lifecycle.*
import com.example.category_slider.SingleItem
import com.example.mockprojectfinal.data.entity.Category
import com.example.mockprojectfinal.repo.RepositoryBudgetGoal
import com.example.mockprojectfinal.utils.DefaultCategory
import com.example.mockprojectfinal.utils.MoneySpendInformation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class BudgetGoalViewModel @Inject constructor(
    private val repo: RepositoryBudgetGoal
) : ViewModel() {

    private var mListCategory: LiveData<List<Category>> = repo.getCategories()
    val listSingleItem: LiveData<List<SingleItem>> =
        Transformations.map(mListCategory) { listCategory ->
            val listSingleItem = mutableListOf<SingleItem>()
            for (categoryName in DefaultCategory.CATEGORY_ORDER) {
                for (category in listCategory) {
                    if (category.name == categoryName) {
                        listSingleItem.add(category.toSingleItem())
                    }
                }
            }
            listSingleItem
        }

    var currentCategory: MutableLiveData<SingleItem?> = MutableLiveData()

    private var mCurValue: MutableLiveData<Int> = MutableLiveData()
    val curValue: LiveData<Int>
        get() = mCurValue

    private var mLastStatus: MoneySpendInformation? = null
    val lastStatus: MoneySpendInformation?
        get() = mLastStatus

    private var mCurStatus: MutableLiveData<MoneySpendInformation> = MutableLiveData()
    val curStatus: LiveData<MoneySpendInformation>
        get() = mCurStatus

    fun updateCurStatus(curMoney: Int) {
        when (curMoney) {
            in Int.MIN_VALUE..normalBudget -> {
                if (mCurStatus.value != MoneySpendInformation.NORMAL) {
                    mLastStatus = mCurStatus.value
                    mCurStatus.value = MoneySpendInformation.NORMAL
                }
            }
            in normalBudget..normalBudget * 2 -> {
                if (mCurStatus.value != MoneySpendInformation.LOT) {
                    mLastStatus = mCurStatus.value
                    mCurStatus.value = MoneySpendInformation.LOT
                }
            }
            else -> {
                if (mCurStatus.value != MoneySpendInformation.CRAZY) {
                    mLastStatus = mCurStatus.value
                    mCurStatus.value = MoneySpendInformation.CRAZY
                }
            }
        }
    }

    fun setCurrentValue(newValue: Int) {
        mCurValue.value = newValue
    }

    init {
        currentCategory.value = null
        mCurValue.value = 0
        mCurStatus.value = MoneySpendInformation.NORMAL
    }

    private var mBudget: Int = repo.readBudget()

    private val normalBudget: Int
        get() = mBudget / (mListCategory.value?.size ?: 1)


    fun updateDatabase(newValue: Int) {
        currentCategory.value?.let {
            it.value = newValue
            viewModelScope.launch {
                withContext(Dispatchers.IO) {
                    repo.saveCategory(Category.fromSingleItem(it))
                }
            }
        }
    }

}