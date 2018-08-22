package com.lee.apps.vivir.di

import dagger.android.AndroidInjector
import dagger.Subcomponent
import android.app.Activity
import com.example.lee.dcnyc18.MainActivity
import dagger.android.ActivityKey
import dagger.multibindings.IntoMap
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector


@Subcomponent
interface MainActivitySubcomponent: AndroidInjector<MainActivity> {
    @Subcomponent.Builder
    abstract class Builder : AndroidInjector.Builder<MainActivity>()
}

@Module(subcomponents = arrayOf(MainActivitySubcomponent::class))
internal abstract class MainActivityModule {
    @Binds
    @IntoMap
    @ActivityKey(MainActivity::class)
    internal abstract fun myActivityInjectorFactory(builder: MainActivitySubcomponent.Builder):
            AndroidInjector.Factory<out Activity>
}