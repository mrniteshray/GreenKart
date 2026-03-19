package appxyz.greenkart

import android.app.Application
import appxyz.greenkart.di.authModule
import appxyz.greenkart.di.homeModule
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
            modules(authModule, homeModule, appxyz.greenkart.di.cartModule)
        }
    }
}

