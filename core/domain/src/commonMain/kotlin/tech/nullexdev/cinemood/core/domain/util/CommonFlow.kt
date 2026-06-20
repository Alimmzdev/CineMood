package tech.nullexdev.cinemood.core.domain.util

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class FlowWatcher<T : Any>(
    private val flow: Flow<T>,
    private val onEach: (T) -> Unit
) {
    private var job: Job? = null

    fun start(scope: CoroutineScope) {
        job = flow.onEach { onEach(it) }.launchIn(scope)
    }

    fun stop() {
        job?.cancel()
        job = null
    }
}

fun <T : Any> Flow<T>.doWatch(onEach: (T) -> Unit): FlowWatcher<T> {
    val watcher = FlowWatcher(this, onEach)
    watcher.start(CoroutineScope(Dispatchers.Main))
    return watcher
}
