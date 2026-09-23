package dev.alimmz.cinemood

import android.app.Application
import dev.alimmz.cinemood.di.initKoin
import org.koin.android.ext.koin.androidContext

class CineMoodApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin {
            androidContext(this@CineMoodApplication)
        }
    }
}
