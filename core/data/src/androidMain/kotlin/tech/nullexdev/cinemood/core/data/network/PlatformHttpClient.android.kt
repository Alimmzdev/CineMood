package tech.nullexdev.cinemood.core.data.network

import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.okhttp.OkHttp

actual fun provideEngine(): HttpClientEngine {
    val chuckerCollector = ChuckerCollector(
        context = this,
        // Toggles visibility of the notification
        showNotification = true,
        // Allows to customize the retention period of collected data
        retentionPeriod = RetentionManager.Period.ONE_HOUR
    )

// Create the Interceptor
    val chuckerInterceptor = ChuckerInterceptor.Builder(context)
        // The previously created Collector
        .collector(chuckerCollector)
        // The max body content length in bytes, after this responses will be truncated.
        .maxContentLength(250_000L)
        // List of headers to replace with ** in the Chucker UI
        .redactHeaders("Auth-Token", "Bearer")
        // Read the whole response body even when the client does not consume the response completely.
        // This is useful in case of parsing errors or when the response body
        // is closed before being read like in Retrofit with Void and Unit types.
        .alwaysReadResponseBody(true)
        // Use decoder when processing request and response bodies. When multiple decoders are installed they
        // are applied in an order they were added.
        .addBodyDecoder(decoder)
        // Controls Android shortcut creation.
        .createShortcut(true)
        .build()
    return OkHttp.create()
}