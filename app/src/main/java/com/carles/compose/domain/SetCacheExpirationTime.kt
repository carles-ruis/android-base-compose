package com.carles.compose.domain

import com.carles.compose.AppDispatchers
import com.carles.compose.data.SettingsRepository
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SetCacheExpirationTime @Inject constructor(
    private val repository: SettingsRepository,
    private val dispatchers: AppDispatchers
) {

    suspend fun execute(expirationTime: Int) = withContext(dispatchers.io) {
        repository.setCacheExpirationTime(expirationTime)
    }
}