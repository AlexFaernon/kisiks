package com.example.cashincontrol.presentation

import android.annotation.SuppressLint
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun Start() {
    val navController = rememberNavController()
    Scaffold(
        bottomBar = {
            val navBackStackEntry = navController.currentBackStackEntryAsState().value
            val currentRoute = navBackStackEntry?.destination?.route

            if (currentRoute in listOf(
                    BottomItem.ScreenMain.route,
                    BottomItem.ScreenAnalytics.route,
                    BottomItem.ScreenTarget.route,
                    BottomItem.ScreenPurpose.route,
                    BottomItem.ScreenSettings.route)
                ) {
                BottomNavigation(navController = navController)
            }
        },
    ) {
        NavGraph(navHostController = navController)
    }
}