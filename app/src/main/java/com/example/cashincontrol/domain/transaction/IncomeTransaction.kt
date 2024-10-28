package com.example.cashincontrol.domain.transaction

import java.util.Date

data class IncomeTransaction(
    override val sum: Float,
    override val card: String,
    override val date: Date,
    val category: IncomeCategory
) : Transaction()