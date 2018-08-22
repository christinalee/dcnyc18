package com.example.lee.dcnyc18

import android.app.Activity
import android.app.Application
import com.example.lee.dcnyc18.di.AppModule
import com.example.lee.dcnyc18.di.DaggerAppComponent
import com.example.lee.dcnyc18.di.RoomModule
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import javax.inject.Inject


class DCNYCApplication : Application(), HasActivityInjector {

    @Inject
    protected lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Activity>

    override fun onCreate() {
        super.onCreate()

        DaggerAppComponent.builder()
                .appModule(AppModule(this))
                .roomModule(RoomModule(this))
                .build()
                .inject(this)
    }

    override fun activityInjector(): DispatchingAndroidInjector<Activity> {
        return dispatchingAndroidInjector
    }
}



