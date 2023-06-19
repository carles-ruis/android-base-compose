package com.carles.compose.data.cache

import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class CacheTest {

    private val datastore: AppDatastore = mockk()
    private val cacheKey = CacheKey(CacheItems.MONSTERS)
    private lateinit var cache: Cache

    @Before
    fun setup() {
        cache = Cache(datastore)
    }

    @Test
    fun `given isCached, when no item set, then it returns false`() {
        every { datastore.observeCacheExpirationTime() } returns flow { emit(10) }
        assertFalse(cache.isCached(cacheKey))
    }

    @Test
    fun `given isCached, when item set and expired, then it returns false`() = runTest {
        every { datastore.observeCacheExpirationTime() } returns flow { emit(-1) }
        cache.set(cacheKey)
        assertFalse(cache.isCached(cacheKey))
    }

    @Test
    fun `given isCached, When item is set and not expired, then it returns true`() = runTest {
        every { datastore.observeCacheExpirationTime() } returns flow { emit(10) }
        cache.set(cacheKey)
        assertTrue(cache.isCached(cacheKey))
    }
}