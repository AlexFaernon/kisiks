package com.example.cashincontrol.presentation

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
fun Analytic() {
    AnalyticsScreen()
}

@Composable
fun Purpose() {
    PurposeScreen()
}

@Composable
fun Goal(navController: NavController) {
    GoalScreen(navController)
}

@Composable
fun Settings() {
        Text(
            modifier = Modifier.fillMaxSize().wrapContentHeight(),
            text = "Settings",
            textAlign = TextAlign.Center
        )
}