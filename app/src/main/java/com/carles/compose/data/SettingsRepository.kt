package com.carles.compose.data

import com.carles.compose.data.cache.AppDatastore
import com.carles.compose.data.cache.Cache
import com.carles.compose.model.UserSettings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SettingsRepository @Inject constructor(
    private val cache: Cache,
    private val appDatastore: AppDatastore
) {

    suspend fun setCacheExpirationTime(expirationTime: Int) {
        cache.resetCacheExpiration()
        appDatastore.setCacheExpirationTime(expirationTime)
    }

    fun observeUserSettings(): Flow<UserSettings> {
        return appDatastore.observeCacheExpirationTime().map { expirationTime ->
            UserSettings(expirationTime)
        }
    }
}