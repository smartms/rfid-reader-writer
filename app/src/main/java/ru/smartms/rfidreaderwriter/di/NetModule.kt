package ru.smartms.smallsmarty.di

import dagger.Provides
import javax.inject.Singleton
import android.preference.PreferenceManager
import android.app.Application
import android.content.SharedPreferences
import dagger.Module

@Module
class NetModule {

    @Provides
    @Singleton
    fun providesSharedPreferences(application: Application): SharedPreferences =
        PreferenceManager.getDefaultSharedPreferences(application)
}