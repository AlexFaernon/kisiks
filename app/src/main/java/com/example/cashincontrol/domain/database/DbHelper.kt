package com.example.cashincontrol.domain.database

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
        Log.d("Category BD", "Bb created")

        db.execSQL("CREATE TABLE ${TransactionTable.TABLE_NAME} (" +
                "${TransactionTable.PRIMARY_KEY} INTEGER PRIMARY KEY," +
                "${TransactionTable.SUM} REAL NOT NULL," +
                "${TransactionTable.DATE} TEXT NOT NULL," +
                "${TransactionTable.COMMENTARY} TEXT NOT NULL," +
                "${TransactionTable.CATEGORY} TEXT NOT NULL," +
                "${TransactionTable.IS_EXPENSES} INTEGER NOT NULL)")
        Log.d("Transaction Db", "Db created")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        return
    }
}