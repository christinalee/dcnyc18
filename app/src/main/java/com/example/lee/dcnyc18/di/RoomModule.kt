package com.example.lee.dcnyc18.di

import android.app.Application
import android.arch.persistence.room.Room
import com.example.lee.dcnyc18.db.APPLICATION_NAME
import com.example.lee.dcnyc18.db.AppDatabase
import com.example.lee.dcnyc18.db.PhotoDao
import com.example.lee.dcnyc18.db.PhotoDataSource
import com.example.lee.dcnyc18.models.DCNYCDispatchers
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(
        includes = arrayOf(DispatchersModule::class)
)
class RoomModule(application: Application) {

    private val appDatabase: AppDatabase by lazy {
        Room.databaseBuilder(
                application, AppDatabase::class.java, APPLICATION_NAME)
                .fallbackToDestructiveMigration()
                .build()
    }

    @Singleton
    @Provides
    internal fun providesRoomDatabase(): AppDatabase {
        return appDatabase
    }

    @Singleton
    @Provides
    internal fun providesPhotoDao(appDatabase: AppDatabase): PhotoDao {
        return appDatabase.photoDao()
    }

    @Singleton
    @Provides
    internal fun photoDataSource(photoDao: PhotoDao, dispatchers: DCNYCDispatchers): PhotoDataSource {
        return PhotoDataSource(photoDao, dispatchers)
    }
}
