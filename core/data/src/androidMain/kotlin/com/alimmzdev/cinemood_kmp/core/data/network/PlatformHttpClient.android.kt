package com.alimmzdev.cinemood_kmp.core.data.network

import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.okhttp.OkHttp

actual fun provideEngine(): HttpClientEngine {
    return OkHttp.create()
}