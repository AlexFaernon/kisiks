package com.example.cashincontrol.domain.transaction

import java.time.LocalDateTime

data class ExpensesTransaction(
    override val sum: Float,
    override val date: LocalDateTime,
    override val category: ExpensesCategory,
    override val commentary: String
) : Transaction(sum, date, commentary)
