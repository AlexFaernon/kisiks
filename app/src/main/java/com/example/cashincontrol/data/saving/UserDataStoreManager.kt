package com.example.cashincontrol.data.saving

import android.content.Context
import androidx.datastore.dataStore
import com.example.cashincontrol.domain.UserClass

private val Context.protoDataStore by dataStore("UserData.json", UserDataSerializer)
class UserDataStoreManager(private val context: Context) {
    suspend fun saveData(){
        val save = UserDataSaveClass(UserClass.goal, UserClass.startDate, UserClass.isOnboardingCompleted, UserClass.rank, UserClass.achievementSystem)
        context.protoDataStore.updateData { save }
    }

    fun getData() = context.protoDataStore.data
}