package com.jacquessmuts.overengineered.ui

import androidx.fragment.app.Fragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import timber.log.Timber

@ExperimentalCoroutinesApi
@FlowPreview
abstract class BaseFragment<BS : BaseState, VM : BaseViewModel<BS>> : Fragment(),
    CoroutineScope by MainScope() {

    abstract val viewModel: VM

    /**
     * This is the heart of the fragment. Every time the viewmodel updates its state, this is called
     * and the fragment should adjust to the new state.
     */
    abstract fun onStateUpdated(nuState: BS)

    override fun onStart() {
        super.onStart()
        listenToState()
        listenToBaseEvents()
    }

    private fun listenToState() = launch {
        viewModel.state
            .distinctUntilChanged()
            .collect {
                onStateUpdated(it)
            }
    }

    private fun listenToBaseEvents() = launch {

        viewModel.actionFlow.collect { baseEvent ->
            when (baseEvent) {
                is BaseEvent.PressBack -> activity?.onBackPressed()
                is BaseEvent.FinishActivity -> activity?.finish()
            }?.let {
                Timber.d("processed actionEvent $it")
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        coroutineContext.cancelChildren()
    }
}
