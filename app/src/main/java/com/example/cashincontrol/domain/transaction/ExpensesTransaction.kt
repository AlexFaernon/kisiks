package com.example.cashincontrol.domain.transaction

import java.time.LocalDate
import java.util.Date

data class ExpensesTransaction(
    override val sum: Float,
    override val card: String,
    override val date: LocalDate,
    override val category: ExpensesCategory,
    val organization: String
) : Transaction(){
}
