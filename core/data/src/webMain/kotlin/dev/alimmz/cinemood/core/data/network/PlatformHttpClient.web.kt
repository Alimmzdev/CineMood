package dev.alimmz.cinemood.core.data.network

import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.js.Js

actual fun provideEngine(): HttpClientEngine {
    return Js.create()
}