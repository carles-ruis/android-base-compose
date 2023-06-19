package com.carles.hyrule.domain

import com.carles.compose.AppDispatchers
import com.carles.compose.data.HyruleRepository
import com.carles.compose.model.Monster
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RefreshMonsters @Inject constructor(
    private val repository: HyruleRepository,
    private val dispatchers: AppDispatchers
) {

    suspend fun execute(): List<Monster> {
        return withContext(dispatchers.io) {
            repository.refreshMonsters()
        }
    }
}