package com.eyeshield.expensetracker

import android.content.SharedPreferences
import androidx.core.content.edit
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.SerializationStrategy
import kotlinx.serialization.json.Json
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EncryptedStorage @Inject constructor(
    private val sharedPreferences: SharedPreferences
) {
    suspend fun <T> get(key: String, deserializer: DeserializationStrategy<T>): T? {
        return withContext(Dispatchers.IO) {
            val json = sharedPreferences.getString(key, null)
            json?.let {
                Json.decodeFromString(deserializer, it)
            }
        }
    }

    suspend fun <T> set(key: String, serializer: SerializationStrategy<T>, info: T?) {
        withContext(Dispatchers.IO) {
            if (info == null) {
                sharedPreferences
                    .edit(commit = true) {
                        remove(key)
                    }
                return@withContext
            }
            val json = Json.encodeToString(serializer, info)
            sharedPreferences
                .edit(commit = true) {
                    putString(key, json)
                }
        }
    }

    suspend fun clear() {
        withContext(Dispatchers.IO) {
            sharedPreferences.edit(commit = true) { clear() }
        }
    }
}