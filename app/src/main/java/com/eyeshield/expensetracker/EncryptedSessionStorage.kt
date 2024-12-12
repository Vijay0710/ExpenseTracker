package com.eyeshield.expensetracker

import android.annotation.SuppressLint
import android.content.SharedPreferences
import com.eyeshield.expensetracker.auth.AuthInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EncryptedSessionStorage @Inject constructor(
    private val sharedPreferences: SharedPreferences
) {
    suspend fun get(): AuthInfo? {
        return withContext(Dispatchers.IO) {
            val json = sharedPreferences.getString(AUTH_KEY, null)
            json?.let {
                Json.decodeFromString<AuthInfo>(it)
            }
        }
    }

    @SuppressLint("ApplySharedPref")
    suspend fun set(info: AuthInfo?) {
        withContext(Dispatchers.IO) {
            if (info == null) {
                sharedPreferences
                    .edit()
                    .remove(AUTH_KEY)
                    .commit()
                return@withContext
            }
            val json = Json.encodeToString(info)
            sharedPreferences
                .edit()
                .putString(AUTH_KEY, json)
                .commit()
        }
    }

    suspend fun clear() {
        withContext(Dispatchers.IO) {
            sharedPreferences.edit().clear().commit()
        }
    }

    companion object {
        private const val AUTH_KEY = "AUTH_INFO"
    }
}