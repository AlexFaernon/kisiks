package com.example.cashincontrol.domain.transaction

import java.time.LocalDateTime

data class IncomeTransaction(
    override val sum: Float,
//    override val card: String,
    override val date: LocalDateTime,
    override val category: IncomeCategory,
    override val commentary: String
) : Transaction()
