package ru.smartms.rfidreaderwriter.di

import android.content.Context
import dagger.Module
import dagger.Provides


@Module
class ContextModule(val context: Context) {

    @Provides
    fun context(): Context {
        return context.applicationContext
    }
}