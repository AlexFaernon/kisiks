package com.example.cashincontrol.presentation

import com.example.cashincontrol.R

sealed class BottomItem(val iconId: Int, val route: String){
    data object ScreenMain: BottomItem(R.drawable.icon_main, "main")
    data object ScreenAnalytics: BottomItem(R.drawable.icon_analytics, "analytics")
    data object ScreenPurpose: BottomItem(R.drawable.icon_purpose, "purpose")
    data object ScreenSettings: BottomItem(R.drawable.icon_settings, "settings")
}