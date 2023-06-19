package com.carles.compose.di

import android.content.Context
import androidx.room.Room
import com.carles.compose.AppDispatchers
import com.carles.compose.AppModule
import com.carles.compose.data.local.HyruleDatabase
import com.carles.compose.data.local.MonsterDao
import com.carles.compose.data.remote.HyruleApi
import com.carles.compose.fakes.FakeHyruleApi
import dagger.Module
import dagger.Provides
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [AppModule::class]
)
@Module
object TestModule {

    @Provides
    @Singleton
    fun provideDispatchers(): AppDispatchers {
        val dispatcher = Dispatchers.Main
        return AppDispatchers(dispatcher, dispatcher, dispatcher)
    }

    @Provides
    @Singleton
    fun provideHyruleDatabase(@ApplicationContext context: Context): HyruleDatabase {
        return Room.inMemoryDatabaseBuilder(context, HyruleDatabase::class.java)
            .fallbackToDestructiveMigration()
            .allowMainThreadQueries()
            .build()
    }

    @Provides
    @Singleton
    fun provideMonsterDao(database: HyruleDatabase): MonsterDao {
        return database.monsterDao()
    }

    @Provides
    @Singleton
    fun provideHyruleApi(): HyruleApi {
        return FakeHyruleApi()
    }
}