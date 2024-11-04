package com.example.cashincontrol.domain

import com.example.cashincontrol.domain.transaction.Category
import com.example.cashincontrol.domain.transaction.ExpensesCategory
import com.example.cashincontrol.domain.transaction.ExpensesTransaction
import com.example.cashincontrol.domain.transaction.IncomeCategory
import com.example.cashincontrol.domain.transaction.IncomeTransaction
import com.example.cashincontrol.domain.transaction.Transaction
import java.time.LocalDate

class UserClass {
    companion object {
        val transactions: MutableList<Transaction> = mutableListOf()
        val categories: MutableList<Category> =
            mutableListOf(ExpensesCategory("Продукты"), IncomeCategory("Зарплата"))
        var currentMoney: Float = 1000F

        public fun GetExpensesCategory(): List<ExpensesCategory> {
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

        public fun AddNewExpenses(
            sum: Float,
            card: String,
            date: LocalDate,
            category: ExpensesCategory,
            organization: String
        ) {
            transactions.add(ExpensesTransaction(sum, card, date, category, organization))
            currentMoney -= sum
        }

        public fun AddNewIncome(
            sum: Float,
            card: String,
            date: LocalDate,
            category: IncomeCategory
        ) {
            transactions.add(IncomeTransaction(sum, card, date, category))
            currentMoney += sum
        }
    }
}