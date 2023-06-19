package com.carles.compose.fakes

import com.carles.compose.data.remote.HyruleApi
import com.carles.compose.data.remote.MonsterDetailResponseDto
import com.carles.compose.data.remote.MonstersResponseDto
import com.google.gson.Gson

class FakeHyruleApi : HyruleApi {

    override suspend fun getMonsters(): MonstersResponseDto {
        return Gson().fromJson(
            MONSTERS_RESPONSE_DTO,
            MonstersResponseDto::class.java
        )
    }

    override suspend fun getMonsterDetail(id: String): MonsterDetailResponseDto {
        val response = when (id) {
            "133" -> MONSTERS_DETAIL_133_RESPONSE_DTO
            "151" -> MONSTERS_DETAIL_151_RESPONSE_DTO
            else -> throw IllegalArgumentException()
        }
        return Gson().fromJson(response, MonsterDetailResponseDto::class.java)
    }
}