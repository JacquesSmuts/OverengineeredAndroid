package com.jacquessmuts.overengineered.ui

import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

@FlowPreview
@ExperimentalCoroutinesApi
abstract class BaseActivity <BS : BaseState, VM : BaseViewModel<BS>> : AppCompatActivity(), CoroutineScope by MainScope() {

    abstract val viewModel: VM

    override fun onStart() {
        super.onStart()
        listenToState()
    }

    abstract fun onStateUpdated(nuState: BS)

    private fun listenToState() = launch {
        viewModel.state
            .distinctUntilChanged()
            .collect {
                onStateUpdated(it)
            }
    }

    override fun onDestroy() {
        super.onDestroy()
        coroutineContext.cancelChildren()
    }
}
