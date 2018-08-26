package com.example.lee.dcnyc18.di

import com.example.lee.dcnyc18.DCNYCApplication
import com.example.lee.dcnyc18.MainActivity
import com.example.lee.dcnyc18.db.AppDatabase
import com.example.lee.dcnyc18.db.PhotoDao
import com.example.lee.dcnyc18.db.PhotoDataSource
import com.example.lee.dcnyc18.models.DCNYCDispatchers
import com.example.lee.dcnyc18.network.UnsplashService
import com.example.lee.dcnyc18.prefs.PrefsManager
import com.example.lee.dcnyc18.ui.PhotoAdapter
import com.lee.apps.vivir.di.MainActivityModule
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton


@Singleton
@Component(dependencies = arrayOf(), modules = arrayOf(
        AppModule::class,
        RoomModule::class,
        RetrofitModule::class,
        MainActivityModule::class,
        AndroidInjectionModule::class,
        AndroidSupportInjectionModule::class,
        ViewModelModule::class,
        DispatchersModule::class
))
interface AppComponent {
    fun inject(mainActivity: MainActivity)

    fun inject(application: DCNYCApplication)

    fun photoDao(): PhotoDao

    fun appDatabase(): AppDatabase

    fun photoDataSource(): PhotoDataSource

    fun application(): DCNYCApplication

    fun unsplashService(): UnsplashService

    fun prefsManager(): PrefsManager

    fun dispatchers(): DCNYCDispatchers
}
