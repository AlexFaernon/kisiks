package com.example.cashincontrol.domain.transaction

import java.time.LocalDateTime

data class CheckTransaction(
    val date: LocalDateTime,
    val category: Map<CheckCategory, Float>
)