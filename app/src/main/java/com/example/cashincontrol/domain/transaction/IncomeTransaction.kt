package com.example.cashincontrol.domain.transaction

import java.time.LocalDate
import java.util.Date

data class IncomeTransaction(
    override val sum: Float,
//    override val card: String,
    override val date: LocalDate,
    override val category: IncomeCategory
) : Transaction()
