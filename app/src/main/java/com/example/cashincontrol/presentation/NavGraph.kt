package com.example.cashincontrol.presentation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

@Composable
fun NavGraph(
    navHostController: NavHostController
) {
    NavHost(navController = navHostController, startDestination = "main"){
        composable("main"){
            Main()
        }
        composable("analytics"){
            Analytic()
        }
        composable("purpose"){
            Purpose()
        }
        composable("settings"){
            Settings()
        }
    }
}