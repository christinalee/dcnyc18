package com.example.lee.dcnyc18.di

import com.example.lee.dcnyc18.models.DCNYCDispatchers
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DispatchersModule {

    @Singleton
    @Provides
    internal fun providesDispatchers(): DCNYCDispatchers {
        return DCNYCDispatchers()
    }
}