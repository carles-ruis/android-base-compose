package com.carles.compose.domain

import com.carles.compose.AppDispatchers
import com.carles.compose.data.HyruleRepository
import com.carles.compose.model.MonsterDetail
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetMonsterDetail @Inject constructor(
    private val repository: HyruleRepository,
    private val dispatchers: AppDispatchers
) {

    suspend fun execute(id: Int): MonsterDetail {
        return withContext(dispatchers.io) {
            repository.getMonsterDetail(id)
        }
    }
}