package com.example.cashincontrol.domain.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.example.cashincontrol.domain.transaction.Category
import com.example.cashincontrol.domain.transaction.ExpensesCategory
import com.example.cashincontrol.domain.transaction.IncomeCategory

class DbHandler {
    companion object{
        private lateinit var categoryDb: SQLiteDatabase
        private lateinit var transactionsDb: SQLiteDatabase

        fun setupDatabase(context: Context){
            categoryDb = DbHelper(context).writableDatabase
        }

        fun closeDatabase(){
            categoryDb.close()
        }

        fun addCategory(category: Category){
            val values = ContentValues().apply {
                put(CategoryTable.CATEGORY_NAME, category.name)
                put(CategoryTable.ICON_ID, category.icon)
                put(CategoryTable.IS_EXPENSES, if (category is ExpensesCategory) 1 else 0)
            }

            categoryDb.insert(CategoryTable.TABLE_NAME, null, values)
        }

        fun getCategories(): MutableList<Category>{
            val cursor = categoryDb.query(
                CategoryTable.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null
            )

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
    }
}