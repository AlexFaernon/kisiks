package com.example.cashincontrol

import android.Manifest
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.rememberCoroutineScope
import com.example.cashincontrol.data.saving.UserDataStoreManager
import com.example.cashincontrol.data.saving.database.DbHandler
import com.example.cashincontrol.domain.UserClass
import com.example.cashincontrol.presentation.Start
import com.example.cashincontrol.ui.theme.CashInControlTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private val dataStoreManager: UserDataStoreManager = UserDataStoreManager(this)
    private lateinit var coroutine: CoroutineScope

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { _ ->
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
        setContent {
            CashInControlTheme {
                Start()
                DbHandler.setupDatabase(this)
                UserClass.SetupUserData(dataStoreManager)
                coroutine = rememberCoroutineScope()
            }
        }
    }

    override fun onStop() {
        coroutine.launch {
            dataStoreManager.saveData()
        }
        super.onStop()
    }

    override fun onDestroy() {
        DbHandler.closeDatabase()
        super.onDestroy()
    }
}