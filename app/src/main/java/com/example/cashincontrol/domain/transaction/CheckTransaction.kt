package com.example.cashincontrol.domain.transaction

import java.time.LocalDateTime

data class CheckTransaction(
    val date: LocalDateTime,
    val categories: Map<CheckCategory, Float>
)