package com.example.cashincontrol.presentation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.cashincontrol.domain.UserClass

@Composable
fun NavGraph(
    navHostController: NavHostController,
    isBottomBarVisible: (Boolean) -> Unit,
    paddingValues: PaddingValues
) {
    NavHost(navController = navHostController, startDestination = "main") {
        composable("main") {
            Main(navHostController)
        }
        composable("analytics") {
            Analytic()
        }
        composable("inflation") {
            Inflation(navController = navHostController, isBottomBarVisible = isBottomBarVisible, paddingValues = paddingValues)
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
        composable("onboarding") {
            OnboardingScreens(
                navController = navHostController,
                onComplete = {
                    UserClass.isOnboardingCompleted = true
                    navHostController.navigate("inflation") {
                        popUpTo("onboarding") { inclusive = true }
                    }
                }
            )
        }
    }
}
