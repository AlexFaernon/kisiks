package com.example.cashincontrol.data.saving

import com.example.cashincontrol.domain.goals.Goal
import kotlinx.serialization.Serializable
import java.time.LocalDate

@Serializable
data class UserDataSaveClass(
    val goal: Goal? = null,
    @Serializable(with = LocalDateSerializer::class)
    val startDate: LocalDate = LocalDate.now(),
    val onboardingCompleted: Boolean = false
)
