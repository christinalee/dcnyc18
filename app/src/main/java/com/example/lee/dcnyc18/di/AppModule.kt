package com.example.lee.dcnyc18.di

import android.preference.PreferenceManager
import com.example.lee.dcnyc18.DCNYCApplication
import com.example.lee.dcnyc18.prefs.ApplicationPreferenceManager
import com.example.lee.dcnyc18.prefs.PrefsManager
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule(private val application: DCNYCApplication) {

    @Provides
    @Singleton
    internal fun providesApplication(): DCNYCApplication {
        return application
    }

    @Provides
    @Singleton
    internal fun providesPreferencesManager(): PrefsManager {
        return ApplicationPreferenceManager(application)
    }
}
