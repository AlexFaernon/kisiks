package com.example.cashincontrol.domain.transaction

import java.time.LocalDate

data class ExpensesTransaction(
    override val sum: Float,
//    override val card: String,
    override val date: LocalDate,
    override val category: ExpensesCategory,
    override val commentary: String
) : Transaction(){
}
