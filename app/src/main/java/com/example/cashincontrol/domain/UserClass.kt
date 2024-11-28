package com.example.cashincontrol.domain

import com.example.cashincontrol.domain.goals.Goal
import com.example.cashincontrol.domain.transaction.Category
import com.example.cashincontrol.domain.transaction.ExpensesCategory
import com.example.cashincontrol.domain.transaction.ExpensesTransaction
import com.example.cashincontrol.domain.transaction.IncomeCategory
import com.example.cashincontrol.domain.transaction.IncomeTransaction
import com.example.cashincontrol.domain.transaction.Transaction
import java.time.LocalDateTime

class UserClass {
    companion object {
        val transactions: MutableList<Transaction> = mutableListOf()
        val categories: MutableList<Category> =
            mutableListOf(ExpensesCategory("Продукты"), IncomeCategory("Зарплата"))
        public var goal: Goal? = null
        var currentMoney: Float = 1000F

        public fun getExpensesCategory(): List<ExpensesCategory> {
            val result: MutableList<ExpensesCategory> = mutableListOf()
            for (category in categories) {
                if (category is ExpensesCategory) {
                    result.add(category)
                }
            }
            return result
        }

        public fun GetIncomeCategory(): List<IncomeCategory> {
            val result: MutableList<IncomeCategory> = mutableListOf()
            for (category in categories) {
                if (category is IncomeCategory) {
                    result.add(category)
                }
            }
            return result
        }

        public fun addTransaction(
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
            currentMoney += if (isExpenses) -sum else sum
        }

        public fun getOrCreateCategory(categoryName: String, isExpenses: Boolean): Category{
            val category = checkCategory(categoryName, isExpenses)
            category?.let {
                return category
            }

            return createCategory(categoryName, isExpenses)
        }

        public fun createCategory(categoryName: String, isExpenses: Boolean): Category{
            val category = checkCategory(categoryName, isExpenses)
            var newCategory: Category
            category?: run {
                newCategory = if (isExpenses) { ExpensesCategory(categoryName) } else { IncomeCategory(categoryName) }
                categories.add(newCategory)
                return newCategory
            }
            throw IllegalArgumentException("Category already exists")
        }

        fun checkCategory(categoryName: String, isExpenses: Boolean): Category?{
            val targetType = if (isExpenses) ExpensesCategory::class.simpleName else IncomeCategory::class.simpleName
            return categories.firstOrNull { it.name == categoryName && it::class.simpleName == targetType}
        }

        fun getExpensesTransactions(): List<Transaction>
        {
            return transactions.filterIsInstance<ExpensesTransaction>()
        }
    }
}