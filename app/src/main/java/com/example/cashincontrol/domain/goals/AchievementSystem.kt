package com.example.cashincontrol.domain.goals

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.example.cashincontrol.domain.UserClass
import kotlinx.serialization.Serializable
import java.time.LocalDate
import java.time.Period

@Serializable
class AchievementSystem {
    val lowInflation = Achievement(
        "Как тебе такое, Илон Маск?",
        "Уровень инфляции стал ниже уровня инфляции по данным росстата",
    )

    val highInflation = Achievement(
        "100! (24)",
        "Уровень инфляции поднялся до 24%"
    )

    @Composable
    fun CheckInflation(){
        val inflation = Inflation.GlobalInflation
        if (inflation >= 24){
            highInflation.achieve(LocalContext.current)
        }

        if (Inflation.YearInflation.isNotEmpty() && inflation < Inflation.getOfficialForMonth()){
            lowInflation.achieve(LocalContext.current)
        }
    }

    val goalComplete = Achievement(
        "Успешный успех",
        "Добился поставленной финансовой цели"
    )

    @Composable
    fun CheckGoal(){
        goalComplete.achieve(LocalContext.current)
    }

    val noTransactions = Achievement(
        "Ну что там с деньгами?",
        "Не вносил расходы более месяца"
    )

    fun onNewTransaction(){
        noTransactions.reset()
    }

    val noGoalPayment = Achievement(
        "Ваша казна пустеет, Милорд",
        "Не пополнил финансовую цель в текущем месяце."
    )

    fun onGoalPayment(){
        noGoalPayment.reset()
    }

    @Composable
    fun CheckMonthly(){
        val lastTransactionDate = UserClass.transactions.firstOrNull()?.date?.toLocalDate()
        val lastCheckTransactionDate = UserClass.checkTransactions.firstOrNull()?.date?.toLocalDate()
        val compare = {date: LocalDate? -> date != null && Period.between(LocalDate.now(), date).toTotalMonths() > 0}
        if (compare(lastTransactionDate) || compare(lastCheckTransactionDate)){
            noTransactions.achieve(LocalContext.current)
        }

        val lastPaymentDate = UserClass.goal?.getPayments()?.lastOrNull()?.first
        if (compare(lastPaymentDate)){
            noGoalPayment.achieve(LocalContext.current)
        }
    }

    val getAchievements: List<Achievement> = listOf(
        noTransactions,
        noGoalPayment,
        lowInflation,
        highInflation,
        goalComplete
    )
}