package com.example.cashincontrol.data.saving.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

const val DB_NAME: String = "DB"

class DbHelper(context: Context) : SQLiteOpenHelper(context, DB_NAME, null, 1){
    override fun onCreate(db: SQLiteDatabase?) {
        db!!.execSQL("CREATE TABLE ${CategoryTable.TABLE_NAME} (" +
                "${CategoryTable.PRIMARY_KEY} INTEGER PRIMARY KEY," +
                "${CategoryTable.CATEGORY_NAME} TEXT NOT NULL," +
                "${CategoryTable.ICON_ID} INTEGER NOT NULL," +
                "${CategoryTable.IS_EXPENSES} INTEGER NOT NULL)")
        Log.d("Category BD", "Db created")

        db.execSQL("CREATE TABLE ${TransactionTable.TABLE_NAME} (" +
                "${TransactionTable.PRIMARY_KEY} INTEGER PRIMARY KEY," +
                "${TransactionTable.SUM} REAL NOT NULL," +
                "${TransactionTable.DATE} TEXT NOT NULL," +
                "${TransactionTable.COMMENTARY} TEXT NOT NULL," +
                "${TransactionTable.CATEGORY} TEXT NOT NULL," +
                "${TransactionTable.IS_EXPENSES} INTEGER NOT NULL)")
        Log.d("Transaction Db", "Db created")

        db.execSQL("CREATE TABLE ${CheckCategoryTable.TABLE_NAME} (" +
                "${CheckCategoryTable.PRIMARY_KEY} INTEGER PRIMARY KEY," +
                "${CheckCategoryTable.NAME} TEXT NOT NULL," +
                "${CheckCategoryTable.ICON_ID} INTEGER NOT NULL," +
                "${CheckCategoryTable.ALIASES} TEXT NOT NULL)")
        Log.d("Check Category BD", "Db created")

        db.execSQL("CREATE TABLE ${CheckTransactionsTable.TABLE_NAME} (" +
                "${CheckTransactionsTable.PRIMARY_KEY} INTEGER PRIMARY KEY," +
                "${CheckTransactionsTable.DATE} TEXT NOT NULL," +
                "${CheckTransactionsTable.CATEGORIES} TEXT NOT NULL)")
        Log.d("Check Transaction BD", "Db created")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        return
    }
}