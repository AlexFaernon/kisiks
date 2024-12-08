package com.example.cashincontrol.domain.saving

import androidx.datastore.core.Serializer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream


object UserDataSerializer : Serializer<UserDataSaveClass> {
    override val defaultValue: UserDataSaveClass
        get() = UserDataSaveClass()

    override suspend fun readFrom(input: InputStream): UserDataSaveClass {
        return try {
            Json.decodeFromString(
                deserializer = UserDataSaveClass.serializer(),
                string = input.readBytes().decodeToString()
            )
        }
        catch (e: SerializationException){
            e.printStackTrace()
            UserDataSaveClass()
        }
    }

    override suspend fun writeTo(t: UserDataSaveClass, output: OutputStream) {
        withContext(Dispatchers.IO){
            output.write(
                Json.encodeToString(
                    serializer = UserDataSaveClass.serializer(),
                    value = t
                ).encodeToByteArray()
            )
        }
    }

}