package com.example.cashincontrol.domain.goals

import android.content.Context
import com.example.cashincontrol.domain.notifications.showNotification
import kotlinx.serialization.Serializable

@Serializable
data class Achievement(
    val name: String,
    val description: String,
    val icon: Int = 0
){
    var achieved = false

    fun achieve(context: Context){
        if (achieved) return

        showNotification(context, name, description)
        achieved = true
    }

    fun reset(){
        achieved = false
    }
}