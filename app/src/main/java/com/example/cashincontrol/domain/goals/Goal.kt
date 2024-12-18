package com.example.cashincontrol.domain.goals

import androidx.compose.runtime.Composable
import com.example.cashincontrol.data.saving.LocalDateSerializer
import com.example.cashincontrol.domain.UserClass
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


    private val payments: MutableList<Pair<@Serializable(with = LocalDateSerializer::class) LocalDate, Float>> = mutableListOf()
    val isAchieved = payments.sumOf { it.second.toDouble() } >= sum

    fun monthlyPayment(): Float{
        val months = Period.between(startDate, targetDate).toTotalMonths()
        return sum / months
    }

    fun getInflationPayments(): List<Float>{
        val months = Period.between(startDate, targetDate).toTotalMonths()
        val result = mutableListOf(monthlyPayment())

        for (i in 1..<months){
            val prev = result.last()
            result.add(prev * (Inflation.GlobalInflation / 100 + 1))
        }

        return result
    }

    @Composable
    fun AddPayment(date: LocalDate, sum: Float) {
        payments.add(Pair(date, sum))
        UserClass.achievementSystem.onGoalPayment()
        if (isAchieved){
            UserClass.achievementSystem.CheckGoal()
        }
    }

    fun getPayments(): List<Pair<LocalDate, Float>> = payments
}