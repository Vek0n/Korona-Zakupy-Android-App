package com.maskjs.korona_zakupy

import android.app.Application
import com.maskjs.korona_zakupy.di.AppModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class KoronaZakupyApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@KoronaZakupyApplication)
            androidLogger()
            modules(AppModule.appModule)
        }
    }
}