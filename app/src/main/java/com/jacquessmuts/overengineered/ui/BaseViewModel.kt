package com.jacquessmuts.overengineered.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.launch
import timber.log.Timber

@FlowPreview
@ExperimentalCoroutinesApi
abstract class BaseViewModel<BS : BaseState> : ViewModel() {

    // TODO: replace with StateFlow when it comes out
    private val _baseActionChannel = ConflatedBroadcastChannel<BaseEvent>()
    val actionFlow = _baseActionChannel.asFlow()

    // TODO: replace with StateFlow when it comes out
    private val _state = BroadcastChannel<BS>(1)
    val state = _state.asFlow()

    /**
     * This is he heart of the viewmodel. Every time this state updates, the attached view will be
     * notified of the new state and update accordingly
     */
    protected fun updateState(nuState: BS) = viewModelScope.launch {
        _state.send(nuState)
    }

    protected fun pressBack() {
        broadcastEvent(BaseEvent.PressBack())
    }

    protected fun finishActivity() {
        broadcastEvent(BaseEvent.FinishActivity())
        onCleared()
    }

    private fun broadcastEvent(event: BaseEvent) {
        if (!_baseActionChannel.isClosedForSend) {
            _baseActionChannel.offer(event)
        } else {
            Timber.w("Attempted to send $event to closed $_baseActionChannel")
        }
    }
}

abstract class BaseState

/**
 * BaseEvents are the same in every Fragment, and for very generic UI events that essentially
 * translate to Android Events. Pressing Back, finishing activity, opening a new fragment, etc.
 */
sealed class BaseEvent {
    class PressBack() : BaseEvent()
    class FinishActivity : BaseEvent()
}
