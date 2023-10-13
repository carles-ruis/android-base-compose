package com.carles.compose.model.mapper

import com.carles.compose.data.local.MonsterEntity
import com.carles.compose.data.remote.MonstersResponseDto
import com.carles.compose.model.Monster
import javax.inject.Inject

class MonstersMapper @Inject constructor() {

    fun toEntity(dto: MonstersResponseDto): List<MonsterEntity> =
        dto.data.map { MonsterEntity(it.id, it.name) }

    fun fromEntity(entity: List<MonsterEntity>): List<Monster> =
        entity.map { Monster(it.id, it.name) }
}