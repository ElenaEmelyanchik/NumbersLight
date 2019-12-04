package com.example.numberslight.di

import android.content.Context
import com.example.numberslight.BuildConfig
import com.example.numberslight.service.repository.ApiRestRepository
import com.example.numberslight.service.repository.Repository
import com.example.numberslight.service.repository.NumbersLightApi
import com.example.numberslight.utils.URL_API
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.internal.platform.Platform
import okhttp3.internal.platform.Platform.INFO
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class NetworkModule {

    @Provides
    @Singleton
    @LoggingInterceptor
    fun provideLoggingInterceptor(): Interceptor {
        val loggingInterceptor = HttpLoggingInterceptor { message ->
            if(BuildConfig.LOG_DEBUG_MODE) {
                Platform.get().log(INFO, message, null)
            }
        }
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        return loggingInterceptor
    }

    @Provides
    @Singleton
    @NetworkConnectionInterceptor
    fun provideNoNetworkConnectionInterceptor(context: Context): Interceptor {
        return NoNetworkConnectionInterceptor(context)
    }

    @Provides
    @Singleton
    fun provideRetrofit(client: OkHttpClient, gson: Gson): Retrofit {
        return Retrofit.Builder()
            .baseUrl(URL_API)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(client)
            .build()
    }

    @Provides
    @Singleton
    internal fun provideOkHttpClient(
        @LoggingInterceptor loggingInterceptor: Interceptor,
        @NetworkConnectionInterceptor noNetworkConnectionInterceptor: Interceptor
    ): OkHttpClient =
        OkHttpClient
            .Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(noNetworkConnectionInterceptor)
            .build()

    @Provides
    @Singleton
    fun provideGson(): Gson =
        GsonBuilder().create()

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): NumbersLightApi {
        return retrofit.create(NumbersLightApi::class.java)
    }

    @Provides
    @Singleton
    internal fun provideRepository(api: NumbersLightApi): Repository {
        return ApiRestRepository(api)
    }
}