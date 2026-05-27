package tech.nullexdev.cinemood.core.data.network

import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.darwin.Darwin


actual fun provideEngine(): HttpClientEngine {
    return Darwin.create()
}