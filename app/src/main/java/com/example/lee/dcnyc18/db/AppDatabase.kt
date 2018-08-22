package com.example.lee.dcnyc18.db

import android.app.Application
import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import com.example.lee.dcnyc18.models.Photo

const val APPLICATION_NAME = "app-db"
private const val SCHEMA_VERSION = 2

@Database(
        entities = [(Photo::class)],
        version = SCHEMA_VERSION
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun photoDao(): PhotoDao
}

object Database {
    private var internalInstance: AppDatabase? = null

    fun getInstance(application: Application) : AppDatabase {
        return internalInstance ?: Room.databaseBuilder(application, AppDatabase::class.java, APPLICATION_NAME)
                .fallbackToDestructiveMigration()
                .build()
                .also { internalInstance = it }
    }
}



