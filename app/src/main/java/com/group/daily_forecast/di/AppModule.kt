package com.group.daily_forecast.di

import android.content.Context
import androidx.room.Room
import androidx.viewbinding.BuildConfig
import com.group.daily_forecast.data.local.MainDAO
import com.group.daily_forecast.data.local.RoomManger
import com.group.daily_forecast.data.network.MainServices
import com.group.daily_forecast.utils.Constants
import com.group.daily_forecast.utils.Constants.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    private const val TIMEOUT = 30L

    @Provides
    @Singleton
    fun providesDispatchersIO(): CoroutineDispatcher = Dispatchers.IO

    @Provides
    @Singleton
    fun providesOkHttp(): OkHttpClient = OkHttpClient.Builder().apply {
        if (BuildConfig.DEBUG) {
            addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
        }
        connectTimeout(TIMEOUT, TimeUnit.SECONDS)
        writeTimeout(TIMEOUT, TimeUnit.SECONDS)
        readTimeout(TIMEOUT, TimeUnit.SECONDS)
    }.build()

    @Provides
    @Singleton
    fun providesRetrofit(okHttpClient: OkHttpClient): Retrofit = Retrofit.Builder().apply {
        baseUrl(BASE_URL)
        client(okHttpClient)
        addConverterFactory(GsonConverterFactory.create())
    }.build()

    @Provides
    @Singleton
    fun provideMainServices(retrofit: Retrofit): MainServices {
        return retrofit.create(MainServices::class.java)
    }

    @Singleton
    @Provides
    fun provideMainDatabase(
        @ApplicationContext app: Context
    ) = Room.databaseBuilder(
        app,
        RoomManger::class.java,
        Constants.DB_NAME
    )
//        .allowMainThreadQueries()
        .fallbackToDestructiveMigration()
        .build()

    @Singleton
    @Provides
    fun provideMainDao(db: RoomManger): MainDAO = db.mainDAO()
}