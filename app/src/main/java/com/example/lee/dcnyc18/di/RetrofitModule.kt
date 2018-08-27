package com.example.lee.dcnyc18.di

import com.example.lee.dcnyc18.network.HeaderInterceptor
import com.example.lee.dcnyc18.network.UnsplashService
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.experimental.CoroutineCallAdapterFactory
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
class RetrofitModule {
    private val okHttpClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
                .addInterceptor(HeaderInterceptor)
                .build()
    }

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(MoshiConverterFactory.create())
                // Network 1: Add CoroutineCallAdapterFactory adapter factory
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
//                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .build()
    }

    @Singleton
    @Provides
    internal fun providesUnsplashService(): UnsplashService {
        return retrofit.create(UnsplashService::class.java)
    }

    companion object {
        private const val BASE_URL = "https://api.unsplash.com/"
    }
}