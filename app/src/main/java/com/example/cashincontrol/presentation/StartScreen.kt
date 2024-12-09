package com.example.cashincontrol.presentation

import android.annotation.SuppressLint
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun Start() {
    val navController = rememberNavController()
    val isBottomBarVisible = remember { mutableStateOf(true) }

    Scaffold(
        bottomBar = {
            val navBackStackEntry = navController.currentBackStackEntryAsState().value
            val currentRoute = navBackStackEntry?.destination?.route

            if (isBottomBarVisible.value && currentRoute in listOf(
                    BottomItem.ScreenMain.route,
                    BottomItem.ScreenAnalytics.route,
                    BottomItem.ScreenInflation.route,
                    BottomItem.ScreenGoal.route,
                    BottomItem.ScreenSettings.route
                )
            ) {
                BottomNavigation(navController = navController)
            }

        },
    ) { paddingValues ->
        NavGraph(navHostController = navController, isBottomBarVisible = { isBottomBarVisible.value = it }, paddingValues)
    }
}