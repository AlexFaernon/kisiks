package com.example.cashincontrol.domain.goals

import java.time.LocalDate
import java.time.Period

data class Goal(
    val goal: String,
    val sum: Float,
    val targetDate: LocalDate){
    val startDate: LocalDate = LocalDate.now()
    public fun monthlyPayment(): Float{
        val months = Period.between(startDate, targetDate).months
        return sum / months
    }
}
