package com.example.cashincontrol.presentation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

@Composable
fun NavGraph(
    navHostController: NavHostController
) {
    NavHost(navHostController, startDestination = "main") {
        composable("main") {
            Main(navHostController)
        }
        composable("analytics") {
            Analytic()
        }
        composable("purpose") {
            Purpose()
        }
        composable("goal") {
            Goal(navHostController)
        }
        composable("settings") {
            Settings()
        }
        composable("add") {
            AddScreen(navHostController)
        }
        composable("addGoal") {
            AddGoalScreen(navHostController)
        }
    }
}