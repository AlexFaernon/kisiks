package com.example.cashincontrol.domain.transaction

import java.time.LocalDateTime


abstract class Transaction(
    open val sum: Float,
    open val date: LocalDateTime,
    open val commentary: String) {
    abstract val category: Category
}
