package com.greenkart

import android.app.Application
import com.greenkart.di.authModule
import com.greenkart.di.homeModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class GreenkartApp : Application() {
    override fun onCreate() {
        super.onCreate()
        
        startKoin {
            androidLogger(Level.ERROR)
            androidContext(this@GreenkartApp)
            modules(authModule, homeModule)
        }
    }
}
