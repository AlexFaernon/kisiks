package com.example.cashincontrol

sealed class BottomItem(val iconId: Int, val route: String){
    data object Screen1: BottomItem(R.drawable.icon, "main")
    data object Screen2: BottomItem( R.drawable.icon, "analytics")
    data object Screen3: BottomItem( R.drawable.icon, "purpose")
    data object Screen4: BottomItem( R.drawable.icon, "settings")
}