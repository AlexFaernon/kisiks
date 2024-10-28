package com.example.cashincontrol

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.cashincontrol.presentation.Start
import com.example.cashincontrol.ui.theme.CashInControlTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CashInControlTheme {
                Start()
            }
        }
    }
}