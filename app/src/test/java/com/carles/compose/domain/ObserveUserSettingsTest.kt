package com.carles.settings.domain

import com.carles.compose.AppDispatchers
import com.carles.compose.MainDispatcherRule
import com.carles.compose.data.SettingsRepository
import com.carles.settings.UserSettings
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ObserveUserSettingsTest {

    @get:Rule
    var mainDispatcherRule = MainDispatcherRule()

    private val dispatchers = AppDispatchers(Dispatchers.Main, Dispatchers.Main, Dispatchers.Main)
    private val repository: SettingsRepository = mockk()
    private lateinit var usecase: ObserveUserSettings

    @Before
    fun setup() {
        usecase = ObserveUserSettings(repository, dispatchers)
    }

    @Test
    fun `given usecase, when called, then observe user settings`() = runTest {
        val settings = UserSettings(10)

        every { repository.observeUserSettings() } returns flow { emit(settings) }
        val result = usecase.execute().first()
        verify { repository.observeUserSettings() }
        assertEquals(settings, result)
    }
}