package com.example.cashincontrol.presentation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
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
    Text(
        modifier = Modifier.fillMaxSize().wrapContentHeight(),
        text = "Purpose",
        textAlign = TextAlign.Center
    )
}
@Composable
fun Settings() {
        Text(
            modifier = Modifier.fillMaxSize().wrapContentHeight(),
            text = "Settings",
            textAlign = TextAlign.Center
        )
}