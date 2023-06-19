package com.carles.compose.domain

import com.carles.compose.AppDispatchers
import com.carles.compose.data.HyruleRepository
import com.carles.compose.model.MonsterDetail
import com.carles.hyrule.domain.GetMonsterDetail
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class GetMonsterDetailTest {

    private val dispatcher = UnconfinedTestDispatcher()
    private val dispatchers = AppDispatchers(dispatcher, dispatcher, dispatcher)
    private val repository: HyruleRepository = mockk()
    private lateinit var usecase: GetMonsterDetail

    @Before
    fun setup() {
        usecase = GetMonsterDetail(repository, dispatchers)
    }

    @Test
    fun `given usecase, when called, then get monster detail from repository`() = runTest {
        val monsterDetail = MonsterDetail(1, "Monster", "here", "big monster", "https://url")
        coEvery { repository.getMonsterDetail(any()) } returns monsterDetail
        assertEquals(monsterDetail, usecase.execute(1))
        coVerify { repository.getMonsterDetail(1) }
    }
}