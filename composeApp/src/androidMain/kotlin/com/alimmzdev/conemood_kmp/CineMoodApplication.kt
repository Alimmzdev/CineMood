package com.alimmzdev.conemood_kmp

import android.app.Application
import com.alimmzdev.conemood_kmp.di.initKoin
import org.koin.android.ext.koin.androidContext

class CineMoodApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin {
            androidContext(this@CineMoodApplication)
        }
    }
}