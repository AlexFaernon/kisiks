package com.example.cashincontrol.domain.goals

import com.example.cashincontrol.domain.UserClass
import kotlinx.serialization.Serializable
import java.time.LocalDate
import kotlin.math.min

@Serializable
class RankClass{
    var currentRank: Int = -1
        private set(value){
            field = min(value, 10)
        }

    private val stringRanks = listOf(
        "Новичок",
        "Эксперт Бронзовый",
        "Эксперт Серебряный",
        "Эксперт Золотой",
        "Мастер Бронзовый",
        "Мастер Серебряный",
        "Мастер Золотой",
        "Гуру Бронзовый",
        "Гуру Серебряный",
        "Гуру Золотой",
        "Легенда"
    )

    val stringRank
        get() = if (currentRank == -1) "-" else stringRanks[currentRank]

    fun checkNewRank(){
        val currentDate = LocalDate.now()
        val lastTransaction = UserClass.transactions.lastOrNull()
        val lastCheckTransaction = UserClass.checkTransactions.lastOrNull()

        if (lastCheckTransaction == null && lastTransaction == null){
            currentRank++
            return
        }

        val transactionDate = lastTransaction?.date?.toLocalDate()
        val checkTransaction = lastCheckTransaction?.date?.toLocalDate()

        val checkDate = {current: LocalDate, prev: LocalDate? -> prev != null &&
                (prev.month != current.month || prev.year != current.year)}

        if (checkDate(currentDate, transactionDate) || checkDate(currentDate, checkTransaction)){
            currentRank++
        }
    }
}