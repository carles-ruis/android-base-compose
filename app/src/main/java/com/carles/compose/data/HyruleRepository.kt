package com.carles.compose.data

import com.carles.compose.data.local.ItemNotCachedException
import com.carles.compose.data.local.HyruleLocalDatasource
import com.carles.compose.data.remote.HyruleRemoteDatasource
import com.carles.compose.model.Monster
import com.carles.compose.model.MonsterDetail
import com.carles.compose.model.mapper.MonsterDetailMapper
import com.carles.compose.model.mapper.MonstersMapper
import javax.inject.Inject

class HyruleRepository @Inject constructor(
    private val localDatasource: HyruleLocalDatasource,
    private val remoteDatasource: HyruleRemoteDatasource,
    private val monstersMapper: MonstersMapper,
    private val monsterDetailMapper: MonsterDetailMapper
) {

    suspend fun getMonsters(): List<Monster> {
        return try {
            val entities = localDatasource.getMonsters()
            monstersMapper.fromEntity(entities)
        } catch (e: ItemNotCachedException) {
            refreshMonsters()
        }
    }

    suspend fun refreshMonsters(): List<Monster> {
        val dtos = remoteDatasource.getMonsters()
        val entities = monstersMapper.toEntity(dtos)
        localDatasource.persist(entities)
        val localEntities = localDatasource.getMonsters()
        return monstersMapper.fromEntity(localEntities)
    }

     suspend fun getMonsterDetail(id: Int): MonsterDetail {
        return try {
            val entity = localDatasource.getMonsterDetail(id)
            monsterDetailMapper.fromEntity(entity)
        } catch (e: ItemNotCachedException) {
            refreshMonsterDetail(id)
        }
    }

    suspend fun refreshMonsterDetail(id: Int): MonsterDetail {
        val dto = remoteDatasource.getMonsterDetail(id)
        val entity = monsterDetailMapper.toEntity(dto)
        localDatasource.persist(entity)
        val localEntity = localDatasource.getMonsterDetail(id)
        return monsterDetailMapper.fromEntity(localEntity)
    }
}