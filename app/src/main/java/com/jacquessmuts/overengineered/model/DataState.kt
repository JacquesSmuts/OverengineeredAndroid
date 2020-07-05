package com.jacquessmuts.overengineered.model

import com.jacquessmuts.overengineered.api.ApiResult
import com.jacquessmuts.overengineered.api.Failure
import com.jacquessmuts.overengineered.api.Success
import com.jacquessmuts.overengineered.api.onSuccess
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.selects.select
import kotlin.reflect.KSuspendFunction0

/**
 * A DataState is an object which may contain data for a given type. This data comes from an API
 * call, and/or local persistence. It is meant to be used in a coroutine Flow to update a view
 * based on both the data contents as well as the state of those contents.
 *
 * The possible DataStates include [NoData], [StaleData], [FallbackData] and [FreshData].
 */
sealed class DataState<T> {

    val anyData: T?
        get() = when (this) {
            is NoData -> null
            is StaleData -> localData
            is FallbackData -> localData
            is FreshData -> apiData
        }

    @ExperimentalCoroutinesApi
    companion object {

        /**
         * Builds up a Flow of DataStates from a given localData flow and apiCall reference.
         * Optionally updates the data after a single successful api call.
         *
         * Once the data is Fresh™️, any future changes to the data is assumed to also be Fresh™️.
         *
         * The assumption here is that saving local data automatically results in the localData flow
         * also updating. If that does not happen, the flow's last emission will be a result of the
         * final api Call's status: FreshData, NoData or BackupData.
         *
         * The expected flow of data coming from this state would generally be one of the following:
         * - Quick api [Success]: [FreshData]
         * - Slow api [Success] with local data: [StaleData] -> [FreshData]
         * - Slow api [Success] without local data: -> [FreshData]
         * - Quick api [Failure] with local data: [FallbackData]
         * - Slow api [Failure] with local data: [StaleData] -> [FallbackData]
         * - any api [Failure] without fallback data: [NoData]
         *
         * @param localData a Flow reference to localData, that will update once data is inserted
         * @param apiCall the call made to get the newest data
         * @param saveLocalData the optional call made to update the data locally
         * @param timeoutInMillisForStale the time before staleData is emitted.
         */
        fun <T> buildDataState(
            coroutineScope: CoroutineScope,
            localData: Flow<T?>,
            apiCall: KSuspendFunction0<ApiResult<T>>,
            saveLocalData: suspend (T) -> Unit = ::doNothing,
            timeoutInMillisForStale: Long = 2500
        ): Flow<DataState<T>> = flow {

            val staleData = localData.first()

            val apiJob = coroutineScope.async {
                apiCall()
                    .logErrors()
                    .onSuccess { apiData ->
                        if (staleData != apiData) {
                            saveLocalData(apiData)
                        }
                    }
            }
            val timeoutJob = coroutineScope.async {
                delay(timeoutInMillisForStale)
                @Suppress("ThrowableNotThrown")
                Failure<T>(DataStateTimeoutException())
            }
            val list = listOf(apiJob, timeoutJob)
            val firstResult = select<ApiResult<T>> {
                list.forEach { job ->
                    job.onAwait { result ->
                        result
                    }
                }
            }
            if (firstResult is Failure && firstResult.exception is DataStateTimeoutException) {
                if (staleData != null) {
                    // Don't emit an empty list or null value on the initial load.
                    if (staleData is Collection<*>) {
                        @Suppress("UNCHECKED_CAST")
                        if (staleData.isNotEmpty()) emit(StaleData(staleData as T))
                    } else {
                        emit(StaleData(staleData))
                    }
                }
            }

            val apiResult = apiJob.await()

            val dataHasChanged = (apiResult is Success && apiResult.data != staleData)

            var emissionsSkipped = 0
            localData.collect { latestData ->
                if (latestData == staleData && dataHasChanged && emissionsSkipped <= 1 && apiResult is Success) {
                    //"skip" the first identical emission and emit the api data we just received instead
                    emissionsSkipped++
                    emit(FreshData(apiResult.data))
                } else if (emissionsSkipped == 1 && latestData == apiResult.getOrNull()) {
                    // skip this emission entirely because we just emitted the identical data above.
                    emissionsSkipped++
                } else {
                    emit (if (latestData == null) {
                        NoData()
                    } else {
                        when (apiResult) {
                            is Success -> FreshData(latestData)
                            is Failure -> FallbackData(latestData)
                        }
                    })
                }
            }
        }

        private suspend fun <T> doNothing(input: T) = delay(0)
    }
}

class DataStateTimeoutException(override val message: String = "Timeout for fresh data reached. Reverting to Stale Data until fresh data arrives."): Exception()

/**
 * This state indicates that local data has been fetched, but that an api call may still be in
 * progress. Use this state to process data that is unlikely to change.
 */
data class StaleData<T>(val localData: T): DataState<T>()

/**
 * This state indicates that the api call has failed, but local data is available to use if you want.
 */
data class FallbackData<T>(val localData: T): DataState<T>()

/**
 * This state indicates that an api call has been finished and
 */
data class FreshData<T>(val apiData: T): DataState<T>()

/**
 * This state indicates that there is no local data and attempts to obtain it have failed.
 */
class NoData<T>: DataState<T>()
