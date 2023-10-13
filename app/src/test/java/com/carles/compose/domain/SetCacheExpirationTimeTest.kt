package com.carles.compose.domain

import com.carles.compose.AppDispatchers
import com.carles.compose.MainDispatcherRule
import com.carles.compose.data.SettingsRepository
import com.carles.compose.domain.SetCacheExpirationTime
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class SetCacheExpirationTimeTest {

    @get:Rule
    var mainDispatcherRule = MainDispatcherRule()

    private val dispatchers = AppDispatchers(Dispatchers.Main, Dispatchers.Main, Dispatchers.Main)
    private val repository: SettingsRepository = mockk()
    private lateinit var usecase: SetCacheExpirationTime

    @Before
    fun setup() {
        usecase = SetCacheExpirationTime(repository, dispatchers)
    }

    @Test
    fun `given usecase, when called, then reset cache from repository`() = runTest {
        coEvery { repository.setCacheExpirationTime(any()) } just Runs
        usecase.execute(10)
        coVerify { repository.setCacheExpirationTime(10) }
    }
}
