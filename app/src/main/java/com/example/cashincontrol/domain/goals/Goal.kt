package com.example.cashincontrol.domain.goals

import com.example.cashincontrol.domain.saving.LocalDateSerializer
import kotlinx.serialization.Serializable
import java.time.LocalDate
import java.time.Period

@Serializable
data class Goal(
    val goal: String,
    val sum: Float,
    @Serializable(with = LocalDateSerializer::class)
    val targetDate: LocalDate,
    @Serializable(with = LocalDateSerializer::class)
    val startDate: LocalDate = LocalDate.now()){

    fun monthlyPayment(): Float{
        val months = Period.between(startDate, targetDate).toTotalMonths()
        return sum / months
    }
}

