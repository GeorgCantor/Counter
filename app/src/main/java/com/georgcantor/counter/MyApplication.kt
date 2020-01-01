package com.georgcantor.counter

import android.app.Application
import com.georgcantor.counter.di.appModule
import com.georgcantor.counter.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@MyApplication)
            modules(arrayListOf(appModule, viewModelModule))
        }
    }

}