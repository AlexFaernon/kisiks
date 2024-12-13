package com.example.cashincontrol.data.saving.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import com.example.cashincontrol.domain.UserClass
import com.example.cashincontrol.domain.transaction.Category
import com.example.cashincontrol.domain.transaction.CheckCategory
import com.example.cashincontrol.domain.transaction.CheckTransaction
import com.example.cashincontrol.domain.transaction.ExpensesCategory
import com.example.cashincontrol.domain.transaction.ExpensesTransaction
import com.example.cashincontrol.domain.transaction.IncomeCategory
import com.example.cashincontrol.domain.transaction.IncomeTransaction
import com.example.cashincontrol.domain.transaction.Transaction
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
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

            Log.d("category db", "cat add " + db.insert(CategoryTable.TABLE_NAME, null, values).toString())
        }

        fun addTransaction(transaction: Transaction){
            val values = ContentValues().apply {
                put(TransactionTable.SUM, transaction.sum)
                put(TransactionTable.DATE, transaction.date.toString())
                put(TransactionTable.COMMENTARY, transaction.commentary)
                put(TransactionTable.CATEGORY, transaction.category.name)
                put(TransactionTable.IS_EXPENSES, if (transaction is ExpensesTransaction) 1 else 0)
            }

            Log.d("Transaction db", "transaction add " + db.insert(TransactionTable.TABLE_NAME, null, values).toString())
        }

        fun addCheckCategory(checkCategory: CheckCategory){
            val jsonAliases = Json.encodeToString(checkCategory.alias)
            val values = ContentValues().apply {
                put(CheckCategoryTable.NAME, checkCategory.name)
                put(CheckCategoryTable.ICON_ID, checkCategory.icon)
                put(CheckCategoryTable.ALIASES, jsonAliases)
            }

            Log.d("Check category db", "check cat add " + db.insert(CheckCategoryTable.TABLE_NAME, null, values).toString())
        }

        fun addCheckTransaction(checkTransaction: CheckTransaction){
            val jsonCategories = Json.encodeToString(checkTransaction.category.mapKeys { it.key.name })
            val values = ContentValues().apply {
                put(CheckTransactionsTable.DATE, checkTransaction.date.toString())
                put(CheckTransactionsTable.CATEGORIES, jsonCategories)
            }

            Log.d("Check transaction db", "check transaction add " + db.insert(CheckTransactionsTable.TABLE_NAME, null, values).toString())
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

        fun getCheckCategories(): MutableList<CheckCategory>{
            val cursor = db.query(
                CheckCategoryTable.TABLE_NAME, null, null, null, null, null, null)

            val result: MutableList<CheckCategory> = mutableListOf()
            with(cursor){
                while (moveToNext()){
                    val name = getString(getColumnIndexOrThrow(CheckCategoryTable.NAME))
                    val icon = getInt(getColumnIndexOrThrow(CheckCategoryTable.ICON_ID))
                    val aliasesJson = getString(getColumnIndexOrThrow(CheckCategoryTable.ALIASES))
                    val aliases = Json.decodeFromString<HashSet<String>>(aliasesJson)

                    result.add(CheckCategory(name, aliases, icon))
                }
            }

            cursor.close()
            return result
        }

        fun getCheckTransactions(): MutableList<CheckTransaction>{
            val cursor = db.query(
                CheckTransactionsTable.TABLE_NAME, null, null, null, null, null, null)

            val result: MutableList<CheckTransaction> = mutableListOf()
            with(cursor){
                while (moveToNext()){
                    val dateString = getString(getColumnIndexOrThrow(CheckTransactionsTable.DATE))
                    val categoriesJson = getString(getColumnIndexOrThrow(CheckTransactionsTable.CATEGORIES))
                    val date = LocalDateTime.parse(dateString)
                    val categories = Json.decodeFromString<Map<String, Float>>(categoriesJson).mapKeys { UserClass.getCheckCategory(it.key) }

                    result.add(CheckTransaction(date, categories))
                }
            }

            cursor.close()
            Log.d("Check transaction db", "Found ${result.count()} check transactions")
            return result
        }
    }
}