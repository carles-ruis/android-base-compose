package com.carles.compose.domain

import com.carles.compose.AppDispatchers
import com.carles.compose.MainDispatcherRule
import com.carles.compose.data.HyruleRepository
import com.carles.compose.model.Monster
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class RefreshMonstersTest {

    @get:Rule
    var mainDispatcherRule = MainDispatcherRule()

    private val dispatchers = AppDispatchers(Dispatchers.Main, Dispatchers.Main, Dispatchers.Main)
    private val repository: HyruleRepository = mockk()
    private lateinit var usecase: RefreshMonsters

    @Before
    fun setup() {
        usecase = RefreshMonsters(repository, dispatchers)
    }

    @Test
    fun `given usecase, when called, then refresh monsters from repository`() = runTest {
        val monsters = listOf(Monster(1, "Monster"))
        coEvery { repository.refreshMonsters() } returns monsters
        assertEquals(monsters, usecase.execute())
        coVerify { repository.refreshMonsters() }
    }
}