package com.example.cashincontrol.domain

import com.example.cashincontrol.domain.transaction.ExpensesCategory
import com.example.cashincontrol.domain.transaction.ExpensesTransaction
import com.example.cashincontrol.domain.transaction.IncomeCategory
import com.example.cashincontrol.domain.transaction.IncomeTransaction
import com.example.cashincontrol.domain.transaction.Transaction
import java.time.LocalDate

class UserClass {
    val transactions: MutableList<Transaction> = mutableListOf()
    var currentMoney: Float = 1000F

    public fun AddNewExpenses(sum: Float, card: String, date: LocalDate, category: ExpensesCategory, organization: String){
        transactions.add(ExpensesTransaction(sum, card, date, category, organization))
        currentMoney -= sum
    }

    public fun AddNewIncome(sum: Float, card: String, date: LocalDate, category: IncomeCategory){
        transactions.add(IncomeTransaction(sum, card, date, category))
        currentMoney += sum
    }
}