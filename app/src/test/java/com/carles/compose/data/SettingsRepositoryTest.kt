package com.carles.compose.data

import com.carles.compose.data.cache.AppDatastore
import com.carles.compose.data.cache.Cache
import com.carles.compose.model.UserSettings
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class SettingsRepositoryTest {

    private val cache: Cache = mockk()
    private val datastore: AppDatastore = mockk()
    private lateinit var repository: SettingsRepository

    @Before
    fun setup() {
        repository = SettingsRepository(cache, datastore)
    }

    @Test
    fun `given setCacheExpirationTime, when called, then reset cache, store new value and emit`() = runTest {
        coEvery { cache.resetCacheExpiration() } just Runs
        coEvery { datastore.setCacheExpirationTime(any()) } just Runs

        repository.setCacheExpirationTime(10)

        coVerify { cache.resetCacheExpiration() }
        coVerify { datastore.setCacheExpirationTime(10) }
    }

    @Test
    fun `given getUserSettings, when called, then return UserSettings object with preferences value`() = runTest {
        coEvery { datastore.observeCacheExpirationTime() } returns flow { emit(10) }
        val result = repository.observeUserSettings().first()
        coVerify { datastore.observeCacheExpirationTime() }
        assertEquals(UserSettings(10), result)
    }
}