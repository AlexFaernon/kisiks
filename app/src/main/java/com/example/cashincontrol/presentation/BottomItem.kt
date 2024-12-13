package com.example.cashincontrol.presentation

import com.example.cashincontrol.R

sealed class BottomItem(val iconId: Int, val route: String){
    data object ScreenMain: BottomItem(R.drawable.icon_main_new, "main")
    data object ScreenAnalytics: BottomItem(R.drawable.icon_analytics, "analytics")
    data object ScreenInflation: BottomItem(R.drawable.icon_purpose_new, "inflation")
    data object ScreenGoal: BottomItem(R.drawable.icon_goal_new, "goal")
    //data object ScreenSettings: BottomItem(R.drawable.icon_settings_new, "settings")
}