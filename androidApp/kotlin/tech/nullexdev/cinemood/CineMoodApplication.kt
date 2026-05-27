package tech.nullexdev.cinemood

import android.app.Application
import tech.nullexdev.cinemood.di.initKoin
import org.koin.android.ext.koin.androidContext

class CineMoodApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin {
            androidContext(this@CineMoodApplication)
        }
    }
}