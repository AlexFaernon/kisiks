package com.example.cashincontrol.domain.transaction

import java.time.LocalDate


abstract class Transaction(){
    abstract val sum: Float
    abstract val card: String
    abstract val date: LocalDate
    abstract val category: Category
}
