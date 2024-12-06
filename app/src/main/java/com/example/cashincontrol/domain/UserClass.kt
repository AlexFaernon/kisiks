package com.example.cashincontrol.domain

import com.example.cashincontrol.domain.database.DbHandler
import com.example.cashincontrol.domain.goals.Goal
import com.example.cashincontrol.domain.transaction.Category
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
        var goal: Goal? = null
        var currentMoney: Float = 1000F
        var isOnboardingCompleted: Boolean = false
        var startDate: LocalDate? = LocalDate.now()

        fun setupUserData(){
            val result = DbHandler.getCategories()
            if (result.isEmpty()) {
                createCategory("Продукты", true)
                createCategory("Зарплата", false)
            } else {
                categories = result
            }

            transactions = DbHandler.getTransactions()
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

            val index = if (insertIndex==0) 0 else -insertIndex - 1
            transactions.add(index, newTransaction)
            DbHandler.addTransaction(newTransaction)
            currentMoney += if (isExpenses) -sum else sum
        }

        fun getOrCreateCategory(categoryName: String, isExpenses: Boolean): Category{
            val category = checkCategory(categoryName, isExpenses)
            category?.let {
                return category
            }

            return createCategory(categoryName, isExpenses)
        }

        fun createCategory(categoryName: String, isExpenses: Boolean): Category{
            val category = checkCategory(categoryName, isExpenses)
            var newCategory: Category
            category?: run {
                newCategory = if (isExpenses) { ExpensesCategory(categoryName) } else { IncomeCategory(categoryName) }
                categories.add(newCategory)
                DbHandler.addCategory(newCategory)
                return newCategory
            }
            throw IllegalArgumentException("Category already exists")
        }

        private fun checkCategory(categoryName: String, isExpenses: Boolean): Category?{
            val targetType = if (isExpenses) ExpensesCategory::class.simpleName else IncomeCategory::class.simpleName
            return categories.firstOrNull { it.name == categoryName && it::class.simpleName == targetType}
        }

        fun getExpensesTransactions(): List<Transaction>
        {
            return transactions.filterIsInstance<ExpensesTransaction>()
        }

        fun getDaysSinceStart(): Long {
            return ChronoUnit.DAYS.between(startDate, LocalDate.now()) + 1
        }
    }
}