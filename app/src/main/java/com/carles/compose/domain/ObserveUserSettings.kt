package com.carles.compose.domain

import com.carles.compose.AppDispatchers
import com.carles.compose.data.SettingsRepository
import com.carles.compose.model.UserSettings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class ObserveUserSettings @Inject constructor(
    private val repository: SettingsRepository,
    private val dispatchers: AppDispatchers
) {

    fun execute(): Flow<UserSettings> {
        return repository.observeUserSettings().flowOn(dispatchers.io)
    }
}