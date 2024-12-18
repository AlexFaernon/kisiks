package com.example.cashincontrol.domain.goals

import androidx.core.math.MathUtils.clamp
import com.example.cashincontrol.data.saving.LocalDateSerializer
import kotlinx.serialization.Serializable
import java.time.LocalDate

@Serializable
class RankClass{
    var currentRank: Int = 0
        private set(value){
            field = clamp(value, 0, 10)
        }

    @Serializable(with = LocalDateSerializer::class)
    private var lastUploadDate: LocalDate? = null

    private val stringRanks = listOf(
        "Нет ранга",
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
        get() = stringRanks[currentRank]

    fun checkNewRank(){
        val currentDate = LocalDate.now()
        if (lastUploadDate == null || currentDate.month != lastUploadDate!!.month || currentDate.year != lastUploadDate!!.year){
            currentRank++
        }
        lastUploadDate = currentDate
    }
}