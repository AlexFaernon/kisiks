package com.example.cashincontrol.domain.database

class CategoryTable {
    companion object{
        const val TABLE_NAME = "Categories"
        const val PRIMARY_KEY = "Id"
        const val CATEGORY_NAME = "Name"
        const val ICON_ID = "Icon"
        const val IS_EXPENSES = "Expenses"
    }
}