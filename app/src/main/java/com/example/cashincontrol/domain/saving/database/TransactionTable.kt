package com.example.cashincontrol.domain.saving.database

class TransactionTable{
    companion object{
        const val TABLE_NAME = "Transactions"
        const val PRIMARY_KEY = "Id"
        const val SUM = "Sum"
        const val DATE = "Date"
        const val COMMENTARY = "Commentary"
        const val CATEGORY = "Category"
        const val IS_EXPENSES = "Expenses"
    }
}
