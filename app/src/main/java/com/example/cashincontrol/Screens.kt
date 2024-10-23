package com.example.cashincontrol

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign


@Composable
fun Main() {
    Text(
        modifier = Modifier.fillMaxSize().wrapContentHeight(),
        text = "Main",
        textAlign = TextAlign.Center
    )
}
@Composable
fun Analytic() {
    Text(
        modifier = Modifier.fillMaxSize().wrapContentHeight(),
        text = "Analytic",
        textAlign = TextAlign.Center
    )
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