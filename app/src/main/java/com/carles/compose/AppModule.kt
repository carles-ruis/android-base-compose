package com.carles.compose

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.carles.compose.data.local.HyruleDatabase
import com.carles.compose.data.local.MonsterDao
import com.carles.compose.data.remote.HyruleApi
import com.chuckerteam.chucker.api.ChuckerInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    private const val BASE_URL = "https://botw-compendium.herokuapp.com"

    @Provides
    @Singleton
    fun provideRetrofit(app: Application): Retrofit {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            setLevel(HttpLoggingInterceptor.Level.BODY)
        }
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(ChuckerInterceptor.Builder(app.applicationContext).build())
            .build()
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .baseUrl(BASE_URL)
            .build()
    }

    @Provides
    @Singleton
    fun provideDispatchers(): AppDispatchers {
        return AppDispatchers(io = Dispatchers.IO, ui = Dispatchers.Main, default = Dispatchers.Default)
    }

    @Provides
    @Singleton
    fun provideHyruleDatabase(
        @ApplicationContext
        context: Context
    ): HyruleDatabase {
        return Room.databaseBuilder(context, HyruleDatabase::class.java, "hyrule_database")
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideMonsterDao(database: HyruleDatabase): MonsterDao {
        return database.monsterDao()
    }

    @Provides
    @Singleton
    fun provideHyruleApi(retrofit: Retrofit): HyruleApi {
        return retrofit.create(HyruleApi::class.java)
    }
}