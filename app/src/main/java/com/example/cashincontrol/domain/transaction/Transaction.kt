package com.example.cashincontrol.domain.transaction

import java.time.LocalDateTime


abstract class Transaction(){
    abstract val sum: Float
    abstract val date: LocalDateTime
    abstract val category: Category
    abstract val commentary: String
}
