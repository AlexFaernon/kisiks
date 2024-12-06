package com.example.cashincontrol.domain.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import com.example.cashincontrol.domain.UserClass
import com.example.cashincontrol.domain.transaction.Category
import com.example.cashincontrol.domain.transaction.ExpensesCategory
import com.example.cashincontrol.domain.transaction.ExpensesTransaction
import com.example.cashincontrol.domain.transaction.IncomeCategory
import com.example.cashincontrol.domain.transaction.IncomeTransaction
import com.example.cashincontrol.domain.transaction.Transaction
import java.time.LocalDateTime

class DbHandler {
    companion object{
        private lateinit var db: SQLiteDatabase

        fun setupDatabase(context: Context){
            db = DbHelper(context).writableDatabase
        }

        fun closeDatabase(){
            db.close()
        }

        fun addCategory(category: Category){
            val values = ContentValues().apply {
                put(CategoryTable.CATEGORY_NAME, category.name)
                put(CategoryTable.ICON_ID, category.icon)
                put(CategoryTable.IS_EXPENSES, if (category is ExpensesCategory) 1 else 0)
            }

            Log.d("category db", db.insert(CategoryTable.TABLE_NAME, null, values).toString())
        }

        fun addTransaction(transaction: Transaction){
            val values = ContentValues().apply {
                put(TransactionTable.SUM, transaction.sum)
                put(TransactionTable.DATE, transaction.date.toString())
                put(TransactionTable.COMMENTARY, transaction.commentary)
                put(TransactionTable.CATEGORY, transaction.category.name)
                put(TransactionTable.IS_EXPENSES, if (transaction is ExpensesTransaction) 1 else 0)
            }

            Log.d("Transaction db", db.insert(TransactionTable.TABLE_NAME, null, values).toString())
        }

        fun getCategories(): MutableList<Category>{
            val cursor = db.query(
                CategoryTable.TABLE_NAME, null, null, null, null, null, null)

            val result: MutableList<Category> = mutableListOf()
            with(cursor){
                while (moveToNext()){
                    val name = getString(getColumnIndexOrThrow(CategoryTable.CATEGORY_NAME))
                    val icon = getInt(getColumnIndexOrThrow(CategoryTable.ICON_ID))
                    val isExpenses = getInt(getColumnIndexOrThrow(CategoryTable.IS_EXPENSES))
                    if (isExpenses == 1){
                        result.add(ExpensesCategory(name, icon))
                    }
                    else{
                        result.add(IncomeCategory(name, icon))
                    }
                }
            }

            cursor.close()
            return result
        }

        fun getTransactions(): MutableList<Transaction>{
            val cursor = db.query(
                TransactionTable.TABLE_NAME, null, null, null, null, null, null)

            val result: MutableList<Transaction> = mutableListOf()
            with(cursor){
                while (moveToNext()){
                    val sum = getFloat(getColumnIndexOrThrow(TransactionTable.SUM))
                    val date = LocalDateTime.parse(getString(getColumnIndexOrThrow(TransactionTable.DATE)))
                    val comment = getString(getColumnIndexOrThrow(TransactionTable.COMMENTARY))
                    val categoryStr = getString(getColumnIndexOrThrow(TransactionTable.CATEGORY))
                    val isExpenses = getInt(getColumnIndexOrThrow(TransactionTable.IS_EXPENSES))
                    val category = UserClass.getOrCreateCategory(categoryStr, isExpenses == 1)
                    if (isExpenses == 1){
                        result.add(ExpensesTransaction(sum, date, category as ExpensesCategory, comment))
                    }
                    else{
                        result.add(IncomeTransaction(sum, date, category as IncomeCategory, comment))
                    }
                }
            }

            cursor.close()
            return result
        }
    }
}