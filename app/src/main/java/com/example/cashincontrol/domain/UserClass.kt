package com.example.cashincontrol.domain

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import com.example.cashincontrol.data.saving.UserDataSaveClass
import com.example.cashincontrol.data.saving.UserDataStoreManager
import com.example.cashincontrol.data.saving.database.DbHandler
import com.example.cashincontrol.domain.goals.AchievementSystem
import com.example.cashincontrol.domain.goals.Goal
import com.example.cashincontrol.domain.goals.RankClass
import com.example.cashincontrol.domain.parsing.baseCheckCategories
import com.example.cashincontrol.domain.transaction.Category
import com.example.cashincontrol.domain.transaction.CheckCategory
import com.example.cashincontrol.domain.transaction.CheckTransaction
import com.example.cashincontrol.domain.transaction.ExpensesCategory
import com.example.cashincontrol.domain.transaction.ExpensesTransaction
import com.example.cashincontrol.domain.transaction.IncomeCategory
import com.example.cashincontrol.domain.transaction.IncomeTransaction
import com.example.cashincontrol.domain.transaction.Transaction
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

class UserClass {
    companion object {
        var transactions: MutableList<Transaction> = mutableListOf()
        private var categories: MutableList<Category> = mutableListOf()
        var checkTransactions: MutableList<CheckTransaction> = mutableListOf()
        private var checkCategories: MutableList<CheckCategory> = baseCheckCategories.toMutableList()
        var goal: Goal? = null
        var currentMoney: Float = 0F
        var isOnboardingCompleted: Boolean = false
        var startDate: LocalDate = LocalDate.now()
        var rank: RankClass = RankClass()
        var achievementSystem = AchievementSystem()

        @Composable
        fun SetupUserData(dataStoreManager: UserDataStoreManager){

            val categoriesFromDb = DbHandler.getCategories()
            if (categoriesFromDb.isEmpty()) {
                createCategory("Супермаркеты", true)
                createCategory("Транспорт", true)
                createCategory("Все для дома", true)

                createCategory("Зарплата", false)
                createCategory("Стипендия", false)

            } else {
                categories = categoriesFromDb
            }

            val checkCategoriesFromDb = DbHandler.getCheckCategories()
            if (checkCategoriesFromDb.isEmpty()) {
                for (cat in baseCheckCategories) {
                    addCheckCategory(cat)
                }
            } else {
                checkCategories = checkCategoriesFromDb
            }

            transactions = DbHandler.getTransactions()
            checkTransactions = DbHandler.getCheckTransactions()

            val userData = dataStoreManager.getData().collectAsState(initial = UserDataSaveClass()).value
            goal = userData.goal
            isOnboardingCompleted = userData.onboardingCompleted
            startDate = userData.startDate
            rank = userData.rank
            achievementSystem = userData.achievements
            achievementSystem.CheckMonthly()
        }

        fun getExpensesCategory(): List<ExpensesCategory> {
            val result: MutableList<ExpensesCategory> = mutableListOf()
            for (category in categories) {
                if (category is ExpensesCategory) {
                    result.add(category)
                }
            }
            return result
        }

        fun getIncomeCategory(): List<IncomeCategory> {
            val result: MutableList<IncomeCategory> = mutableListOf()
            for (category in categories) {
                if (category is IncomeCategory) {
                    result.add(category)
                }
            }
            return result
        }

        fun getCheckCategory(name: String): CheckCategory{
            val checkCategory = checkCategories.find { it.name == name }
            if (checkCategory == null){
                throw IllegalArgumentException("Check category not found")
            }

            return checkCategory
        }

        fun getCheckCategories(): List<CheckCategory>
        {
            return checkCategories
        }

        fun addTransaction(
            isExpenses: Boolean,
            sum: Float,
            date: LocalDateTime,
            category: Category,
            commentary: String,
            checkUnique: Boolean = false
        ) {
            val newTransaction = if (isExpenses)
                ExpensesTransaction(sum, date, category as ExpensesCategory, commentary)
            else
                IncomeTransaction(sum, date,category as IncomeCategory, commentary)

            val insertIndex = transactions.binarySearch(newTransaction, compareBy<Transaction> { it.date }.reversed())
            if (checkUnique && insertIndex >= 0) {
                return
            }

            val index = if (insertIndex >= 0) insertIndex else -insertIndex - 1
            transactions.add(index, newTransaction)
            DbHandler.addTransaction(newTransaction)
            currentMoney += if (isExpenses) -sum else sum
            rank.checkNewRank()
            achievementSystem.onNewTransaction()
        }

        fun getOrCreateCategory(categoryName: String, isExpenses: Boolean): Category{
            val category = isCategoryExists(categoryName, isExpenses)
            category?.let {
                return category
            }

            return createCategory(categoryName, isExpenses)
        }

        fun createCategory(categoryName: String, isExpenses: Boolean): Category{
            val category = isCategoryExists(categoryName, isExpenses)
            var newCategory: Category
            category?: run {
                newCategory = if (isExpenses) { ExpensesCategory(categoryName) } else { IncomeCategory(categoryName) }
                categories.add(newCategory)
                DbHandler.addCategory(newCategory)
                return newCategory
            }
            throw IllegalArgumentException("Category already exists")
        }

        private fun isCategoryExists(categoryName: String, isExpenses: Boolean): Category?{
            val targetType = if (isExpenses) ExpensesCategory::class.simpleName else IncomeCategory::class.simpleName
            return categories.firstOrNull { it.name == categoryName && it::class.simpleName == targetType}
        }

        fun getExpensesTransactions(): List<Transaction>
        {
            return transactions.filterIsInstance<ExpensesTransaction>()
        }

        fun getIncomeTransactions(): List<Transaction>
        {
            return transactions.filterIsInstance<IncomeTransaction>()
        }

        fun addCheckCategory(name: String, aliasesStr: String) {
            val checkCategory = CheckCategory(name, aliasesStr.split('.', ',', ' ').toHashSet())
            addCheckCategory(checkCategory)
        }

        private fun addCheckCategory(checkCategory: CheckCategory){
            checkCategories.add(checkCategory)
            DbHandler.addCheckCategory(checkCategory)
        }

        fun addCheckTransaction(datetime: LocalDateTime, checkCategories: Map<CheckCategory, Float>){
            val newTransaction = CheckTransaction(datetime, checkCategories)
            val insertIndex = checkTransactions.binarySearch(newTransaction, compareBy<CheckTransaction> { it.date }.reversed())
            if (insertIndex >= 0) {
                Log.d("New transaction", "Transaction by price ${newTransaction.category.values.sum()} discarded")
                return
            }

            val index = -insertIndex - 1
            checkTransactions.add(index, newTransaction)
            DbHandler.addCheckTransaction(newTransaction)
            rank.checkNewRank()
            achievementSystem.onNewTransaction()
        }

        fun findCheckCategoryByAlias(maybeAliases: List<String>): CheckCategory? {
            for (checkCategory in checkCategories){
                for (maybeAlias in maybeAliases){
                    val find = checkCategory.alias.find {
                        it.startsWith(maybeAlias, true) || maybeAlias.startsWith(it, true)}
                    if (find != null){
                        Log.d("findCheck", "found $maybeAlias in $find")
                        return checkCategory
                    }
                }
            }

            return null
        }

        fun getDaysSinceStart(): Long {
            return ChronoUnit.DAYS.between(startDate, LocalDate.now()) + 1
        }
    }
}