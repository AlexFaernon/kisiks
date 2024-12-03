package com.example.cashincontrol.domain.goals

import co.yml.charts.common.model.Point
import java.time.LocalDate
import java.time.Period

data class Goal(
    val goal: String,
    val sum: Float,
    val targetDate: LocalDate){
    val startDate: LocalDate = LocalDate.now()
    
    fun monthlyPayment(): Float{
        val months = Period.between(startDate, targetDate).toTotalMonths()
        return sum / months
    }
}

