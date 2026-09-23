package dev.alimmz.cinemood.core.data.network

import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.java.Java

actual fun provideEngine(): HttpClientEngine {
    return Java.create()
}
