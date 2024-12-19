package com.example.cashincontrol.presentation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavController

@Composable
fun Main(navController: NavController) {
    MainScreen(navController)
}

@Composable
fun Analytic(paddingValues: PaddingValues) {
    AnalyticsScreen(paddingValues)
}

@Composable
fun Inflation(navController: NavController, isBottomBarVisible: (Boolean) -> Unit, paddingValues: PaddingValues) {
    InflationScreen(navController,isBottomBarVisible, paddingValues)
}

@Composable
fun Goal(navController: NavController, paddingValues: PaddingValues) {
    GoalScreen(navController, paddingValues)
}

@Composable
fun Settings() {
        Text(
            modifier = Modifier.fillMaxSize().wrapContentHeight(),
            text = "Settings",
            textAlign = TextAlign.Center
        )
}