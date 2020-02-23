package com.jacquessmuts.overengineered.coroutines

import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancelChildren

open class DefaultCoroutineScope : CoroutineScope {
    val job: Job by lazy { SupervisorJob() }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Default + job

    fun clearJobs() {
        coroutineContext.cancelChildren()
    }
}
