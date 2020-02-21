package com.jacquessmuts.overengineered.coroutines

import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext


open class DefaultCoroutineScope : CoroutineScope {
    val job: Job by lazy { SupervisorJob() }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Default + job

    fun clearJobs() {
        coroutineContext.cancelChildren()
    }
}