package com.example.cashincontrol

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.rememberCoroutineScope
import com.example.cashincontrol.domain.UserClass
import com.example.cashincontrol.domain.saving.UserDataStoreManager
import com.example.cashincontrol.domain.saving.database.DbHandler
import com.example.cashincontrol.presentation.Start
import com.example.cashincontrol.ui.theme.CashInControlTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private val dataStoreManager: UserDataStoreManager = UserDataStoreManager(this)
    private lateinit var coroutine: CoroutineScope

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CashInControlTheme {
                Start()
                DbHandler.setupDatabase(this)
                UserClass.SetupUserData(dataStoreManager)
                coroutine = rememberCoroutineScope()
            }
        }
    }

    override fun onDestroy() {
        DbHandler.closeDatabase()
        coroutine.launch {
            dataStoreManager.saveData()
        }
        super.onDestroy()
    }
}